import com.violette.command.Command;
import com.violette.command.impl.InitCommand;
import com.violette.document.HtmlDocument;
import com.violette.document.HtmlElement;
import com.violette.document.TagElement;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Violette
 * @date 2024/10/31 13:02
 */
public class InitCommandTest {
    private HtmlDocument document;

    @Before
    public void setUp() {
        document = new HtmlDocument();
    }

    @Test
    public void testInitCommand() {
        // 测试初始化命令是否正确创建了HTML结构
        Command initCommand = new InitCommand(document);
        initCommand.execute();

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
        assertNotNull(head, "Head element should not be null after initialization");
        assertEquals(1, head.getChildren().size(), "Head should contain one child (title)");
        assertEquals("title", ((TagElement) head.getChildren().get(0)).getTagName(), "Head's child should be a title element");

        // 验证body元素是否存在
        TagElement body = children.stream()
                .filter(child -> child instanceof TagElement && "body".equals(((TagElement) child).getTagName()))
                .findFirst()
                .map(TagElement.class::cast)
                .orElse(null);
        assertNotNull(body, "Body element should not be null after initialization");
        assertEquals(0, body.getChildren().size(), "Body should be empty after initialization");
    }
}
