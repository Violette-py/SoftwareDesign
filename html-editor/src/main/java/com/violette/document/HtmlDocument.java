package com.violette.document;

import com.violette.utils.SpellChecker;
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
    private Boolean showId; // print-tree时是否显示id

    public HtmlDocument() {
        super("html");
        this.showId = true;  // 默认显示 id
    }

    public void init() {
        this.clearChildren();
        this.head = new TagElement("head");
        this.head.addChild(new TagElement("title"));
        this.body = new TagElement("body");
        this.addChild(head);
        this.addChild(body);
    }

    @Override
    public void printIndent(int indent, int prefix) {
        System.out.println("<" + this.getTagName() + ">");
        for (HtmlElement child : this.getChildren()) {
            child.printIndent(indent, prefix);
        }
        System.out.println("</" + this.getTagName() + ">");
    }

    @Override
    public void printTree(TagElement parentElement, String prefix, Boolean show) {
        // 更新拼写检查，确保数据一致性
        SpellChecker.setPrint(false); // 不输出拼写检查日志
        SpellChecker.traverseAndCheckText(this);
        SpellChecker.setPrint(true); // 恢复输出

        System.out.println(this.getTagName());
        for (int i = 0; i < this.getChildren().size(); i++) {
            this.getChildren().get(i).printTree(this, "", this.showId);
        }
    }

    public void printTree() {
        System.out.println(this.getTagName());
        for (int i = 0; i < this.getChildren().size(); i++) {
            this.getChildren().get(i).printTree(this, "", this.showId);
        }
    }

//    public void printTree() {
//        printTree(this, "");
//    }
}
