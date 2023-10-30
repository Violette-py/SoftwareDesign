package command.impl;

import command.Command;
import editor.MarkdownElement;
import editor.TextElement;
import editor.TitleElement;
import receiver.Document;

public class InsertCommand implements Command {
//    private TitleElement root;
    private int lineNumber;
    private String content;

    public InsertCommand(int lineNumber, String content) {
//        this.root = root;
        this.lineNumber = lineNumber;
        this.content = content;
    }

    @Override
    public void execute() {
//        MarkdownElement markdownElement;
//        if (isTitleElement(this.content)) {
//            markdownElement = new TitleElement(lineNumber, content);
//        } else {
//            markdownElement = new TextElement(lineNumber, content);
//        }
//        MarkdownElement.insert(markdownElement);
        Document.insert(lineNumber, content);
    }

    @Override
    public void undo() {

    }
}
