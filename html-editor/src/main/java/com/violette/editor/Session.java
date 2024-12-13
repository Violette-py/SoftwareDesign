package com.violette.editor;

import com.violette.command.Command;
import com.violette.command.CommandExecutor;
import com.violette.command.impl.*;
import com.violette.document.HtmlDocument;
import com.violette.exception.NotExistsException;
import com.violette.exception.RepeatedException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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

    public Session() {
        this.editorList = new ArrayList<>();
        this.activeEditor = null;
    }

    public HtmlEditor addEditor(String filepath) throws RepeatedException {
        // FIXME: 文件已经装入过，给出错误提示
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

    public void switchActiveEditor(String filepath) throws NotExistsException {
        // 文件未装入editor
        if (this.editorList.stream().noneMatch(editor -> editor.getFilepath().equals(filepath))) {
            throw new NotExistsException("filepath", filepath, "editor");
        }
        this.activeEditor = this.editorList.stream()
                .filter(editor -> filepath.equals(editor.getFilepath()))
                .findFirst()
                .orElse(null);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("This is a HTML editor, code whatever you want here.");
        boolean isFirstCommand = true;

        while (true) {
            System.out.println("Enter command:");
            // 读取用户输入
            String line = scanner.nextLine().trim();
            if ("exit".equalsIgnoreCase(line)) {
                break;
            }
            // 解析命令
            Command parsedCommand = this.parseCommand(line);
            if (isFirstCommand && !(parsedCommand instanceof LoadCommand)) {
                throw new RuntimeException("First Command must be [load]");
            }
            isFirstCommand = false;

            // 委托给 executor执行 -- 为了实现 undo和 redo功能
            if (parsedCommand != null) {
                this.activeEditor.getCommandExecutor().executeCommand(parsedCommand);
            }
        }

        scanner.close();
    }

    public Command parseCommand(String line) {
        HtmlDocument document = null;
        CommandExecutor commandExecutor = null;
        if (this.activeEditor != null) {
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
                        command = new EditorListCommand(this.editorList, this.activeEditor);
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
                        // 装入新的 editor
                        String filepath = parts[1];
                        this.addEditor(filepath);
                        command = new LoadCommand(this.activeEditor.getDocument(), filepath);
                    } else {
                        throw new NotExistsException("command", line);
                    }
                }
                case "save" -> {
                    if (parts.length == 1) {
                        // 保存 activeEditor 中的文件内容
                        command = new SaveCommand(document, this.activeEditor.getFilepath());
                    } else {
                        throw new NotExistsException("command", line);
                    }
                }
                case "edit" -> {
                    if (parts.length == 2) {
                        // 切换 activeEditor
                        command = new EditCommand(this, parts[1]);
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

            // 记录当前 editor 是否被修改过
            if (command.getCommandType().equals(Command.CommandType.EDIT)) {
                this.activeEditor.setIsSaved(false);
            }

        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        return command;
    }
}
