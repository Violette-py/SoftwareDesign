package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.document.HtmlDocument;
import com.violette.document.TagElement;
import com.violette.document.TextElement;
import com.violette.exception.NotExistsException;
import com.violette.exception.RepeatedException;
import com.violette.utils.DocumentUtil;

/**
 * @author Violette
 * @date 2024/10/31 13:19
 * @description 在某元素内插入新元素
 * <p>
 * append tagName idValue parentElement [textContent]
 * @Param tagName 新元素的元素标签
 * @Param idValue 新元素的 id，注意 id 不能与其他元素重复
 * @Param parentElement 目标父元素的 id，新元素将被插入到该元素内部，并且成为该元素的最后一个子元素
 * @Param textContent 可选参数，表示新元素中的文本内容
 */
public class AppendCommand extends Command {
    private HtmlDocument document;
    private TagElement newElement;
    private TagElement parentElement; // 目标父元素

    public AppendCommand(HtmlDocument document, String tagName, String idValue, String parentElementId, String textContent) throws NotExistsException, RepeatedException {
        super(CommandType.EDIT);

        this.document = document;
        // 寻找目标元素，即为新元素的父元素
        this.parentElement = DocumentUtil.findElementById(document, parentElementId);
        if (this.parentElement == null) {
            throw new NotExistsException("id", parentElementId);
        }
        // 确保id唯一
        if (DocumentUtil.isIdExists(document, idValue)) {
            throw new RepeatedException("id", idValue);
        }
        this.newElement = new TagElement(tagName, idValue);
        // 插入文本内容
        if (textContent != null && !textContent.isEmpty()) {
            this.newElement.addChild(new TextElement(textContent));
        }
    }

    @Override
    public void execute() {
        // 在目标父元素内部添加新元素
        parentElement.addChild(newElement);
    }

    @Override
    public void undo() {
        parentElement.removeChild(newElement);
    }

    @Override
    public void redo() {
        parentElement.addChild(newElement); // 重新添加新元素
    }
}
