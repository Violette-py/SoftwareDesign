import com.violette.command.Command;
import com.violette.command.impl.SpellCheckCommand;
import com.violette.document.HtmlDocument;
import com.violette.document.TextElement;
import org.junit.Before;
import org.junit.Test;
import org.languagetool.JLanguageTool;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

/**
 * @author Violette
 * @date 2024/10/31 19:49
 */
public class SpellCheckCommandTest {
    private HtmlDocument document;
    private JLanguageTool mockLangTool;

    @Before
    public void setUp() {
        document = new HtmlDocument();
        document.init();
        mockLangTool = mock(JLanguageTool.class);
    }

    @Test
    public void testSpellCheckCommand() throws IOException {
        // 设置模拟对象，使其返回空的检查结果
        when(mockLangTool.check(anyString())).thenReturn(Collections.emptyList());
        Command spellCheckCommand = new SpellCheckCommand(document) {
            @Override
            protected JLanguageTool getLangTool() {
                return mockLangTool;
            }
        };

        // 添加文本元素到文档中
        String toCheckText = "A sentence with a spelling error.";
        TextElement textElement = new TextElement(toCheckText);
        document.getBody().addChild(textElement);

        // 执行拼写检查命令
        spellCheckCommand.execute();

        // 验证是否调用了LanguageTool的检查方法
        verify(mockLangTool).check(toCheckText);
    }

    @Test
    public void testSpellCheckCommandWithErrors() throws IOException {
        // 设置模拟对象，使其返回包含错误的检查结果
        RuleMatch ruleMatch = mock(RuleMatch.class);
        when(ruleMatch.getFromPos()).thenReturn(10);
        when(ruleMatch.getToPos()).thenReturn(19);
        when(ruleMatch.getMessage()).thenReturn("Spelling mistake");
        when(mockLangTool.check(anyString())).thenReturn(Collections.emptyList());
        Command spellCheckCommand = new SpellCheckCommand(document) {
            @Override
            protected JLanguageTool getLangTool() {
                return mockLangTool;
            }
        };

        // 添加文本元素到文档中
        String toCheckText = "A sentence with a spelling error.";
        TextElement textElement = new TextElement(toCheckText);
        document.getBody().addChild(textElement);

        // 执行拼写检查命令
        spellCheckCommand.execute();

        // 验证是否调用了LanguageTool的检查方法
        verify(mockLangTool).check(toCheckText);
    }
}
