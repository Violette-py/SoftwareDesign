import com.violette.command.Command;
import com.violette.command.impl.EditIdCommand;
import com.violette.editor.HtmlDocument;
import com.violette.editor.TagElement;
import com.violette.exception.NotExistsException;
import com.violette.exception.RepeatedException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Violette
 * @date 2024/10/31 15:24
 */
public class EditIdCommandTest {
    private HtmlDocument document;
    private TagElement elementToEdit;

    @Before
    public void setUp() {
        this.document = new HtmlDocument();
        this.document.init();

        // 向初始HTML文档中插入一个id为"initialId"的元素
        elementToEdit = new TagElement("div", "initialId");
        document.addChild(elementToEdit);
    }

    /*
    * 成功修改id
    * */
    @Test
    public void testEditIdCommandSuccess() {
        try {
            Command editIdCommand = new EditIdCommand(document, "initialId", "newId");
            editIdCommand.execute();

            // 验证ID是否更改成功
            assertEquals("newId", elementToEdit.getId());
        } catch (Exception e) {
            fail("EditIdCommand construction or execution failed with exception: " + e.getMessage());
        }
    }

    /*
    * 要修改的元素不存在
    * */
    @Test
    public void testEditIdCommandNonExistentId() {
        assertThrows(NotExistsException.class, () -> {
            Command editIdCommand = new EditIdCommand(document, "nonExistentId", "newId");
        });
    }

    /*
    * 新id重复
    * */
    @Test
    public void testEditIdCommandDuplicateId() {
        // 尝试将另一个元素的id改成已存在的"initialId"
        TagElement existingElement = new TagElement("div", "anotherId");
        document.addChild(existingElement);

        assertThrows(RepeatedException.class, () -> {
            Command editIdCommand = new EditIdCommand(document, "anotherId", "initialId");
        });
    }

    @Test
    public void testEditIdCommandUndo() {
        try {
            Command editIdCommand = new EditIdCommand(document, "initialId", "newId");
            editIdCommand.execute();
            assertEquals("newId", elementToEdit.getId());

            editIdCommand.undo();
            // 验证撤销操作是否成功
            assertEquals("initialId", elementToEdit.getId());
        } catch (Exception e) {
            fail("EditIdCommand construction or execution failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testEditIdCommandRedo() {
        try {
            Command editIdCommand = new EditIdCommand(document, "initialId", "newId");
            editIdCommand.execute();
            assertEquals("newId", elementToEdit.getId());

            editIdCommand.undo();
            assertEquals("initialId", elementToEdit.getId());

            editIdCommand.redo();
            // 验证重做操作是否成功
            assertEquals("newId", elementToEdit.getId());
        } catch (Exception e) {
            fail("EditIdCommand construction or execution failed with exception: " + e.getMessage());
        }
    }
}
