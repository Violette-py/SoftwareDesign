package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.Session;

/**
 * @author Violette
 * @date 2024/12/14 2:54
 */
public class DirIndentCommand extends Command {
    private Session session;
    private int indent;

    public DirIndentCommand(Session session, int indent) {
        super(CommandType.DISPLAY);

        this.session = session;
        this.indent = indent;
    }

    @Override
    public void execute() {
        session.dirIndent(indent);
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }
}
