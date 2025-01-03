package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.document.HtmlDocument;

/**
 * @author Violette
 * @date 2024/10/31 3:24
 */
public class PrintIndentCommand extends Command {
    private HtmlDocument document;
    private int indent;

    public PrintIndentCommand(HtmlDocument document, int indent) {
        super(CommandType.DISPLAY);

        this.document = document;
        this.indent = indent;
    }

    @Override
    public void execute() {
        document.printIndent(indent, 0);
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
