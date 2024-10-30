package com.violette.command;

public interface Command {
    void execute();
    void undo();
    void redo();
}
