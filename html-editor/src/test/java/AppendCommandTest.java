import com.violette.command.Command;
import com.violette.command.impl.AppendCommand;
import com.violette.editor.HtmlDocument;
import com.violette.editor.TagElement;
import com.violette.editor.TextElement;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Violette
 * @date 2024/10/31 14:24
 */

public class AppendCommandTest {
    private HtmlDocument document;

    @Before
    public void setUp() {
        this.document = new HtmlDocument();
        this.document.init();
    }

    /*
     * 在body元素内追加新元素，文本元素非空
     * */
    @Test
    public void testAppendElementWithTextContent() {
        try {
            Command appendCommand = new AppendCommand(document, "p", "newParagraph", "body", "This is a new paragraph.");
            appendCommand.execute();

            // 验证新元素是否被添加到body中
            TagElement body = document.getChildren().stream()
                    .filter(child -> "body".equals(((TagElement) child).getTagName()))
                    .findFirst()
                    .map(TagElement.class::cast)
                    .orElse(null);
            assertNotNull(body, "Body element should not be null after initialization");
            assertEquals(1, body.getChildren().size(), "Body should contain one child after appending a new element.");
            TagElement newParagraph = (TagElement) body.getChildren().get(0);
            assertEquals("p", newParagraph.getTagName(), "The new element's tag name should be 'p'.");
            assertEquals("newParagraph", newParagraph.getId(), "The new element's id should be 'newParagraph'.");
            assertEquals(1, newParagraph.getChildren().size(), "The new paragraph should contain one text child.");
            assertEquals("This is a new paragraph.", ((TextElement) newParagraph.getChildren().get(0)).getText(), "The text content is incorrect.");
        } catch (Exception e) {
            fail("AppendCommand construction or execution failed with exception: " + e.getMessage());
        }
    }

    /*
     * 在body元素内追加新元素，文本内容为空
     * */
    @Test
    public void testAppendElementWithoutTextContent() {
        try {
            Command appendCommand = new AppendCommand(document, "p", "newParagraph", "body", "");
            appendCommand.execute();

            // 验证新元素是否被添加到body中
            TagElement body = document.getChildren().stream()
                    .filter(child -> "body".equals(((TagElement) child).getTagName()))
                    .findFirst()
                    .map(TagElement.class::cast)
                    .orElse(null);
            assertNotNull(body, "Body element should not be null after initialization");
            assertEquals(1, body.getChildren().size(), "Body should contain one child after appending a new element.");
            TagElement newParagraph = (TagElement) body.getChildren().get(0);
            assertEquals("p", newParagraph.getTagName(), "The new element's tag name should be 'p'.");
            assertEquals("newParagraph", newParagraph.getId(), "The new element's id should be 'newParagraph'.");
            assertEquals(0, newParagraph.getChildren().size(), "The new paragraph shouldn't contain text children.");
//            assertEquals("", ((TextElement) newParagraph.getChildren().get(0)).getText(), "The text content should be empty.");
        } catch (Exception e) {
            fail("AppendCommand construction or execution failed with exception: " + e.getMessage());
        }
    }

    /*
     * 在不存在的父元素内追加新元素
     * */
    @Test
    public void testAppendElementToNonExistentParent() {
        assertThrows(Exception.class, () -> {
            Command appendCommand = new AppendCommand(document, "p", "newParagraph", "nonExistent", "This is a new paragraph.");
        });
    }

    /*
     * 追加具有重复ID的新元素
     * */
    @Test
    public void testAppendElementWithDuplicateId() {
        assertThrows(Exception.class, () -> {
            Command appendCommand = new AppendCommand(document, "p", "body", "body", "This is a new paragraph.");
        });
    }

//    @Test
//    public void testUndoAndRedoAppendCommand() {
//        try {
//            // 测试撤销和重做追加操作
//            Command appendCommand = new AppendCommand(document, "p", "newParagraph", "body", "This is a new paragraph.");
//            appendCommand.execute();
//            assertEquals(1, document.getChildren().size(), "Body should contain one child after appending a new element.");
//
//            appendCommand.undo();
//            assertEquals(0, document.getChildren().size(), "Body should be empty after undoing the append operation.");
//
//            appendCommand.redo();
//            assertEquals(1, document.getChildren().size(), "Body should contain one child after redoing the append operation.");
//        } catch (Exception e) {
//            fail("AppendCommand construction or execution failed with exception: " + e.getMessage());
//        }
//    }
}