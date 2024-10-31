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

    }

    @Override
    public void redo() {

    }
}
