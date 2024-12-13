package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.HtmlEditor;
import com.violette.exception.NotExistsException;

import java.util.List;

/**
 * @author Violette
 * @date 2024/12/13 15:29
 */
public class EditorListCommand extends Command {
    private List<HtmlEditor> editorList;
    private Integer activeIndex;

    /**
     * EditorListCommand 的构造函数。
     *
     * @param editorList 当前拥有的editor列表
     */
    public EditorListCommand(List<HtmlEditor> editorList, HtmlEditor activeEditor) throws NotExistsException {
        super(CommandType.DISPLAY);

        if (activeEditor == null) {
            throw new NotExistsException("activeEditor");
        }
        int activeIndex = editorList.indexOf(activeEditor);
        if (activeIndex < 0) {
            throw new NotExistsException("filepath", activeEditor.getFilepath(), "editor");
        }

        this.editorList = editorList;
        this.activeIndex = activeIndex;
    }

    @Override
    public void execute() {
        StringBuilder sb = new StringBuilder();
        HtmlEditor editor;
        for (int i = 0; i < editorList.size(); i++) {
            editor = editorList.get(i);
            sb.append(i == this.activeIndex ? "> " : "  ");
            sb.append(editor.getFilepath());
            sb.append(editor.getIsSaved() ? "" : " *");
            sb.append("\n");
        }
        System.out.println(sb);
    }

    @Override
    public void undo() {
        // 显示类命令不需要撤销与重做
    }

    @Override
    public void redo() {
        // 显示类命令不需要撤销与重做
    }
}
