import com.violette.command.impl.CloseCommand;
import com.violette.editor.HtmlEditor;
import com.violette.editor.Session;
import com.violette.exception.NotExistsException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Violette
 * @date 2024/12/13 20:02
 */
public class CloseCommandTest {
    private Session session;
    private HtmlEditor activeEditor;
    private HtmlEditor otherEditor;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        session = new Session();
        activeEditor = session.addEditor("file1.html");
        otherEditor = session.addEditor("file2.html");
        session.setActiveEditor(activeEditor);
    }

    /*
     * 没有 activeEditor时，close会报错
     * */
    @Test(expected = NotExistsException.class)
    public void testCloseCommandWithNoActiveEditor() {
        session.setActiveEditor(null);
        new CloseCommand(session).execute();
    }

    /*
     * activeEditor编辑过
     * */
    @Test
    public void testCloseCommandWithUnsavedActiveEditorAndSave() throws Exception {
        // 模拟用户输入 "y" 来保存文件
        String input = "y";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        // 执行 CloseCommand
        new CloseCommand(session).execute();

        // 验证 activeEditor 是否被设置为 otherEditor
        assertEquals(otherEditor, session.getActiveEditor());

        // 恢复 System.in
        System.setIn(System.in);
    }


    /*
     * activeEditor已保存
     * */
    @Test
    public void testCloseCommandWithSavedActiveEditor() throws Exception {
        new CloseCommand(session).execute();
        assertEquals(otherEditor, session.getActiveEditor());
    }

    /*
     * 列表中不存在 editor
     * */
    @Test
    public void testCloseCommandWithLastEditor() throws Exception {
        session.getEditorList().clear();
        session.setActiveEditor(activeEditor);

        new CloseCommand(session).execute();

        assertNull(session.getActiveEditor());
    }
}