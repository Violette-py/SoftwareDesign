package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.HtmlDocument;

/**
 * @author Violette
 * @date 2024/10/31 12:46
 */
public class InitCommand implements Command {
    private HtmlDocument document; // 每个命令维护一个document，便于后续操作不同document

    public InitCommand(HtmlDocument document) {
        this.document = document;
    }

    @Override
    public void execute() {
        this.document.init();
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }
}
