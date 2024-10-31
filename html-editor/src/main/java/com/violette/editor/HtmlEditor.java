package com.violette.editor;

import com.violette.command.Command;
import com.violette.command.CommandExecutor;
import com.violette.command.impl.*;
import com.violette.exception.NotExistsException;
import com.violette.exception.RepeatedException;

import java.util.Scanner;

/**
 * @author Violette
 * @date 2024/10/31 0:59
 */
public class HtmlEditor {
    private HtmlDocument document;
    private CommandExecutor commandExecutor;

    public HtmlEditor() {
        this.document = new HtmlDocument();
        this.commandExecutor = new CommandExecutor();
    }

    public void start() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("This is a HTML editor, code whatever you want here.");

        while (true) {
            System.out.println("Enter command:");
            // 读取用户输入
            String line = scanner.nextLine().trim();
            if ("exit".equalsIgnoreCase(line)) {
                break;
            }
            // 解析命令
            Command parsedCommand = parseCommand(line);
            // 委托给 executor执行 -- 为了实现 undo和 redo功能
            if (parsedCommand != null) {
                commandExecutor.executeCommand(parsedCommand);
            }
        }

        scanner.close();
    }

    private Command parseCommand(String line) throws Exception {
        // FIXME: 先提取第一个，根据命令决定后续分割数量
        String[] parts = line.split(" ", 2);
        if (parts.length == 0) {
            return null; // 或者抛出一个异常
        }

        String commandType = parts[0].toLowerCase();
        Command command = null;
        String[] params;

        switch (commandType) {
            case "insert":
                params = parts[1].split(" ", 4);
                if (params.length == 3) {
                    command = new InsertCommand(document, params[0], params[1], params[2], "");
                } else if (params.length == 4) {
                    command = new InsertCommand(document, params[0], params[1], params[2], params[3]);
                } else {
                    throw new NotExistsException("command", line);
                }
                break;
            case "append":
                params = parts[1].split(" ", 4);
                if (params.length == 3) {
                    command = new AppendCommand(document, params[0], params[1], params[2], "");
                } else if (params.length == 4) {
                    command = new AppendCommand(document, params[0], params[1], params[2], params[3]);
                } else {
                    throw new NotExistsException("command", line);
                }
                break;
            case "edit-id":
                params = parts[1].split(" ", 2);
                if (parts.length == 2) {
                    command = new EditIdCommand(document, params[0], params[1]);
                } else {
                    throw new NotExistsException("command", line);
                }
                break;
            case "edit-text":
                params = parts[1].split(" ", 2);
                if (params.length == 1) {
                    command = new EditTextCommand(document, params[0], "");
                } else if (params.length == 2) {
                    command = new EditTextCommand(document, params[0], params[1]);
                } else {
                    throw new NotExistsException("command", line);
                }
                break;
//            case "delete":
//                if (parts.length == 2) {
//                    command = new DeleteCommand(document, parts[1]);
//                }
//                break;
            case "print-indent":
                command = new PrintIndentCommand(document, parts.length > 1 ? Integer.parseInt(parts[1]) : 2); // 默认缩进2空格
                break;
            case "print-tree":
                command = new PrintTreeCommand(document);
                break;
//            case "spell-check":
//                command = new SpellCheckCommand(document);
//                break;
//            case "undo":
//                command = new UndoCommand(commandExecutor);
//                break;
//            case "redo":
//                command = new RedoCommand(commandExecutor);
//                break;
//            case "read":
//                if (parts.length == 2) {
//                    command = new ReadCommand(parts[1]);
//                }
//                break;
//            case "save":
//                if (parts.length == 2) {
//                    command = new SaveCommand(parts[1]);
//                }
//                break;
            case "init":
                command = new InitCommand(document);
                break;
            default:
//                System.out.println("Unknown command: " + line);
//                break;
                throw new NotExistsException("command", commandType);
        }

        return command;
    }

}
