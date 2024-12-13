package com.violette.editor;

import com.violette.exception.NotExistsException;
import com.violette.exception.RepeatedException;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
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
    private static final String STATE_FILE_PATH = ".my-html";

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
        this.activeEditor = findEditorByFilepath(filepath);
    }

    /*
     * 显示editor列表
     * */
    public void showEditorList() throws NotExistsException {
        // 当前没有editor
        if (this.editorList.isEmpty() || this.activeEditor == null) {
            throw new NotExistsException("editor");
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
    public void saveActiveEditor() {
        activeEditor.save();
    }

    /*
     * 设置当前editor被修改过
     * */
    public void setCurrEditorUnsaved() {
        this.activeEditor.setIsSaved(false);
    }

    /*
     * 关闭 Active Editor
     * 如果 Active Editor 中的文件修改过，则询问是否需要保存
     * 关闭后 Active Editor 变为打开的编辑器列表中第一个；如果 Session 中没有其他的 Editor，则没有Active Editor。
     * */
    @SneakyThrows
    public void closeCurrEditor() {
        if (activeEditor == null) {
            throw new NotExistsException("activeEditor");
        }
        // 当前 editor 修改过
        if (!activeEditor.getIsSaved()) {
            askIfSaveTargetEditor(activeEditor);
        }
        // 将当前 editor 移除
        editorList.remove(activeEditor);
        // 设置 activeEditor
        if (editorList.isEmpty()) {
            activeEditor = null;
        } else {
            // 打开的编辑器列表中第一个
            activeEditor = editorList.get(0);
        }
    }

    /*
     * 退出程序
     * 退出程序时需要逐一询问用户没有保存的文件是否需要保存，未保存的内容将被丢弃
     * 退出程序不等同于对所有文件执行 close 命令，需要记录当前工作区文件状态：
     * 当下次启动程序时，程序应该自动恢复上次的工作状态
     * */
    public void exit() {
        // 逐一询问用户没有保存的文件是否需要保存
        for (HtmlEditor editor : editorList) {
            if (!editor.getIsSaved()) {
                askIfSaveTargetEditor(editor);
            }
        }
        // 记录工作状态
        saveState();
        // 退出程序
        System.exit(0);
    }

    /*
     * 保存当前会话状态
     * 1. 退出前打开的文件编辑器(Editor)列表
     * 2. 退出前的活动编辑器(Active Editor)
     * 3. 每个文件编辑器的 showid 设置
     * （注：不需要保留 undo/redo 记录）
     * */
    public void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STATE_FILE_PATH))) {
            // 保存所有 Editor 的状态，包括 filepath 和 showId 设置
            List<EditorState> states = new ArrayList<>();
            for (HtmlEditor editor : editorList) {
                states.add(new EditorState(editor));
            }
            oos.writeObject(states);
            // 保存 Active Editor 的 filePath
            oos.writeObject(activeEditor != null ? activeEditor.getFilepath() : null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 恢复上一次会话状态
     * */
    public void loadState() {
        // 状态文件不存在，无需恢复
        if (!(new File(STATE_FILE_PATH).exists())) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STATE_FILE_PATH))) {
            // 读取所有 Editor 的状态
            List<EditorState> states = (List<EditorState>) ois.readObject();
            for (EditorState state : states) {
                HtmlEditor editor = addEditor(state.getFilePath());
                editor.setShowId(state.getShowId());
            }
            // 读取 Active Editor 的 filePath
            String activeFilePath = (String) ois.readObject();
            if (activeFilePath != null) {
                activeEditor = findEditorByFilepath(activeFilePath);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void askIfSaveTargetEditor(HtmlEditor editor) {
        // 询问用户是否需要保存
        System.out.printf("Editor [%s] 中的文件内容暂未保存，是否需要保存？[y/n]%n", editor.getFilepath());
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine().trim().toLowerCase();
        if (line.equals("y") || line.equals("yes")) {
            editor.save();
        }
    }

    private HtmlEditor findEditorByFilepath(String filePath) {
        return editorList.stream()
                .filter(editor -> filePath.equals(editor.getFilepath()))
                .findFirst()
                .orElse(null);
    }

}
