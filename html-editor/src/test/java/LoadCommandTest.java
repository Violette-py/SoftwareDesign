import com.violette.command.Command;
import com.violette.command.impl.LoadCommand;
import com.violette.document.HtmlDocument;
import com.violette.document.HtmlElement;
import com.violette.document.TagElement;
import com.violette.document.TextElement;
import com.violette.editor.Session;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Violette
 * @date 2024/12/13 14:34
 */
public class LoadCommandTest {
    private Session session;
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        session = new Session();
    }

    /*
     * 装入的文件不存在时，新建文件并提供初始 html 模板 （Lab1的init功能）
     * */
    @Test
    public void testLoadNotExistedHtmlFile() {
        // 创建一个不存在的文件路径，并执行load命令
        File nonExistentFile = new File(testFolder.getRoot(), "non_existent.html");
        LoadCommand loadCommand = new LoadCommand(this.session, nonExistentFile.getAbsolutePath());
        loadCommand.execute();

        HtmlDocument document = this.session.getActiveEditor().getDocument();

        System.out.println("after load:");
        document.printTree();

        // 验证head和title元素是否存在
        List<HtmlElement> children = document.getChildren();
        assertTrue(children.stream().anyMatch(child -> child instanceof TagElement && "head".equals(((TagElement) child).getTagName())));
        assertTrue(children.stream().anyMatch(child -> child instanceof TagElement && "body".equals(((TagElement) child).getTagName())));

        // 验证head元素及其子元素title
        TagElement head = children.stream()
                .filter(child -> child instanceof TagElement && "head".equals(((TagElement) child).getTagName()))
                .findFirst()
                .map(TagElement.class::cast)
                .orElse(null);
        Assertions.assertNotNull(head, "Head element should not be null after initialization");
        Assertions.assertEquals(1, head.getChildren().size(), "Head should contain one child (title)");
        Assertions.assertEquals("title", ((TagElement) head.getChildren().get(0)).getTagName(), "Head's child should be a title element");

        // 验证body元素是否存在
        TagElement body = children.stream()
                .filter(child -> child instanceof TagElement && "body".equals(((TagElement) child).getTagName()))
                .findFirst()
                .map(TagElement.class::cast)
                .orElse(null);
        Assertions.assertNotNull(body, "Body element should not be null after initialization");
        Assertions.assertEquals(0, body.getChildren().size(), "Body should be empty after initialization");
    }

    /*
     * 装入文件已存在，直接读入
     * */
    @Test
    public void testLoadExistedHtmlFile() throws IOException {
        // 准备数据
        String testHtmlContent = "<!DOCTYPE html>\n" +
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
        File testHtmlFile = testFolder.newFile("test.html");
        Path path = testHtmlFile.toPath();
        Files.writeString(path, testHtmlContent);

        // 执行命令
        Command loadCommand = new LoadCommand(this.session, testHtmlFile.getAbsolutePath());
        loadCommand.execute();

        HtmlDocument document = this.session.getActiveEditor().getDocument();

        System.out.println("after load:");
        document.printTree();

        // 验证HTML结构是否正确读取
        assertEquals("html", document.getTagName());
        assertEquals(2, document.getChildren().size()); // html should have head and body

        TagElement head = findChildById(document, "head");
        assertNotNull("Head element should not be null", head);
        assertEquals("head", head.getTagName());
        assertEquals(1, head.getChildren().size()); // head should have title

        TagElement title = findChildById(head, "title");
        assertNotNull("Title element should not be null", title);
        assertEquals("title", title.getTagName());
        assertEquals("HTML Document: Test 1", ((TextElement) title.getChildren().get(0)).getText());

        TagElement body = findChildById(document, "body");
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
