import com.violette.command.Command;
import com.violette.command.impl.SaveCommand;
import com.violette.editor.HtmlDocument;
import com.violette.editor.TagElement;
import com.violette.editor.TextElement;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;

/**
 * @author Violette
 * @date 2024/10/31 23:36
 */
public class SaveCommandTest {
    private HtmlDocument document;
    private File tempFile;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        document = new HtmlDocument();
        document.init();

        // 向 body 添加一些元素用于测试
        TagElement p1 = new TagElement("p", "p1");
        TextElement text = new TextElement("Hello World");
        p1.addChild(text);
        TagElement p2 = new TagElement("p", "p2");
        TagElement div = new TagElement("div", "div1");
        div.addChild(p1);
        div.addChild(p2);
        document.getBody().addChild(div);

        TagElement span = new TagElement("span", "span1");
        TextElement spanText = new TextElement("Span Text");
        span.addChild(spanText);
        document.getBody().addChild(span);
    }

    @Test
    public void testSaveHtmlFile() throws IOException {
        // 创建一个临时文件路径
        tempFile = testFolder.newFile("testSave.html");
        String tempFilePath = tempFile.getAbsolutePath();

        // 执行SaveCommand
        Command saveCommand = new SaveCommand(document, tempFilePath);
        saveCommand.execute();

        // 验证文件内容是否正确
        String content = Files.readString(tempFile.toPath());
        assertTrue(content.contains("<div id=\"div1\">"));
        assertTrue(content.contains("<p id=\"p1\">Hello World</p>"));
        assertTrue(content.contains("<p id=\"p2\"></p>"));
        assertTrue(content.contains("<span id=\"span1\">Span Text</span>"));

        // 验证文件路径
        System.out.println("File saved to: " + tempFile.getAbsolutePath());
    }
}
