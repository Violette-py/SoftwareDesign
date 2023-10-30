package receiver;

import editor.MarkdownElement;
import editor.TextElement;
import editor.TitleElement;

public class Document {
    private static MarkdownElement root;
    public static void setRoot(MarkdownElement root) {
        root = root;
    }
    public static MarkdownElement getRoot() {
        return root;
    }

    public static void insert(int lineNumber, String content) {
        // 判断是标题还是普通文本，二者插入逻辑不同
        if (isTitleElement(content)) {
            insertTitle(new TitleElement(lineNumber, content));
        } else {
            insertText(new TextElement(lineNumber, content));
        }
    }

    private static void insertTitle(TitleElement titleElement) {

    }

    private static void insertText(TextElement textElement) {

    }

    private static boolean isTitleElement(String content) {
        // Markdown语法：匹配 1~6个#号 + 1个空格 作为标题
        String pattern = "^#{1,6}\\s.+";
        return content.matches(pattern);
    }

}