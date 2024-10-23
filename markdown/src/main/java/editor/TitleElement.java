package editor;

import receiver.Document;

import java.util.ArrayList;
import java.util.List;

public class TitleElement extends MarkdownElement {
    //    private MarkdownElement root;
    private List<MarkdownElement> children;

    public TitleElement() {
        super();
        this.children = new ArrayList<>();
    }

    public TitleElement(int lineNumber, String content) {
        super(lineNumber, content);
        this.children = new ArrayList<>();
    }

//    @Override
//    public void insert(MarkdownElement markdownElement) {
//
//    }

//    @Override
//    public static void insertTitle(TitleElement titleElement) {
//
//    }

    @Override
    public void remove(MarkdownElement markdownElement) {

    }

    public List<MarkdownElement> getChildren() {
        return children;
    }

    public void setChildren(List<MarkdownElement> children) {
        this.children = children;
    }

    @Override
    public MarkdownElement getChild(int i) {
        return null;
    }

    // 是否需要 override，还是单独只让标题有这个函数？
    @Override
    public void display() {

    }

    @Override
    public int getSize() {
        int size = this == Document.getRoot() ? 0 : 1;
        for (MarkdownElement child : children) {
            size += child.getSize(); // 递归计算子树大小
        }
        return size;
    }
}
