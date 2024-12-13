package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.HtmlEditor;
import com.violette.editor.Session;
import com.violette.utils.HtmlConverter;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.File;
import java.io.IOException;

/**
 * @author Violette
 * @date 2024/12/13 10:53
 */
public class LoadCommand extends Command {
    private Session session;
    private String filePath;
    private HtmlEditor currEditor;

    /**
     * LoadCommand 的构造函数。
     *
     * @param session  当前会话。
     * @param filePath 加载文件路径。
     */
    public LoadCommand(Session session, String filePath){
        super(CommandType.IO);

        this.session = session;
        this.filePath = filePath;

        // 新建 editor
        this.currEditor = this.session.addEditor(this.filePath);
    }

    @SneakyThrows
    @Override
    public void execute() {
        File file = new File(this.filePath);
        if (file.exists()) {
            // 文件存在，直接读入
            try {
                // 使用Jsoup读取和解析HTML文件
                Document jsoupDoc = Jsoup.parse(file, "UTF-8");
                HtmlConverter.convertJsoupModelToCustomModel(jsoupDoc.child(0), currEditor.getDocument()); // 从html元素开始
            } catch (IOException e) {
                throw new RuntimeException("Failed to read HTML file: " + this.filePath, e);
            }
        } else {
            // 文件不存在，新建文件并提供初始 html 模板
            // 保存时再新建文件
            // file.createNewFile();
            currEditor.getDocument().init();
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
