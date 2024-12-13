package com.violette.editor;

import com.violette.command.Command;
import com.violette.command.CommandExecutor;
import com.violette.command.impl.*;
import com.violette.document.HtmlDocument;
import com.violette.exception.NotExistsException;

import java.util.Scanner;

/**
 * @author Violette
 * @date 2024/12/13 17:02
 */
public class ConsoleClient {
    private final Session session;

    public ConsoleClient() {
        this.session = new Session();
        session.loadState(); // 恢复上次工作状态
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("This is a HTML editor, code whatever you want here.");
        boolean isFirstCommand = true;

        while (true) {
            System.out.println("Enter command:");
            // 读取用户输入
            String line = scanner.nextLine().trim();
            // 解析命令
            Command parsedCommand = this.parseCommand(line);
            if (isFirstCommand && !(parsedCommand instanceof LoadCommand || parsedCommand instanceof EditorListCommand)) {
                System.out.println("First Command must be [load] or [editor-list]");
                continue;
            }
            isFirstCommand = false;

            // 委托给 executor执行 -- 为了实现 undo和 redo功能
            if (parsedCommand != null) {
                this.session.getActiveEditor().getCommandExecutor().executeCommand(parsedCommand);
            }
        }

//        scanner.close();
    }

    public Command parseCommand(String line) {
        HtmlEditor activeEditor = this.session.getActiveEditor();
        HtmlDocument document = null;
        CommandExecutor commandExecutor = null;
        if (activeEditor != null) {
            document = activeEditor.getDocument();
            commandExecutor = activeEditor.getCommandExecutor();
        }

        // 先提取第一个，根据具体命令决定后续分割数量
        String[] parts = line.split(" ", 2);
        if (parts.length == 0) {
            return null; // 或者抛出一个异常
        }

        String commandType = parts[0].toLowerCase();
        Command command = null;
        String[] params;

        try {
            // 当前activeEditor = null, 且非 load命令或editor-list命令
            if (activeEditor == null && !(commandType.equals("load") || commandType.equals("editor-list"))) {
                throw new NotExistsException("editor");
            }

            switch (commandType) {
                // 编辑类命令
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
                // 显示类命令
                case "editor-list" -> {
                    if (parts.length == 1) {
                        command = new EditorListCommand(this.session);
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
                // 撤销与重做
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
                // IO类命令
                case "load" -> {
                    if (parts.length == 2) {
                        // 需要改变 session 的 activeEditor
                        command = new LoadCommand(this.session, parts[1]);
                    } else {
                        throw new NotExistsException("command", line);
                    }
                }
                case "saveActiveEditor" -> {
                    if (parts.length == 1) {
                        // 保存 activeEditor 中的文件内容
                        command = new SaveCommand(this.session);
                    } else {
                        throw new NotExistsException("command", line);
                    }
                }
                case "edit" -> { // 切换 activeEditor
                    if (parts.length == 2) {
                        command = new EditCommand(this.session, parts[1]);
                    } else {
                        throw new NotExistsException("command", line);
                    }
                }
                case "close" -> {
                    if (parts.length == 1) {
                        command = new CloseCommand(this.session);
                    } else {
                        throw new NotExistsException("command", line);
                    }
                }
                case "exit" -> {
                    if (parts.length == 1) {
                        command = new ExitCommand(this.session);
                    } else {
                        throw new NotExistsException("command", line);
                    }
                }
                default -> throw new NotExistsException("command", commandType);
            }

            // 记录当前 editor 是否被修改过
            if (command.getCommandType().equals(Command.CommandType.EDIT)) {
                this.session.setCurrEditorUnsaved();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return command;
    }
}
