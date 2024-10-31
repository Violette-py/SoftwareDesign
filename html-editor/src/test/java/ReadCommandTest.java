/**
 * @author Violette
 * @date 2024/10/31 22:34
 */
import com.violette.command.Command;
import com.violette.command.impl.ReadCommand;
import com.violette.editor.HtmlDocument;
import com.violette.editor.HtmlElement;
import com.violette.editor.TagElement;
import com.violette.editor.TextElement;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class ReadCommandTest {
    private HtmlDocument document;
    private String testHtmlContent;
    private File testHtmlFile;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        document = new HtmlDocument();
        testHtmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "    <head>\n" +
                "        <title>HTML Document: Test 1</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <h1 id=\"header\">Header</h1>\n" +
                "        <p id=\"paragraph1\">This is a paragraph.</p>\n" +
                "        <ul id=\"list\">\n" +
                "            <li id=\"sub1\">List item 1</li>\n" +
                "            <li id=\"sub2\">List item 2</li>\n" +
                "            <li id=\"sub3\">List item 3</li>\n" +
                "        </ul>\n" +
                "        <div id=\"content\">\n" +
                "            <p id=\"paragraph2\">This is some content inside a div.</p>\n" +
                "        </div>\n" +
                "    </body>\n" +
                "</html>";
        testHtmlFile = testFolder.newFile("test.html");
        Path path = testHtmlFile.toPath();
        Files.writeString(path, testHtmlContent);
    }

    @Test
    public void testReadHtmlFile() {
        Command readCommand = new ReadCommand(document, testHtmlFile.getAbsolutePath());
        readCommand.execute();

        System.out.println("after read:");
        document.printTree();

        // 验证HTML结构是否正确读取
        TagElement htmlElement = document;
        assertEquals("html", htmlElement.getTagName());
        assertEquals(2, htmlElement.getChildren().size()); // html should have head and body

        TagElement head = findChildById(htmlElement, "head");
        assertNotNull("Head element should not be null", head);
        assertEquals("head", head.getTagName());
        assertEquals(1, head.getChildren().size()); // head should have title

        TagElement title = findChildById(head, "title");
        assertNotNull("Title element should not be null", title);
        assertEquals("title", title.getTagName());
        assertEquals("HTML Document: Test 1", ((TextElement) title.getChildren().get(0)).getText());

        TagElement body = findChildById(htmlElement, "body");
        assertNotNull("Body element should not be null", body);
        assertEquals("body", body.getTagName());
        assertEquals(4, body.getChildren().size()); // body should have h1, p, ul, and div

        TagElement h1 = findChildById(body, "header");
        assertNotNull("H1 element should not be null", h1);
        assertEquals("h1", h1.getTagName());
        assertEquals("Header", ((TextElement) h1.getChildren().get(0)).getText());

        TagElement p1 = findChildById(body, "paragraph1");
        assertNotNull("P1 element should not be null", p1);
        assertEquals("p", p1.getTagName());
        assertEquals("This is a paragraph.", ((TextElement) p1.getChildren().get(0)).getText());

        TagElement ul = findChildById(body, "list");
        assertNotNull("UL element should not be null", ul);
        assertEquals("ul", ul.getTagName());
        assertEquals(3, ul.getChildren().size()); // ul should have 3 li elements

        TagElement div = findChildById(body, "content");
        assertNotNull("Div element should not be null", div);
        assertEquals("div", div.getTagName());
        assertEquals(1, div.getChildren().size()); // div should have 1 p element

        TagElement p2 = findChildById(div, "paragraph2");
        assertNotNull("P2 element should not be null", p2);
        assertEquals("p", p2.getTagName());
        assertEquals("This is some content inside a div.", ((TextElement) p2.getChildren().get(0)).getText());
    }

    private TagElement findChildById(TagElement parent, String id) {
        for (HtmlElement child : parent.getChildren()) {
            if (child instanceof TagElement tagElement && id.equals(tagElement.getId())) {
                return tagElement;
            }
        }
        return null;
    }
}