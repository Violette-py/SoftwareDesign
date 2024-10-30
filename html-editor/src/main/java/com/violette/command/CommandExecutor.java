package com.violette.command;

import java.util.Stack;

/**
 * @author Violette
 * @date 2024/10/31 1:31
 * @description 命令模式中的invoker,负责调用命令(传递给具体命令执行),管理命令的执行顺序(保存执行历史和重做栈)
 */
public class CommandExecutor {
    private Stack<Command> history = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        history.push(command);
        redoStack.clear();  // why
    }

    public void undo() { // ctrl+Z,撤销命令
        if (!history.isEmpty()) {
            Command command = history.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() { // ctrl+Y,恢复命令
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.redo();
            history.push(command);
        }
    }
}
