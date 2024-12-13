package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.Session;

/**
 * @author Violette
 * @date 2024/12/13 22:57
 */
public class ShowIdCommand extends Command {
    private Session session;
    private boolean showId;

    /**
     * ShowIdCommand 的构造函数。
     *
     * @param session 当前会话。
     */
    public ShowIdCommand(Session session, boolean showId) {
        super(CommandType.DISPLAY);

        this.session = session;
        this.showId = showId;
    }

    @Override
    public void execute() {
        this.session.setCurrEditorShowId(showId);
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }
}
