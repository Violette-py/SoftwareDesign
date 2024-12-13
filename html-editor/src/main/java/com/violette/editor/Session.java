package com.violette.editor;

import com.violette.exception.NotExistsException;
import com.violette.exception.RepeatedException;
import com.violette.utils.HtmlConverter;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Violette
 * @date 2024/12/13 10:10
 */
@Data
@Slf4j
public class Session {
    private List<HtmlEditor> editorList;
    private HtmlEditor activeEditor;  // 当前编辑的 editor

    public Session() {
        this.editorList = new ArrayList<>();
        this.activeEditor = null;
    }

    /*
    * 将文件载入新的editor
    * */
    @SneakyThrows
    public HtmlEditor addEditor(String filepath) {
        // 文件已经装入过，给出错误提示
        if (editorList.stream().anyMatch(editor -> editor.getFilepath().equals(filepath))) {
            throw new RepeatedException("filepath", filepath, "editor");
        }

        // 新建 editor，并加入 list
        HtmlEditor editor = new HtmlEditor(filepath);
        this.editorList.add(editor);
        // 自动切换为 activeEditor
        this.activeEditor = editor;

        return editor;
    }

    /*
    * 切换当前editor
    * */
    public void switchActiveEditor(String filepath) throws NotExistsException {
        // 文件未装入editor
        if (this.editorList.stream().noneMatch(editor -> editor.getFilepath().equals(filepath))) {
            throw new NotExistsException("filepath", filepath, "editor");
        }
        this.activeEditor = this.editorList.stream()
                .filter(editor -> filepath.equals(editor.getFilepath()))
                .findFirst()
                .orElse(null);
    }

    /*
    * 显示editor列表
    * */
    public void showEditorList() throws NotExistsException {
        // 当前没有活跃editor
        if (this.activeEditor == null) {
            throw new NotExistsException("activeEditor");
        }
        // 找到活跃editor
        int activeIndex = editorList.indexOf(this.activeEditor);
        if (activeIndex < 0) {
            throw new NotExistsException("filepath", activeEditor.getFilepath(), "editor");
        }

        StringBuilder sb = new StringBuilder();
        HtmlEditor editor;
        for (int i = 0; i < this.editorList.size(); i++) {
            editor = this.editorList.get(i);
            sb.append(i == activeIndex ? "> " : "  ");
            sb.append(editor.getFilepath());
            sb.append(editor.getIsSaved() ? "" : " *");
            sb.append("\n");
        }
        System.out.println(sb);
    }

    /*
    * 保存activeEditor的内容，写入文件
    * */
    public void save() {
        // 写入文件
        // 创建Jsoup文档对象
        Document jsoupDoc = new Document("");
        Node rootElement = HtmlConverter.convertCustomModelToJsoupModel(activeEditor.getDocument());
        jsoupDoc.appendChild(rootElement); // 将根元素添加到文档中
        // 将Jsoup文档对象保存为HTML文件
        String filePath = activeEditor.getFilepath();
        try (FileWriter writer = new FileWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(jsoupDoc.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save HTML document to file: " + filePath, e);
        }
        // 设置保存状态
        activeEditor.setIsSaved(true);
    }

    /*
    * 设置当前editor被修改过
    * */
    public void setCurrEditorUnsaved() {
        this.activeEditor.setIsSaved(false);
    }

}
