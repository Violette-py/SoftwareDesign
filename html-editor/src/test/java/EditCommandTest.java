import com.violette.command.impl.EditCommand;
import com.violette.editor.HtmlEditor;
import com.violette.editor.Session;
import com.violette.exception.NotExistsException;
import com.violette.exception.RepeatedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;


/**
 * @author Violette
 * @date 2024/12/13 16:34
 */
public class EditCommandTest {
    private Session session;

    @Before
    public void setUp() throws IOException, RepeatedException {
        this.session = new Session();

        // 创建几个HtmlEditor实例用于测试
        HtmlEditor editor1 = this.session.addEditor("file1.html");
        editor1.setIsSaved(true);

        HtmlEditor editor2 = this.session.addEditor("file2.html");
        editor2.setIsSaved(false);

        session.setActiveEditor(editor1); // 编辑器1当前活跃
    }

    /*
     * 文件未装入editor，抛出异常
     * */
    @Test(expected = NotExistsException.class)
    public void testEditCommandWithNonExistentFile() throws Exception {
        // 测试未装入Editor的文件是否抛出指定异常
        new EditCommand(session, "non_exist.html").execute();
    }

    /*
     * 文件已装入editor，正常切换
     * */
    @Test
    public void testEditCommandWithExistentFile() {
        // 测试已装入editor的文件，正常切换
        try {
            HtmlEditor targetEditor = session.getEditorList().get(1);
            EditCommand editCommand = new EditCommand(session, targetEditor.getFilepath());
            editCommand.execute();

            // 验证activeEditor是否被设置为targetEditor
            Assertions.assertEquals(targetEditor, session.getActiveEditor());
        } catch (Exception e) {
            fail();
        }
    }
}
