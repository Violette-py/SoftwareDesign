package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.command.CommandExecutor;

/**
 * @author Violette
 * @date 2024/11/1 0:04
 */
public class UndoCommand extends Command {
    private CommandExecutor commandExecutor;

    public UndoCommand(CommandExecutor commandExecutor) {
        super(CommandType.UNDO_REDO);

        this.commandExecutor = commandExecutor;
    }

    @Override
    public void execute() {
        commandExecutor.undo();
    }

    @Override
    public void undo() {
        // Undo操作本身不需要撤销
    }

    @Override
    public void redo() {
        // Undo操作本身不需要重做
    }
}
