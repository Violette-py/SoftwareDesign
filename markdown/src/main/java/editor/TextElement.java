package editor;

import receiver.Document;

public class TextElement extends MarkdownElement {
    public TextElement(int lineNumber, String content) {
        super(lineNumber, content);
    }

//    @Override
//    public void insert(MarkdownElement markdownElement) {
//        throw new UnsupportedOperationException("Cannot insert to a leaf");
//    }

    public static void insertText(TextElement textElement) {

        TitleElement root = Document.getRoot();

    }

    @Override
    public void remove(MarkdownElement markdownElement) {
        throw new UnsupportedOperationException("Cannot remove from a leaf");
    }

    @Override
    public MarkdownElement getChild(int i) {
        throw new UnsupportedOperationException("Leaf has no child");
    }

    @Override
    public void display() {
        // FIXME：多种打印方式如何实现？
    }

    @Override
    public int getSize() {
        return 1;
    }
}
