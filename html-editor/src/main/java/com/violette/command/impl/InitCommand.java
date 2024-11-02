package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.HtmlDocument;

/**
 * @author Violette
 * @date 2024/10/31 12:46
 */
public class InitCommand extends Command {
    private HtmlDocument document; // 每个命令维护一个document，便于后续操作不同document

    public InitCommand(HtmlDocument document) {
        super(CommandType.IO);

        this.document = document;
    }

    @Override
    public void execute() {
        this.document.init();
    }

    @Override
    public void undo() {
        // 执行输入/输出指令后，不允许撤销与重做。
    }

    @Override
    public void redo() {
        // 执行输入/输出指令后，不允许撤销与重做。
    }
}
