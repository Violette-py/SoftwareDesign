package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.document.HtmlDocument;
import com.violette.utils.SpellChecker;

/**
 * @author Violette
 * @date 2024/10/31 19:04
 */
public class SpellCheckCommand extends Command {
    private HtmlDocument document;

    /**
     * SpellCheckCommand 的构造函数。
     *
     * @param document HTML文档。
     */
    public SpellCheckCommand(HtmlDocument document) {
        super(CommandType.DISPLAY);

        this.document = document;
    }

    @Override
    public void execute() {
        // 遍历HTML文档中的所有文本元素，并检查拼写错误
        SpellChecker.traverseAndCheckText(document);
    }

    @Override
    public void undo() {
        // 显示类命令不需要撤销
    }

    @Override
    public void redo() {
        // 显示类命令不需要重做
    }
}