package invoker;

import command.Command;

import java.util.Stack;

public class CommandExecutor {

    // 用双栈实现撤销、重做操作
    private static final Stack<Command> undoStack = new Stack<>();
    private static final Stack<Command> redoStack = new Stack<>();

    // TODO: 需要判断前一个操作为 undo时才能 redo，否则应该清空 redoStack

    public void execute(Command command) {
        command.execute();
        undoStack.push(command);
    }

    public void undoLastCommand() {
        if (!undoStack.isEmpty()) {
            Command lastCommand = undoStack.pop();
            lastCommand.undo();
            redoStack.push(lastCommand);
        }
    }

}
