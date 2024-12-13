import com.violette.command.impl.EditorListCommand;
import com.violette.editor.HtmlEditor;
import com.violette.utils.CapturingPrintStream;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Violette
 * @date 2024/12/13 16:07
 */
public class EditorListCommandTest {
    private List<HtmlEditor> editorList;
    private HtmlEditor activeEditor;

    @Before
    public void setUp() throws IOException {
        editorList = new ArrayList<>();
        // 创建几个HtmlEditor实例用于测试
        for (int i = 0; i < 3; i++) {
            String filepath = "file" + i + ".html";
            HtmlEditor editor = new HtmlEditor(filepath);
            editor.setIsSaved(i != 2); // 假设第三个编辑器未保存
            editorList.add(editor);
        }

        activeEditor = editorList.get(1); // 假设第二个编辑器为活跃状态
    }

    @Test
    public void testEditorListCommand() {
        try {
            EditorListCommand editorListCommand = new EditorListCommand(editorList, activeEditor);

            StringBuilder expectedOutput = new StringBuilder();
            for (int i = 0; i < editorList.size(); i++) {
                HtmlEditor editor = editorList.get(i);
                String prefix = (i == 1) ? "> " : "  "; // 活跃编辑器前显示">"
                expectedOutput.append(prefix);
                expectedOutput.append(editor.getFilepath());
                if (!editor.getIsSaved()) {
                    expectedOutput.append(" *"); // 未保存的编辑器后显示"*"
                }
                expectedOutput.append("\n");
            }

            // 捕获标准输出
            CapturingPrintStream capture = CapturingPrintStream.capture();
            editorListCommand.execute();
            CapturingPrintStream.release();

            // 验证输出是否符合预期
            assertEquals(expectedOutput.toString().strip(), capture.getContents().strip());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
