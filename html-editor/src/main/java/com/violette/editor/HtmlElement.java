package com.violette.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @description 组合模式的Component，定义了组合中所有对象的通用接口，它声明了用于访问和管理子组件的方法，包括添加、删除、获取子组件等
 */
public abstract class HtmlElement {
    String[] defaultElementName = {"html", "head", "title", "body"};
    public List<String> defaultElement = new ArrayList<>(Arrays.asList(defaultElementName));

    abstract void printIndent(int indent);

    abstract void printTree(TagElement parentElement, String prefix);

    public boolean isLastChild(TagElement parentElement) {
        if (parentElement == null) {
            return false; // 如果没有父元素，肯定不是最后一个孩子
        }
        List<HtmlElement> siblings = parentElement.getChildren();
        return siblings.indexOf(this) == siblings.size() - 1;
    }
}
