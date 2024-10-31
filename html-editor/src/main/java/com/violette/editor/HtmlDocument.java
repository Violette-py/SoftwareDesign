package com.violette.editor;

import lombok.Data;

/**
 * @author Violette
 * @date 2024/10/31 1:11
 * @description HTML的文档树，命令模式中的receiver
 */
@Data
public class HtmlDocument extends TagElement {
    private TagElement head;
    private TagElement body;

    public HtmlDocument() {
        super("html");
        this.head = new TagElement("head");
        this.head.addChild(new TagElement("title"));
        this.body = new TagElement("body");
        this.addChild(head);
        this.addChild(body);
    }
}
