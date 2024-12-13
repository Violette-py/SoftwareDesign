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
    }

    @SneakyThrows
    @Override
    public void execute() {
        this.session.addEditor(this.filePath);
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
