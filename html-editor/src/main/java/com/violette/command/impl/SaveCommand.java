package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.document.HtmlDocument;
import com.violette.document.HtmlElement;
import com.violette.document.TagElement;
import com.violette.document.TextElement;
import com.violette.utils.HtmlConverter;
import org.jsoup.nodes.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Violette
 * @date 2024/10/31 22:49
 * @description 将构建的HTML文档对象写入HTML文件
 */
public class SaveCommand extends Command {
    private HtmlDocument document;
    private String filepath;

    /**
     * SaveCommand 的构造函数。
     *
     * @param document HTML文档模型。
     * @param filepath 写入文件的路径名。
     */
    public SaveCommand(HtmlDocument document, String filepath) {
        super(CommandType.IO);

        this.document = document;
        this.filepath = filepath;
    }

    @Override
    public void execute() {
        // 创建Jsoup文档对象
        Document jsoupDoc = new Document("");
        Node rootElement = HtmlConverter.convertCustomModelToJsoupModel(document);
        jsoupDoc.appendChild(rootElement); // 将根元素添加到文档中
        // 将Jsoup文档对象保存为HTML文件
        try (FileWriter writer = new FileWriter(this.filepath, StandardCharsets.UTF_8)) {
            writer.write(jsoupDoc.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save HTML document to file: " + filepath, e);
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