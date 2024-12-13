package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.Session;

/**
 * @author Violette
 * @date 2024/12/13 18:57
 */
public class CloseCommand extends Command {
    private Session session;

    public CloseCommand(Session session) {
        super(CommandType.IO);

        this.session = session;
    }

    @Override
    public void execute() {
        this.session.closeCurrEditor();
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }
}
