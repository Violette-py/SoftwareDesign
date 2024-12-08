package com.violette.editor;

import lombok.Data;

/**
 * @author Violette
 * @date 2024/10/31 0:19
 * @description 组合模式的leaf，纯文本元素，不包含子元素
 */
@Data
public class TextElement extends HtmlElement {
    private String text;

    public TextElement(String text) {
        this.text = text;
    }


    @Override
    public void printIndent(int indent, int prefix) {
        System.out.println(" ".repeat(indent + prefix) + this.text);
    }

    @Override
    public void printTree(TagElement parentElement, String prefix) {
        String connector = isLastChild(parentElement) ? "└── " : "├── ";
        System.out.println(prefix + connector + text);
    }
}
