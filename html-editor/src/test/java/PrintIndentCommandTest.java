import com.violette.command.Command;
import com.violette.command.impl.PrintIndentCommand;
import com.violette.editor.HtmlDocument;
import com.violette.editor.TagElement;
import com.violette.editor.TextElement;
import com.violette.utils.CapturingPrintStream;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Violette
 * @date 2024/11/2 13:41
 */
public class PrintIndentCommandTest {
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
            <html>
              <head>
                <title>
                </title>
              </head>
              <body>
                <div id="div1">
                  <p id="p1">
                    Hello World
                  </p>
                  <p id="p2">
                  </p>
                </div>
                <span id="span1">
                  Span Text
                </span>
              </body>
            </html>
         */
    }

    /*
     * 测试打印缩进格式
     * */
    @Test
    public void testPrintIndent() {
        Command printIndentCommand = new PrintIndentCommand(document, 2); // 使用2个空格缩进

        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("<html>\n");
        expectedOutput.append("  <head>\n");
        expectedOutput.append("    <title>\n");
        expectedOutput.append("    </title>\n");
        expectedOutput.append("  </head>\n");
        expectedOutput.append("  <body>\n");
        expectedOutput.append("    <div id=\"div1\">\n");
        expectedOutput.append("      <p id=\"p1\">\n");
        expectedOutput.append("        Hello World\n");
        expectedOutput.append("      </p>\n");
        expectedOutput.append("      <p id=\"p2\">\n");
        expectedOutput.append("      </p>\n");
        expectedOutput.append("    </div>\n");
        expectedOutput.append("    <span id=\"span1\">\n");
        expectedOutput.append("      Span Text\n");
        expectedOutput.append("    </span>\n");
        expectedOutput.append("  </body>\n");
        expectedOutput.append("</html>\n");

        // 捕获标准输出
        CapturingPrintStream capture = CapturingPrintStream.capture();
        printIndentCommand.execute();
        CapturingPrintStream.release();

        // 验证输出是否符合预期
        assertEquals(expectedOutput.toString(), capture.getContents());
    }
}