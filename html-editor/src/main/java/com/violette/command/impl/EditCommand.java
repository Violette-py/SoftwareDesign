package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.Session;
import com.violette.exception.NotExistsException;
import lombok.SneakyThrows;

/**
 * @author Violette
 * @date 2024/12/13 16:18
 */
public class EditCommand extends Command {
    private Session session;
    private String filepath;

    public EditCommand(Session session, String filepath) throws NotExistsException {
        super(CommandType.IO); // 视为IO，不允许做undo和redo

        this.session = session;
        this.filepath = filepath;
    }

    @SneakyThrows
    @Override
    public void execute() {
        this.session.switchActiveEditor(this.filepath);
    }

    @Override
    public void undo() {

    }

    @Override
    public void redo() {

    }
}
