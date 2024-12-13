package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.Session;

/**
 * @author Violette
 * @date 2024/10/31 22:49
 * @description 将构建的HTML文档对象写入HTML文件
 */
public class SaveCommand extends Command {
    private Session session;

    /**
     * SaveCommand 的构造函数。
     *
     * @param session 当前会话。
     */
    public SaveCommand(Session session) {
        super(CommandType.IO);

        this.session = session;
    }

    @Override
    public void execute() {
        this.session.save();
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