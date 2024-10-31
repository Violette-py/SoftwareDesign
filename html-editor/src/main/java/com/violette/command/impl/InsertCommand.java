package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.HtmlDocument;
import com.violette.editor.TagElement;
import com.violette.editor.TextElement;
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
public class InsertCommand implements Command {
    private HtmlDocument document;
    private TagElement newElement;
    private TagElement targetElement;   // 参考元素，即新元素将插入到该元素之前
    private TagElement parentElement; // 参考元素的直接父节点，用于插入新元素
    private String textContent;

    public InsertCommand(HtmlDocument document, String tagName, String idValue, String insertLocation, String textContent) throws NotExistsException, RepeatedException {
        this.document = document;
        this.textContent = textContent;
        // 寻找插入位置
        Pair<TagElement, TagElement> result = DocumentUtil.findTargetElementAndItsParent(document, insertLocation);
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
    }

    @Override
    public void execute() {
        int index = parentElement.getChildren().indexOf(targetElement);
        parentElement.getChildren().add(index, newElement); // 在 refElement 之前插入
        if (textContent != null && !textContent.isEmpty()) {  // FIXME: 空格是否要作为文本内容
            newElement.addChild(new TextElement(textContent)); // 添加文本内容作为子元素
        }
    }

    @Override
    public void undo() {
        parentElement.getChildren().remove(newElement);
    }

    @Override
    public void redo() {
        int index = parentElement.getChildren().indexOf(targetElement);
        parentElement.getChildren().add(index, newElement); // 在 refElement 之前插入
    }
}
