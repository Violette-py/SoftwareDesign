package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.HtmlDocument;
import com.violette.editor.HtmlElement;
import com.violette.editor.TagElement;
import com.violette.editor.TextElement;
import org.languagetool.JLanguageTool;
import org.languagetool.Languages;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.List;

/**
 * @author Violette
 * @date 2024/10/31 19:04
 */
public class SpellCheckCommand implements Command {
    private HtmlDocument document;
    private JLanguageTool langTool;

    /**
     * SpellCheckCommand 的构造函数。
     *
     * @param document HTML文档。
     */
    public SpellCheckCommand(HtmlDocument document) {
        this.document = document;
        // 使用LanguageTool支持的语言代码初始化LanguageTool对象，例如"en-US"表示美式英语
        this.langTool = new JLanguageTool(Languages.getLanguageForShortCode("en"));
    }

    @Override
    public void execute() {
        // 遍历HTML文档中的所有文本元素
        traverseAndCheckText(document);
    }

    // TODO: 检查标记文本，HTML，而不仅仅检查文字；同时希望检查单词拼写

    /**
     * 遍历HTML文档并检查所有文本元素的拼写。
     *
     * @param element 当前遍历到的HTML元素。
     */
    private void traverseAndCheckText(HtmlElement element) {
        if (element instanceof TextElement) {
            // 检查TextElement的文本内容
            System.out.println("now checking:" + ((TextElement) element).getText());
            checkText(((TextElement) element).getText());
        } else if (element instanceof TagElement) {
            // 递归遍历TagElement的子元素
            for (HtmlElement child : ((TagElement) element).getChildren()) {
                traverseAndCheckText(child);
            }
        }
    }

    /**
     * 使用LanguageTool检查文本内容的拼写。
     *
     * @param text 要检查的文本。
     */
    private void checkText(String text) {
        List<RuleMatch> matches = null;
        try {
            matches = langTool.check(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (RuleMatch match : matches) {
            // 输出拼写检查结果
            System.out.println("Potential error at characters " +
                    match.getFromPos() + "-" + match.getToPos() + ": " +
                    match.getMessage());
            System.out.println("Suggested correction(s): " +
                    match.getSuggestedReplacements());
        }
    }

    @Override
    public void undo() {
        // 显示类命令不需要撤销
    }

    @Override
    public void redo() {
        // 显示类命令不需要重做
    }

    @Override
    public boolean isDisplayCommand() {
        return true;
    }

    @Override
    public boolean isIOCommand() {
        return false;
    }
}