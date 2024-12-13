package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.document.HtmlDocument;
import com.violette.utils.HtmlConverter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.File;
import java.io.IOException;

/**
 * @author Violette
 * @date 2024/12/13 10:53
 */
public class LoadCommand extends Command {
    private HtmlDocument document;
    private String filepath;

    /**
     * LoadCommand 的构造函数。
     *
     * @param document HTML文档模型。
     */
    public LoadCommand(HtmlDocument document, String filepath) {
        super(CommandType.IO);

        this.document = document;
        this.filepath = filepath;
    }

    @Override
    public void execute() {
        File file = new File(filepath);
        if (file.exists()) {
            // 文件存在，直接读入
            try {
                // 使用Jsoup读取和解析HTML文件
                Document jsoupDoc = Jsoup.parse(file, "UTF-8");
                HtmlConverter.convertJsoupModelToCustomModel(jsoupDoc.child(0), this.document); // 从html元素开始
            } catch (IOException e) {
                throw new RuntimeException("Failed to read HTML file: " + filepath, e);
            }
        } else {
            // 文件不存在，新建文件并提供初始 html 模板
            // 保存时再新建文件
            // file.createNewFile();
            this.document.init();
        }
    }

    @Override
    public void undo() {
        // IO类命令不能撤销与重做
    }

    @Override
    public void redo() {
        // IO类命令不能撤销与重做
    }
}
