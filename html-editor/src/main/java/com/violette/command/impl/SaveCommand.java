package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.HtmlDocument;
import com.violette.editor.HtmlElement;
import com.violette.editor.TagElement;
import com.violette.editor.TextElement;
import org.jsoup.nodes.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Violette
 * @date 2024/10/31 22:49
 * @description 将构建的HTML文档对象写入HTML文件
 */
public class SaveCommand implements Command {
    private HtmlDocument document;
    private String filepath;

    /**
     * SaveCommand 的构造函数。
     *
     * @param document HTML文档模型。
     * @param filepath 写入文件的路径名。
     */
    public SaveCommand(HtmlDocument document, String filepath) {
        this.document = document;
        this.filepath = filepath;
    }

    @Override
    public void execute() {
        // 创建Jsoup文档对象
        Document jsoupDoc = new Document("");
        Node rootElement = createJsoupElement(document);
        jsoupDoc.appendChild(rootElement); // 将根元素添加到文档中
        // 将Jsoup文档对象保存为HTML文件
        try (FileWriter writer = new FileWriter(this.filepath, StandardCharsets.UTF_8)) {
            writer.write(jsoupDoc.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save HTML document to file: " + filepath, e);
        }
    }

    /**
     * 递归将自定义的HTML模型转换为Jsoup的Document对象。
     *
     * @param htmlElement 自定义的HtmlElement对象。
     * @return Jsoup的Document对象。
     */
    private Node createJsoupElement(HtmlElement htmlElement) {
        if (htmlElement instanceof TextElement textElement) {
            return new TextNode(textElement.getText());
        } else if (htmlElement instanceof TagElement tagElement) {
            Element jsoupElement = new Element(tagElement.getTagName()).attr("id", tagElement.getId());
            for (HtmlElement child : tagElement.getChildren()) {
                jsoupElement.appendChild(createJsoupElement(child));
            }
            return jsoupElement;
        }
        return new TextNode("");
    }

    @Override
    public void undo() {
        // 执行输入/输出指令后，不允许撤销与重做
    }

    @Override
    public void redo() {
        // 执行输入/输出指令后，不允许撤销与重做
    }
}