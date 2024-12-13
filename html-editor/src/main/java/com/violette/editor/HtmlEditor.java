package com.violette.editor;

import com.violette.command.CommandExecutor;
import com.violette.document.HtmlDocument;
import com.violette.utils.HtmlConverter;
import lombok.Data;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Violette
 * @date 2024/10/31 0:59
 */
@Data
public class HtmlEditor {
    private String filepath;
    private HtmlDocument document;
    private CommandExecutor commandExecutor;
    //    private boolean showId;
    private Boolean isSaved;

    public HtmlEditor(String filepath) {
        this.filepath = filepath;
        this.document = new HtmlDocument();
        this.commandExecutor = new CommandExecutor();
//        this.showId = true;
        this.isSaved = true;
    }

    public void save() {
        // 写入文件
        // 创建Jsoup文档对象
        Document jsoupDoc = new Document("");
        Node rootElement = HtmlConverter.convertCustomModelToJsoupModel(this.getDocument());
        jsoupDoc.appendChild(rootElement); // 将根元素添加到文档中
        // 将Jsoup文档对象保存为HTML文件
        try (FileWriter writer = new FileWriter(this.getFilepath(), StandardCharsets.UTF_8)) {
            writer.write(jsoupDoc.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save HTML document to file: " + this.getFilepath(), e);
        }
        // 设置保存状态
        this.setIsSaved(true);
    }
}