package com.violette.editor;

import com.violette.command.Command;
import com.violette.command.CommandExecutor;
import com.violette.command.impl.*;
import com.violette.document.HtmlDocument;
import com.violette.exception.NotExistsException;

/**
 * @author Violette
 * @date 2024/12/13 10:08
 */
public class ConsoleClient {
    public Command parseCommand(HtmlEditor editor, String line) throws Exception {
        HtmlDocument document = editor.getDocument();
        CommandExecutor commandExecutor = editor.getCommandExecutor();

        // 先提取第一个，根据具体命令决定后续分割数量
        String[] parts = line.split(" ", 2);
        if (parts.length == 0) {
            return null; // 或者抛出一个异常
        }

        String commandType = parts[0].toLowerCase();
        Command command = null;
        String[] params;

        switch (commandType) {
            case "insert" -> {
                params = parts[1].split(" ", 4);
                if (params.length == 3) {
                    command = new InsertCommand(document, params[0], params[1], params[2], "");
                } else if (params.length == 4) {
                    command = new InsertCommand(document, params[0], params[1], params[2], params[3]);
                } else {
                    throw new NotExistsException("command", line);
                }
            }
            case "append" -> {
                params = parts[1].split(" ", 4);
                if (params.length == 3) {
                    command = new AppendCommand(document, params[0], params[1], params[2], "");
                } else if (params.length == 4) {
                    command = new AppendCommand(document, params[0], params[1], params[2], params[3]);
                } else {
                    throw new NotExistsException("command", line);
                }
            }
            case "edit-id" -> {
                params = parts[1].split(" ", 2);
                if (parts.length == 2) {
                    command = new EditIdCommand(document, params[0], params[1]);
                } else {
                    throw new NotExistsException("command", line);
                }
            }
            case "edit-text" -> {
                params = parts[1].split(" ", 2);
                if (params.length == 1) {
                    command = new EditTextCommand(document, params[0], "");
                } else if (params.length == 2) {
                    command = new EditTextCommand(document, params[0], params[1]);
                } else {
                    throw new NotExistsException("command", line);
                }
            }
            case "delete" -> {
                if (parts.length == 2) {
                    command = new DeleteCommand(document, parts[1]);
                } else {
                    throw new NotExistsException("command", line);
                }
            }
            case "print-indent" -> {
                if (parts.length == 1) {
                    command = new PrintIndentCommand(document, 2); // 默认缩进2空格
                } else if (parts.length == 2) {
                    command = new PrintIndentCommand(document, Integer.parseInt(parts[1]));
                } else {
                    throw new NotExistsException("command", line);
                }
            }
            case "print-tree" -> {
                if (parts.length == 1) {
                    command = new PrintTreeCommand(document);
                } else {
                    throw new NotExistsException("command", line);
                }
            }
            case "spell-check" -> {
                if (parts.length == 1) {
                    command = new SpellCheckCommand(document);
                } else {
                    throw new NotExistsException("command", line);
                }
            }
            case "undo" -> {
                if (parts.length == 1) {
                    command = new UndoCommand(commandExecutor);
                } else {
                    throw new NotExistsException("command", line);
                }
            }
            case "redo" -> {
                if (parts.length == 1) {
                    command = new RedoCommand(commandExecutor);
                } else {
                    throw new NotExistsException("command", line);
                }
            }
            /*
            case "read" -> {
                if (parts.length == 2) {
                    // 初始化一个新的文档，在CommandExecutor中会清空对前一个文档的操作记录
                    this.document = new HtmlDocument();
                    command = new ReadCommand(document, parts[1]);
                } else {
                    throw new NotExistsException("command", line);
                }
            }
            case "save" -> {
                if (parts.length == 2) {
                    command = new SaveCommand(document, parts[1]);
                } else {
                    throw new NotExistsException("command", line);
                }
            }
            case "init" -> {
                if (parts.length == 1) {
                    command = new InitCommand(document);
                } else {
                    throw new NotExistsException("command", line);
                }
            }
            */
            default -> throw new NotExistsException("command", commandType);
        }
        return command;
    }
}
