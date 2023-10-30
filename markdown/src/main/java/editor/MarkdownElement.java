package editor;

public abstract class MarkdownElement {
    //    protected MarkdownElement root;
    protected int lineNumber;
    protected String content;

    public MarkdownElement() {
        this.lineNumber = 0;
        this.content = "";
    }

    public MarkdownElement(int lineNumber, String content) {
        this.lineNumber = lineNumber;
        this.content = content;
    }

    public abstract void remove(MarkdownElement markdownElement);

    public abstract MarkdownElement getChild(int i);

    public abstract void display();

    public abstract int getSize();

}
