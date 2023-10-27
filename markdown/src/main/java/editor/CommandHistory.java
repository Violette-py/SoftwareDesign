package editor;

import java.util.Stack;

public class CommandHistory {

    // 用双栈实现撤销、重做操作
    private static final Stack<Command> undoStack = new Stack<>();
    private static final Stack<Command> redoStack = new Stack<>();

    void execute(Command command) {
        command.execute();
        undoStack.push(command);
    }

    void undoLastCommand() {
        if (!undoStack.isEmpty()) {
            Command lastCommand = undoStack.pop();
            lastCommand.undo();
            redoStack.push(lastCommand);
        }
    }

}
