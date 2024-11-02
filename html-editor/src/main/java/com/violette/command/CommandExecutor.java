package com.violette.command;

import lombok.Data;

import java.util.Stack;

/**
 * @author Violette
 * @date 2024/10/31 1:31
 * @description 命令模式中的invoker, 负责调用命令(传递给具体命令执行), 管理命令的执行顺序(保存执行历史和重做栈)
 * <p>
 * 支持多步撤销与多步重做，规则如下：
 * - 编辑类指令：支持撤销与重做。如果在撤销后发生了编辑，则此时不允许重做操作。
 * - 显示类指令：撤销与重做时应跳过显示类指令。
 * - 输入/输出指令：执行输入/输出指令后，不允许撤销与重做。
 */
@Data
public class CommandExecutor {
    private Stack<Command> history = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();

        // 只有编辑类命令可以重做（放入history）
        if (command.getCommandType().equals(Command.CommandType.EDIT)) {
            history.push(command);
            // 若执行了编辑类命令，则不可redo
            redoStack.clear();
        } else if (command.getCommandType().equals(Command.CommandType.IO)) {
            //执行输入/输出命令后，不可undo或redo
            history.clear();
            redoStack.clear();
        }
    }

    public void undo() { // ctrl+Z,撤销命令
        if (!history.isEmpty()) {
            Command command = history.pop();
            if (command.getCommandType().equals(Command.CommandType.EDIT)) { // 只有编辑类命令可以撤销
                command.undo();
                redoStack.push(command);
            }
        }
    }

    public void redo() { // ctrl+Y,恢复命令
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            if (command.getCommandType().equals(Command.CommandType.EDIT)) { // 只有编辑类命令可以重做
                command.redo();
                history.push(command);
            }
        }
    }
}
