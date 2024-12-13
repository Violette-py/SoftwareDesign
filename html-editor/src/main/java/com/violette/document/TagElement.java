package com.violette.document;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Violette
 * @date 2024/10/31 0:19
 * @description 组合模式的Composite对象，复合节点可以包含子节点，可以是叶子节点
 */
@Data
public class TagElement extends HtmlElement {
    private String tagName;
    private String id;
    private List<HtmlElement> children = new ArrayList<>();

    public TagElement() {

    }

    public TagElement(String tagName) {
        this.tagName = tagName;
        this.id = tagName;  // 默认id是标签名
    }

    public TagElement(String tagName, String id) {
        this.tagName = tagName;
        this.id = id;
    }

    public void addChild(HtmlElement element) {
        this.children.add(element);
    }

    public void addChild(int index, HtmlElement element) {
        this.children.add(index, element);
    }

    public void removeChild(HtmlElement element) {
        this.children.remove(element);
    }

    public void clearChildren() {
        this.children.clear();
    }

    @Override
    public void printIndent(int indent, int prefix) {
        System.out.println(" ".repeat(indent + prefix) + "<" + this.tagName + (this.defaultElement.contains(tagName) ? "" : " id=\"" + id + "\"") + ">");
        for (HtmlElement child : this.children) {
            child.printIndent(indent, indent + prefix);
        }
        System.out.println(" ".repeat(indent + prefix) + "</" + this.tagName + ">");
    }

    @Override
    public void printTree(TagElement parentElement, String prefix, Boolean showId) { // 父元素一定是TagElement类型
        boolean isLast = isLastChild(parentElement);
        String currContent = prefix;
        currContent += isLast ? "└── " : "├── ";
        currContent += tagName;
        currContent += showId && !this.defaultElement.contains(tagName) ? "#" + id : "";
        System.out.println(currContent);

        for (HtmlElement child : children) {
            String childPrefix = isLast ? prefix + "    " : prefix + "│   ";
            child.printTree(this, childPrefix, showId);
        }
    }
}
