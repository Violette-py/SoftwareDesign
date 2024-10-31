package com.violette.command;

public interface Command {
    void execute();
    void undo();
    void redo();
    boolean isDisplayCommand(); // 添加这个方法
    boolean isIOCommand(); // 添加这个方法
}
