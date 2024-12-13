package com.violette.command;

import com.violette.exception.NotExistsException;
import lombok.Data;

/**
 * @author Violette
 * @date 2024/11/2 23:10
 */
@Data
public abstract class Command {
    // 命令类型枚举
    public enum CommandType {
        EDIT,    // 编辑类命令
        DISPLAY, // 显示类命令
        IO,      // 输入输出命令
        UNDO_REDO // 撤销重做命令
    }

    public CommandType commandType; // 命令类型字段

    public Command() {

    }

    /**
     * Command 的构造函数。
     * @param commandType 命令类型。
     */
    public Command(CommandType commandType) {
        this.commandType = commandType;
    }

    /**
     * 执行命令。
     */
    public abstract void execute();

    /**
     * 撤销命令。
     */
    public abstract void undo();

    /**
     * 重做命令。
     */
    public abstract void redo();
}