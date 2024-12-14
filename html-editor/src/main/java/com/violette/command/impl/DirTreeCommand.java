package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.Session;

/**
 * @author Violette
 * @date 2024/12/14 2:57
 */
public class DirTreeCommand extends Command {
    private Session session;

    public DirTreeCommand(Session session) {
        super(CommandType.DISPLAY);

        this.session = session;
    }

    @Override
    public void execute() {
        session.dirTree();
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }
}
