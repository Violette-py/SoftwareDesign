package receiver;

import editor.MarkdownElement;
import editor.TextElement;
import editor.TitleElement;

import java.util.List;

public class Document {
    private static TitleElement root;

    public static void setRoot(TitleElement root) {
        root = root;
    }

    public static TitleElement getRoot() {
        return root;
    }

    public static void insert(int lineNumber, String content) {
        // 判断是标题还是普通文本，二者插入逻辑不同
        if (isTitleElement(content)) {
//            TitleElement.insertTitle(new TitleElement(lineNumber, content));
            insertTitle(new TitleElement(lineNumber, content));
        } else {
//            TextElement.insertText(new TextElement(lineNumber, content));
            insertText(new TextElement(lineNumber, content));
        }
    }


    //    问题：无法访问到对象内部的 children等私有属性 -- 还是要在对象类中完成这些操作 -- 或者通过 getter和 setter解决
    private static void insertTitle(TitleElement titleElement) {
    }

    private static void insertText(TextElement textElement) {

        List<MarkdownElement> children = root.getChildren();

        if (children.isEmpty()) {
            children.add(textElement);
            root.setChildren(children);
        } else {

        }


    }

    private static boolean isTitleElement(String content) {
        // Markdown语法：匹配 1~6个#号 + 1个空格 作为标题
        String pattern = "^#{1,6}\\s.+";
        return content.matches(pattern);
    }

}