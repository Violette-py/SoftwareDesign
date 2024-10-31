package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.HtmlDocument;

/**
 * @author Violette
 * @date 2024/10/31 11:10
 */
public class PrintTreeCommand implements Command {
    private HtmlDocument document;

    public PrintTreeCommand(HtmlDocument document) {
        this.document = document;
    }

    @Override
    public void execute() {
        document.printTree(null, "");
    }

    @Override
    public void undo() {
        // 显示类命令不需要撤销
    }

    @Override
    public void redo() {
        // 显示类命令不需要重做
    }

    @Override
    public boolean isDisplayCommand() {
        return true;
    }

    @Override
    public boolean isIOCommand() {
        return false;
    }
}
