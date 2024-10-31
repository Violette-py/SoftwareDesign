package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.HtmlDocument;
import com.violette.editor.TagElement;
import com.violette.editor.TextElement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.TextNode;

import java.io.File;
import java.io.IOException;


/**
 * @author Violette
 * @date 2024/10/31 20:35
 * @description 从文件中读取HTML内容, 并将其转换为自己设计的HTML面向对象模型
 */
public class ReadCommand implements Command {
    private HtmlDocument document;
    private String filepath;

    /**
     * ReadCommand 的构造函数。
     *
     * @param document HTML文档模型。
     */
    public ReadCommand(HtmlDocument document, String filepath) {
        this.document = document;
        this.filepath = filepath;
    }

    @Override
    public void execute() {
        File file = new File(filepath);
        try {
            // 使用Jsoup读取和解析HTML文件
            Document jsoupDoc = Jsoup.parse(file, "UTF-8");
            convertToModel(jsoupDoc.child(0), this.document); // 从html元素开始
        } catch (IOException e) {
            throw new RuntimeException("Failed to read HTML file: " + filepath, e);
        }
    }

    /**
     * 递归将Jsoup的Document对象转换为自定义的HTML模型。
     *
     * @param jsoupElement Jsoup的Element对象。
     * @param tagElement   自定义的TagElement对象。
     */
    private void convertToModel(Element jsoupElement, TagElement tagElement) {
        String tagName = jsoupElement.tagName();
        tagElement.setTagName(tagName);

        // 处理元素id
        for (Attribute attribute : jsoupElement.attributes()) {
            if ("id".equals(attribute.getKey())) {
                tagElement.setId(attribute.getValue());
            }
        }

        // 处理子元素和文本节点
        for (Node node : jsoupElement.childNodes()) { // 不要用.children()
            if (node instanceof Element childJsoupElement) {
                // 默认id为标签名
                TagElement childTagElement = new TagElement(childJsoupElement.tagName(), childJsoupElement.hasAttr("id") ? childJsoupElement.attr("id") : childJsoupElement.tagName());
                convertToModel(childJsoupElement, childTagElement);
                tagElement.addChild(childTagElement);
            } else if (node instanceof TextNode textNode) {
                String text = textNode.text();
                if (!text.trim().isEmpty()) {
                    tagElement.addChild(new TextElement(text));
                }
            }
        }
    }

    @Override
    public void undo() {
        // 执行输入/输出指令后，不允许撤销与重做
    }

    @Override
    public void redo() {
        // 执行输入/输出指令后，不允许撤销与重做
    }

    @Override
    public boolean isDisplayCommand() {
        return false;
    }

    @Override
    public boolean isIOCommand() {
        return true;
    }
}