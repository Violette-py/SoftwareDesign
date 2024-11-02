import com.violette.command.Command;
import com.violette.command.CommandExecutor;
import com.violette.editor.HtmlDocument;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Violette
 * @date 2024/11/2 15:32
 */
public class RedoCommandTest {
    private CommandExecutor commandExecutor;
    private HtmlDocument document;

    @Before
    public void setUp() {
        document = new HtmlDocument();
        commandExecutor = new CommandExecutor();
    }

    // 编辑类命令的redo测试
    @Test
    public void testRedoEditCommand() {
        Command editCommand = mock(Command.class);
        when(editCommand.isDisplayCommand()).thenReturn(false);
        when(editCommand.isIOCommand()).thenReturn(false);

        commandExecutor.executeCommand(editCommand);
        commandExecutor.undo();
        commandExecutor.redo();

        verify(editCommand).redo();
        assertTrue(commandExecutor.getRedoStack().isEmpty());
        assertEquals(1, commandExecutor.getHistory().size());
    }

    // 显示类命令的redo测试
    @Test
    public void testRedoDisplayCommand() {
        Command displayCommand = mock(Command.class);
        when(displayCommand.isDisplayCommand()).thenReturn(true);

        commandExecutor.executeCommand(displayCommand);
        commandExecutor.undo();
        commandExecutor.redo();

        verify(displayCommand, never()).redo(); // 验证显示类命令不应该调用redo
        assertTrue(commandExecutor.getRedoStack().isEmpty()); // 验证重做栈没有变化
        assertTrue(commandExecutor.getHistory().isEmpty()); // 验证历史栈为空
    }

    // 输入输出类命令的redo测试
    @Test
    public void testRedoIOCommand() {
        Command ioCommand = mock(Command.class);
        when(ioCommand.isIOCommand()).thenReturn(true);

        commandExecutor.executeCommand(ioCommand);
        commandExecutor.undo();
        commandExecutor.redo();

        verify(ioCommand, never()).redo(); // 验证输入输出类命令不应该调用redo
        assertTrue(commandExecutor.getRedoStack().isEmpty()); // 验证重做栈没有变化
        assertTrue(commandExecutor.getHistory().isEmpty()); // 验证历史栈为空
    }
}
