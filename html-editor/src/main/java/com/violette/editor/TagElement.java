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
    private TagElement parentElement;
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
    public void printTree(String prefix) {
        // 打印当前元素的标签和id
        System.out.println(prefix + tagName + (id != null ? "#" + id : ""));
        if (!children.isEmpty()) {
            for (HtmlElement child : children) {
                child.printTree(prefix + "├── ");
            }
        }
    }
}
