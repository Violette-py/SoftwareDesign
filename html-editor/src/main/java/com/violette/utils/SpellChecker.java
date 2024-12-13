package com.violette.utils;

import com.violette.document.HtmlElement;
import com.violette.document.TagElement;
import com.violette.document.TextElement;
import org.languagetool.JLanguageTool;
import org.languagetool.Languages;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.List;

/**
 * @author Violette
 * @date 2024/12/13 21:48
 */
public class SpellChecker {
    public static JLanguageTool langTool;
    private static boolean isPrintEnabled = true; // 默认启用日志输出

    static {
        // 静态代码块确保langTool只被初始化一次
        langTool = new JLanguageTool(Languages.getLanguageForShortCode("en"));
    }

    public static JLanguageTool getLangTool() {
        // 便于在测试代码中模拟外部依赖
        return langTool;
    }

    public static void setPrint(boolean enable) {
        isPrintEnabled = enable; // 控制日志输出
    }

    /**
     * 遍历HTML文档并检查所有文本元素的拼写。
     *
     * @param element 当前遍历到的HTML元素。
     */
    public static void traverseAndCheckText(HtmlElement element) {
        if (element instanceof TextElement textElement) {
            // 检查TextElement的文本内容
            if (isPrintEnabled) {
                System.out.println("now checking: " + textElement.getText());
            }
            if (checkText(textElement.getText())) {
                // 存在拼写错误
                textElement.hasSpellingMistake();
            }
        } else if (element instanceof TagElement tagElement) {
            // 递归遍历TagElement的子元素
            for (HtmlElement child : (tagElement.getChildren())) {
                traverseAndCheckText(child);
            }
        }
    }

    /**
     * 使用LanguageTool检查文本内容的拼写。
     *
     * @param text 要检查的文本。
     * @return 该段文本是否存在拼写错误
     */
    private static boolean checkText(String text) {
        List<RuleMatch> matches = null;
        try {
            matches = getLangTool().check(text);  // 便于在测试代码中模拟外部依赖
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean hasError = false;
        // 遍历可能的错误项
        for (RuleMatch match : matches) {
            if (isPrintEnabled) {
                // 输出拼写检查结果
                System.out.println("Potential error at characters " +
                        match.getFromPos() + "-" + match.getToPos() + ": " +
                        match.getMessage());
                System.out.println("Suggested correction(s): " +
                        match.getSuggestedReplacements());
            }
            hasError = true;
        }
        return hasError;
    }
}
