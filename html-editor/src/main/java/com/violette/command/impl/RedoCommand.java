package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.command.CommandExecutor;

/**
 * @author Violette
 * @date 2024/11/1 0:05
 */
public class RedoCommand extends Command {
    private CommandExecutor commandExecutor;

    public RedoCommand(CommandExecutor commandExecutor) {
        super(CommandType.UNDO_REDO);

        this.commandExecutor = commandExecutor;
    }

    @Override
    public void execute() {
        commandExecutor.redo();
    }

    @Override
    public void undo() {
        // Redo操作本身不需要撤销
    }

    @Override
    public void redo() {
        // Redo操作本身不需要重做
    }
}