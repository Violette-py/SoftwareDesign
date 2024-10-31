import com.violette.command.impl.InsertCommand;
import com.violette.editor.HtmlDocument;
import com.violette.editor.HtmlElement;
import com.violette.editor.TagElement;
import com.violette.editor.TextElement;
import com.violette.exception.NotExistsException;
import com.violette.exception.RepeatedException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Violette
 * @date 2024/10/31 00:10
 */
public class InsertCommandTest {
    private HtmlDocument document;

    @Before
    // 不能用BeforeEach，这是junit5的写法
    public void setUp() {
        this.document = new HtmlDocument();
        // 向 body 添加一些元素用于测试
        TagElement div = new TagElement("div", "div1");
        this.document.getBody().addChild(div);
        TagElement p = new TagElement("p", "p1");
        div.addChild(p);
        /*
          html
            ├── head
            │   └── title
            └── body
                └── div#div1
                    └── p#p1
        */
    }

    /*
    * 测试在已有元素之前插入新元素
    * */
    @Test
    public void testInsertBeforeElement() throws NotExistsException, RepeatedException {
        InsertCommand command = new InsertCommand(document, "span", "newSpan", "p1", "");
        command.execute();
        // 验证新元素是否插入到了正确的位置
        HtmlElement insertedElement = ((TagElement) document.getBody().getChildren().get(0)).getChildren().get(0);
        assertTrue(insertedElement instanceof TagElement, "The inserted element should be a TagElement");
        assertEquals("span", ((TagElement) insertedElement).getTagName(), "The tag name of the inserted element should be 'span'");
        assertEquals("newSpan", ((TagElement) insertedElement).getId(), "The id of the inserted element should be 'newSpan'");
    }

    /*
    * 测试插入新元素并包含文本内容
    * */
    @Test
    public void testInsertWithTextContent() throws NotExistsException, RepeatedException {
        String textContent = "New Text Content";
        InsertCommand command = new InsertCommand(document, "span", "newSpan", "p1", textContent);
        command.execute();
        // 验证新元素是否插入到了正确的位置，并包含文本内容
        HtmlElement insertedElement = ((TagElement) document.getBody().getChildren().get(0)).getChildren().get(0);
        assertTrue(insertedElement instanceof TagElement, "The inserted element should be a TagElement");
        assertEquals("span", ((TagElement) insertedElement).getTagName(), "The tag name of the inserted element should be 'span'");
        assertEquals("newSpan", ((TagElement) insertedElement).getId(), "The id of the inserted element should be 'newSpan'");
        command.execute();
        assertEquals(textContent, ((TextElement) ((TagElement) insertedElement).getChildren().get(0)).getText(), "The text content of the inserted element should match");
    }

    /*
    * 测试 ID 唯一性检查
    * */
    @Test
    public void testIdUniqueness() {
        TagElement existingElement = new TagElement("span", "existingSpan");
        document.getBody().addChild(existingElement);
        assertThrows(RepeatedException.class, () -> {
            new InsertCommand(document, "span", "existingSpan", "p1", "");
        });
    }

    /*
    * 测试插入位置元素不存在的情况
    * */
    @Test
    public void testElementNotExists() {
        assertThrows(NotExistsException.class, () -> {
            new InsertCommand(document, "span", "newSpan", "nonExistingId", "");
        });
    }
}
