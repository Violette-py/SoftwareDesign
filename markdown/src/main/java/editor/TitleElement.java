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
//    public static void insert(MarkdownElement markdownElement) {
//
//    }

    @Override
    public void remove(MarkdownElement markdownElement) {

    }

    @Override
    public MarkdownElement getChild(int i) {
        return null;
    }

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
