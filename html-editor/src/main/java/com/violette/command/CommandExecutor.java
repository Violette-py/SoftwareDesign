package com.violette.command;

import lombok.Data;

import java.util.Stack;

/**
 * @author Violette
 * @date 2024/10/31 1:31
 * @description 命令模式中的invoker,负责调用命令(传递给具体命令执行),管理命令的执行顺序(保存执行历史和重做栈)
 */
@Data
public class CommandExecutor {
    private Stack<Command> history = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        // 跳过显示类命令，执行IO命令后不可撤销及重做
        if (!command.isDisplayCommand() && !command.isIOCommand()) {
            history.push(command);
            redoStack.clear();
        }
    }

    public void undo() { // ctrl+Z,撤销命令
        if (!history.isEmpty() && !history.peek().isDisplayCommand() && !history.peek().isIOCommand()) {
            Command command = history.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() { // ctrl+Y,恢复命令
        if (!redoStack.isEmpty() && !redoStack.peek().isDisplayCommand() && !redoStack.peek().isIOCommand()) {
            Command command = redoStack.pop();
            command.redo();
            history.push(command);
        }
    }
}
