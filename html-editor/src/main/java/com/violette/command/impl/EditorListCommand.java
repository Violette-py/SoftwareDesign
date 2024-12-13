package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.Session;
import lombok.SneakyThrows;

/**
 * @author Violette
 * @date 2024/12/13 15:29
 */
public class EditorListCommand extends Command {
    private Session session;

    /**
     * EditorListCommand 的构造函数。
     *
     * @param session 当前会话
     */
    public EditorListCommand(Session session){
        super(CommandType.DISPLAY);

        this.session = session;
    }

    @SneakyThrows
    @Override
    public void execute() {
        this.session.showEditorList();
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
