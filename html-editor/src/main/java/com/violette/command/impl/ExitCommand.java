package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.Session;

/**
 * @author Violette
 * @date 2024/12/13 21:21
 */
public class ExitCommand extends Command {
    private Session session;

    /**
     * ExitCommand 的构造函数。
     *
     * @param session 当前会话。
     */
    public ExitCommand(Session session) {
        super(CommandType.IO);

        this.session = session;
    }

    @Override
    public void execute() {
        session.exit();
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }
}
