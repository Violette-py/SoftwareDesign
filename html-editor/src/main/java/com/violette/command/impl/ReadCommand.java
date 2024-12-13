package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.document.HtmlDocument;
import com.violette.document.TagElement;
import com.violette.document.TextElement;
import com.violette.utils.HTMLParser;
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
public class ReadCommand extends Command {
    private HtmlDocument document;
    private String filepath;

    /**
     * ReadCommand 的构造函数。
     *
     * @param document HTML文档模型。
     */
    public ReadCommand(HtmlDocument document, String filepath) {
        super(CommandType.IO);

        this.document = document;
        this.filepath = filepath;
    }

    @Override
    public void execute() {
        File file = new File(filepath);
        try {
            // 使用Jsoup读取和解析HTML文件
            Document jsoupDoc = Jsoup.parse(file, "UTF-8");
            HTMLParser.convertToModel(jsoupDoc.child(0), this.document); // 从html元素开始
        } catch (IOException e) {
            throw new RuntimeException("Failed to read HTML file: " + filepath, e);
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
}