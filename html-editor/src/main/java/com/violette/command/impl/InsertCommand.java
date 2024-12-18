package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.document.HtmlDocument;
import com.violette.document.TagElement;
import com.violette.document.TextElement;
import com.violette.exception.NotExistsException;
import com.violette.exception.RepeatedException;
import com.violette.utils.DocumentUtil;
import com.violette.utils.Pair;
import lombok.Data;

/**
 * @author Violette
 * @date 2024/10/30 23:58
 */
@Data
public class InsertCommand extends Command {
    private HtmlDocument document;
    private TagElement newElement;
    private TagElement targetElement;   // 参考元素，即新元素将插入到该元素之前
    private TagElement parentElement; // 参考元素的直接父节点，用于插入新元素

    public InsertCommand(HtmlDocument document, String tagName, String idValue, String insertLocation, String textContent) throws NotExistsException, RepeatedException {
        super(CommandType.EDIT);

        this.document = document;
        // 寻找插入位置
        Pair<TagElement, TagElement> result = DocumentUtil.findElementAndItsParentById(document, insertLocation);
        this.targetElement = result.first();
        this.parentElement = result.second();
        if (this.targetElement == null || this.parentElement == null) {
            throw new NotExistsException("id", insertLocation);
        }
        // 确保id唯一
        if (DocumentUtil.isIdExists(document, idValue)) {
            throw new RepeatedException("id", idValue);
        }
        this.newElement = new TagElement(tagName, idValue);
        // 插入文本内容
        if (textContent != null && !textContent.isEmpty()) {  // FIXME: 空格是否要作为文本内容
            this.newElement.addChild(new TextElement(textContent)); // 添加文本内容作为子元素
        }
    }

    @Override
    public void execute() {
        // 在 targetElement 之前插入
        int index = parentElement.getChildren().indexOf(targetElement);
        parentElement.addChild(index, newElement);
    }

    @Override
    public void undo() {
        parentElement.removeChild(newElement);
    }

    @Override
    public void redo() {
        // 在 targetElement 之前插入
        int index = parentElement.getChildren().indexOf(targetElement);
        parentElement.addChild(index, newElement);
    }
}
