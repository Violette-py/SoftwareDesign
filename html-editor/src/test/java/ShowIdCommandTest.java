import com.violette.command.impl.ShowIdCommand;
import com.violette.document.HtmlDocument;
import com.violette.document.TagElement;
import com.violette.document.TextElement;
import com.violette.editor.HtmlEditor;
import com.violette.editor.Session;
import com.violette.utils.CapturingPrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author Violette
 * @date 2024/12/13 23:46
 */
public class ShowIdCommandTest {
    private Session session;
    private HtmlEditor editor;
    private HtmlDocument document;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @Before
    public void setUp() {
        session = new Session();
        editor = session.addEditor("unknown.html");
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        document = editor.getDocument();
        document.init();
        // 添加元素到document中
        TagElement h1 = new TagElement("h1", "header");
        TextElement h1Text = new TextElement("Welcome to my webpage");
        h1.addChild(h1Text);
        document.getBody().addChild(h1);

        TagElement p = new TagElement("p", "paragraph");
        TextElement pText = new TextElement("This is a new paragraph.");
        p.addChild(pText);
        document.getBody().addChild(p);
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testShowIdCommandTrue() {
        // 执行showid true命令
        new ShowIdCommand(session, true).execute();

        // 打印树形结构并捕获标准输出
        CapturingPrintStream capture = CapturingPrintStream.capture();
        document.printTree();
        CapturingPrintStream.release();

        // 验证输出是否包含id
        assertEquals("html\n" +
                "├── head\n" +
                "│   └── title\n" +
                "└── body\n" +
                "    ├── h1#header\n" +
                "    │   └── Welcome to my webpage\n" +
                "    └── p#paragraph\n" +
                "        └── This is a new paragraph.", capture.getContents().strip());
    }

    @Test
    public void testShowIdCommandFalse() {
        // 执行showid false命令
        new ShowIdCommand(session, false).execute();

        // 打印树形结构并捕获输出
        CapturingPrintStream capture = CapturingPrintStream.capture();
        document.printTree();
        CapturingPrintStream.release();

        // 验证输出是否不包含id
        assertEquals("html\n" +
                "├── head\n" +
                "│   └── title\n" +
                "└── body\n" +
                "    ├── h1\n" +
                "    │   └── Welcome to my webpage\n" +
                "    └── p\n" +
                "        └── This is a new paragraph.", capture.getContents().strip());
    }
}
