import com.violette.command.Command;
import com.violette.command.impl.DeleteCommand;
import com.violette.document.HtmlDocument;
import com.violette.document.HtmlElement;
import com.violette.document.TagElement;
import com.violette.exception.NotExistsException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Violette
 * @date 2024/10/31 18:43
 */

public class DeleteCommandTest {
    private HtmlDocument document;

    @Before
    public void setUp() {
        document = new HtmlDocument();
        document.init();
        // 初始化HTML结构，以便测试DeleteCommand
        TagElement body = document.getBody();
        TagElement div1 = new TagElement("div", "div1");
        TagElement div2 = new TagElement("div", "div2");
        body.addChild(div1);
        body.addChild(div2);
    }

    /*
    * 删除存在的元素
    * */
    @Test
    public void testDeleteCommandSuccess() {
        try {
            System.out.println("origin:");
            document.printTree();

            Command deleteCommand = new DeleteCommand(document, "div1");
            deleteCommand.execute();

            System.out.println("after delete:");
            document.printTree();

            // 验证元素是否被删除
            List<HtmlElement> children = document.getBody().getChildren();
            assertFalse(children.stream().anyMatch(child -> child instanceof TagElement && "div1".equals(((TagElement) child).getId())));
        } catch (Exception e) {
            fail("DeleteCommand construction or execution failed with exception: " + e.getMessage());
        }
    }

    /*
    * 删除不存在的元素
    * */
    @Test
    public void testDeleteCommandNonExistentId() {
        assertThrows(NotExistsException.class, () -> {
            new DeleteCommand(document, "nonExistentId");
        });
    }

    @Test
    public void testDeleteCommandUndo() {
        try {
            Command deleteCommand = new DeleteCommand(document, "div1");
            deleteCommand.execute();

            // 验证元素被删除后撤销操作
            deleteCommand.undo();
            List<HtmlElement> children = document.getBody().getChildren();
            assertTrue(children.stream().anyMatch(child -> child instanceof TagElement && "div1".equals(((TagElement) child).getId())));
        } catch (Exception e) {
            fail("DeleteCommand construction or execution failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteCommandRedo() {
        try {
            Command deleteCommand = new DeleteCommand(document, "div1");
            deleteCommand.execute();
            deleteCommand.undo();

            // 验证撤销后重做操作
            deleteCommand.redo();
            List<HtmlElement> children = document.getBody().getChildren();
            assertFalse(children.stream().anyMatch(child -> child instanceof TagElement && "div1".equals(((TagElement) child).getId())));
        } catch (Exception e) {
            fail("DeleteCommand construction or execution failed with exception: " + e.getMessage());
        }
    }
}