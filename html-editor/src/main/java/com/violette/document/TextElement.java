package com.violette.document;

import com.violette.utils.SpellChecker;
import lombok.Data;
import morfologik.speller.Speller;

/**
 * @author Violette
 * @date 2024/10/31 0:19
 * @description 组合模式的leaf，纯文本元素，不包含子元素
 */
@Data
public class TextElement extends HtmlElement {
    private String text;
    private Boolean hasSpellingError;

    public TextElement(String text) {
        this.text = text;
        this.hasSpellingError = false;
    }

    @Override
    public void printIndent(int indent, int prefix) {
        System.out.println(" ".repeat(indent + prefix) + this.text);
    }

    @Override
    public void printTree(TagElement parentElement, String prefix) {
        String connector = isLastChild(parentElement) ? "└── " : "├── ";
        String spellCheckLabel = this.hasSpellingError ? "[X] " : "";
        System.out.println(prefix + connector + spellCheckLabel + text);
    }

    public void hasSpellingMistake() {
        this.hasSpellingError = true;
    }
}
