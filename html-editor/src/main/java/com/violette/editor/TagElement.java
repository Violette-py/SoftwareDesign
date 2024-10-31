package com.violette.editor;

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

    public void removeChild(HtmlElement element) {
        this.children.remove(element);
    }

    @Override
    public void printIndent(int indent) {
        System.out.println(" ".repeat(indent) + "<" + this.tagName + (this.id != null ? " id=\"" + id + "\"" : "") + ">");
        for (HtmlElement child : this.children) {
            child.printIndent(indent + 2);
        }
        System.out.println(" ".repeat(indent) + "</" + this.tagName + ">");
    }

    @Override
    public void printTree(TagElement parentElement, String prefix) { // 父元素一定是TagElement类型
        boolean isLast = isLastChild(parentElement);
        String currContent = prefix;
        currContent += isLast ? "└── " : "├── ";
        currContent += tagName;
        currContent += this.defaultElement.contains(tagName) ? "" : "#" + id;
        System.out.println(currContent);
//        System.out.println(prefix + connector + tagName + "#" + id);

        for (HtmlElement child : children) {
            String childPrefix = isLast ? prefix + "    " : prefix + "│   ";
            child.printTree(this, childPrefix);
        }
    }
}
