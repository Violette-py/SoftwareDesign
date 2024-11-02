/**
 * @author Violette
 * @date 2024/11/2 15:24
 */

import com.violette.command.Command;
import com.violette.command.CommandExecutor;
import com.violette.editor.HtmlDocument;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UndoCommandTest {
    private CommandExecutor commandExecutor;
    private HtmlDocument document;

    @Before
    public void setUp() {
        document = new HtmlDocument();
        document.init();
        commandExecutor = new CommandExecutor();
    }

    // 编辑类命令的undo测试
    @Test
    public void testUndoEditCommand() {
        Command editCommand = mock(Command.class);
        when(editCommand.getCommandType()).thenReturn(Command.CommandType.EDIT);

        commandExecutor.executeCommand(editCommand);
        commandExecutor.undo();

        verify(editCommand).undo();
        assertTrue(commandExecutor.getHistory().isEmpty());
        assertEquals(1, commandExecutor.getRedoStack().size());
    }

    // 显示类命令的undo测试
    @Test
    public void testUndoDisplayCommand() {
        Command displayCommand = mock(Command.class);
        when(displayCommand.getCommandType()).thenReturn(Command.CommandType.DISPLAY);

        commandExecutor.executeCommand(displayCommand);
        commandExecutor.undo();

        verify(displayCommand, never()).undo(); // 验证显示类命令不应该调用undo
        assertTrue(commandExecutor.getHistory().isEmpty()); // 验证历史栈没有变化
        assertTrue(commandExecutor.getRedoStack().isEmpty()); // 验证重做栈为空
    }

    // 输入输出类命令的undo测试
    @Test
    public void testUndoIOCommand() {
        Command ioCommand = mock(Command.class);
        when(ioCommand.getCommandType()).thenReturn(Command.CommandType.IO);

        commandExecutor.executeCommand(ioCommand);
        commandExecutor.undo();

        verify(ioCommand, never()).undo(); // 验证输入输出类命令不应该调用undo
        assertTrue(commandExecutor.getHistory().isEmpty()); // 验证历史栈没有变化
        assertTrue(commandExecutor.getRedoStack().isEmpty()); // 验证重做栈为空
    }
}