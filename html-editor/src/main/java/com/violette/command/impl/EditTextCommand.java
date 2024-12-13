package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.document.HtmlDocument;
import com.violette.document.TagElement;
import com.violette.document.TextElement;
import com.violette.exception.NotExistsException;
import com.violette.utils.DocumentUtil;

/**
 * @author Violette
 * @date 2024/10/31 15:41
 */
public class EditTextCommand extends Command {
    private HtmlDocument document; // HTML文档
    private TagElement targetElement; // 目标元素
    private TextElement oldTextElement; // 旧的文本元素
    private String newTextContent; // 新的文本内容

    /**
     * EditTextCommand 的构造函数。
     *
     * @param document       HTML文档。
     * @param elementId      要编辑的元素的ID。
     * @param newTextContent 新的文本内容，可以为空。
     * @throws NotExistsException 如果指定的元素ID不存在。
     */
    public EditTextCommand(HtmlDocument document, String elementId, String newTextContent) throws NotExistsException {
        super(CommandType.EDIT);

        this.document = document;
        this.newTextContent = newTextContent;

        // 查找指定ID的元素
        this.targetElement = DocumentUtil.findElementById(document, elementId);
        if (this.targetElement == null) {
            throw new NotExistsException("id", elementId);
        }

        // 找到并保存旧的文本元素（可能不存在）
        this.oldTextElement = DocumentUtil.findTextElementInTagElement(targetElement);
    }

    @Override
    public void execute() {
        // 移除旧的文本元素（如果有）
        if (oldTextElement != null) {
            targetElement.removeChild(oldTextElement);
        }

        // 如果有新的文本内容，添加为第一个孩子
        if (newTextContent != null && !newTextContent.isEmpty()) {
            TextElement newTextElement = new TextElement(newTextContent);
            targetElement.addChild(0, newTextElement);
        }
    }

    @Override
    public void undo() {
        // 移除新的文本元素（如果有）
        if (newTextContent != null && !newTextContent.isEmpty()) {
            TextElement newTextElement = DocumentUtil.findTextElementInTagElement(targetElement);
            if (newTextElement != null) {
                targetElement.removeChild(newTextElement);
            }
        }

        // 撤销操作：恢复旧的文本元素（如果有）
        if (oldTextElement != null) {
            targetElement.addChild(0, oldTextElement);
        }
    }

    @Override
    public void redo() {
        // 重做操作：执行相同的编辑操作
        execute();
    }
}