import com.violette.command.Command;
import com.violette.command.impl.PrintTreeCommand;
import com.violette.editor.HtmlDocument;
import com.violette.editor.TagElement;
import com.violette.editor.TextElement;
import com.violette.utils.CapturingPrintStream;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Violette
 * @date 2024/10/31 11:12
 */
public class PrintTreeCommandTest {
    private HtmlDocument document;

    @Before
    public void setUp() {
        this.document = new HtmlDocument();
        this.document.init();

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

        /*
          html
            ├── head
            │   └── title
            └── body
                ├── div#div1
                │   ├── p#p1
                │   │   └── Hello World
                │   └── p#p2
                └── span#span1
                    └── Span Text
        */
    }

    /*
     * 测试打印树结构
     * */
    @Test
    public void testPrintTree() {
        Command printTreeCommand = new PrintTreeCommand(document);

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("html\n");
        expectedOutput.append("├── head\n");
        expectedOutput.append("│   └── title\n");
        expectedOutput.append("└── body\n");
        expectedOutput.append("    ├── div#div1\n");
        expectedOutput.append("    │   ├── p#p1\n");
        expectedOutput.append("    │   │   └── Hello World\n");
        expectedOutput.append("    │   └── p#p2\n");
        expectedOutput.append("    └── span#span1\n");
        expectedOutput.append("        └── Span Text\n");

        // 捕获标准输出
        CapturingPrintStream capture = CapturingPrintStream.capture();
        printTreeCommand.execute();
        CapturingPrintStream.release();

        // 验证输出是否符合预期
        assertEquals(expectedOutput.toString(), capture.getContents());
    }
}
