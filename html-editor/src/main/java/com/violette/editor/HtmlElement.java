package com.violette.editor;

/**
 * @description 组合模式的Component，定义了组合中所有对象的通用接口，它声明了用于访问和管理子组件的方法，包括添加、删除、获取子组件等
 */
public abstract class HtmlElement {
    abstract void printIndent(int indent);
    abstract void printTree(String prefix);
}
