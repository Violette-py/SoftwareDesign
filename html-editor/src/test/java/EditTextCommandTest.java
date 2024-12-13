import com.violette.command.Command;
import com.violette.command.impl.EditTextCommand;
import com.violette.document.HtmlDocument;
import com.violette.document.TagElement;
import com.violette.document.TextElement;
import com.violette.exception.NotExistsException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Violette
 * @date 2024/10/31 16:09
 */
public class EditTextCommandTest {
    private HtmlDocument document;

    @Before
    public void setUp() {
        document = new HtmlDocument();
        document.init();
    }

    /*
     * 原来元素内没有文本，现在新增
     * */
    @Test
    public void testEditTextCommandAddText() {
        try {
            TagElement div = new TagElement("div", "editableDiv");
            // 添加两个子元素（TagElement）
            div.addChild(new TagElement("span", "span1"));
            div.addChild(new TagElement("span", "span2"));
            document.getBody().addChild(div);

            System.out.println("origin:");
            document.printTree();

            // 尝试添加文本
            Command editTextCommand = new EditTextCommand(document, "editableDiv", "New Text");
            editTextCommand.execute();

            System.out.println("after add text:");
            document.printTree();

            // 验证文本是否成功添加成为第一个孩子
            assertEquals("New Text", ((TextElement) div.getChildren().get(0)).getText());
        } catch (Exception e) {
            fail("EditTextCommand construction or execution failed with exception: " + e.getMessage());
        }
    }

    /*
     * 原来元素内有文本，现在修改
     * */
    @Test
    public void testEditTextCommandModifyText() {
        try {
            TagElement div = new TagElement("div", "editableDiv");
            // 添加初始文本
            TextElement oldText = new TextElement("Old Text");
            div.addChild(oldText);
            // 添加两个子元素（TagElement）
            div.addChild(new TagElement("span", "span1"));
            div.addChild(new TagElement("span", "span2"));
            document.getBody().addChild(div);

            System.out.println("origin:");
            document.printTree();

            // 尝试修改文本
            Command editTextCommand = new EditTextCommand(document, "editableDiv", "New Text");
            editTextCommand.execute();

            System.out.println("after modify text:");
            document.printTree();

            // 验证文本是否修改成功
            assertEquals("New Text", ((TextElement) div.getChildren().get(0)).getText());
        } catch (Exception e) {
            fail("EditTextCommand construction or execution failed with exception: " + e.getMessage());
        }
    }

    /*
     * 原来元素内有文本，现在清空
     * */
    @Test
    public void testEditTextCommandEmptyText() {
        try {
            TagElement div = new TagElement("div", "editableDiv");
            // 添加初始文本
            TextElement oldText = new TextElement("Old Text");
            div.addChild(oldText);
            // 添加两个子元素（TagElement）
            div.addChild(new TagElement("span", "span1"));
            div.addChild(new TagElement("span", "span2"));
            document.getBody().addChild(div);

            System.out.println("origin:");
            document.printTree();

            // 尝试清空文本
            Command editTextCommand = new EditTextCommand(document, "editableDiv", "");
            editTextCommand.execute();

            System.out.println("after clear text:");
            document.printTree();

            // 验证文本是否被清空
            assertEquals(2, div.getChildren().size()); // 两个子元素（TagElement）应该还在
        } catch (Exception e) {
            fail("EditTextCommand construction or execution failed with exception: " + e.getMessage());
        }
    }

    /*
     * 更改文本后执行撤销操作
     * */
    @Test
    public void testEditTextCommandUndo() {
        try {
            TagElement div = new TagElement("div", "editableDiv");
            // 添加初始文本
            TextElement oldText = new TextElement("Old Text");
            div.addChild(oldText);
            // 添加两个子元素（TagElement）
            div.addChild(new TagElement("span", "span1"));
            div.addChild(new TagElement("span", "span2"));
            document.getBody().addChild(div);

            // 修改文本
            Command editTextCommand = new EditTextCommand(document, "editableDiv", "New Text");
            editTextCommand.execute();
            assertEquals("New Text", ((TextElement) div.getChildren().get(0)).getText());

            // 执行撤销
            editTextCommand.undo();
            assertEquals("Old Text", ((TextElement) div.getChildren().get(0)).getText());
        } catch (Exception e) {
            fail("EditTextCommand construction or execution failed with exception: " + e.getMessage());
        }
    }

    /*
     * 撤销文本更改后执行重做操作
     * */
    @Test
    public void testEditTextCommandRedo() {
        try {
            TagElement div = new TagElement("div", "editableDiv");
            // 添加初始文本
            TextElement oldText = new TextElement("Old Text");
            div.addChild(oldText);
            // 添加两个子元素（TagElement）
            div.addChild(new TagElement("span", "span1"));
            div.addChild(new TagElement("span", "span2"));
            document.getBody().addChild(div);

            // 执行操作并撤销
            Command editTextCommand = new EditTextCommand(document, "editableDiv", "New Text");
            editTextCommand.execute();
            editTextCommand.undo();
            assertEquals("Old Text", ((TextElement) div.getChildren().get(0)).getText());

            // 重做操作
            editTextCommand.redo();
            assertEquals("New Text", ((TextElement) div.getChildren().get(0)).getText());
        } catch (Exception e) {
            fail("EditTextCommand construction or execution failed with exception: " + e.getMessage());
        }
    }

    /*
     * 找不到指定元素
     * */
    @Test
    public void testEditTextCommandNonExistentId() {
        assertThrows(NotExistsException.class, () -> {
            new EditTextCommand(document, "nonExistentId", "New Text");
        });
    }
}