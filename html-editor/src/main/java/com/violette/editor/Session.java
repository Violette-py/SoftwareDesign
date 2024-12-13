package com.violette.editor;

import com.violette.command.Command;
import com.violette.command.impl.InitCommand;
import com.violette.command.impl.ReadCommand;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Violette
 * @date 2024/12/13 10:10
 */
@Data
public class Session {
    // filepath -> HtmlEditor(每个Editor中有独立的undo、redo栈)
    private Map<String, HtmlEditor> editorMap;
    // 每个 session 有一个活动的 editor
    private HtmlEditor activeEditor;
    // 每个 session 有一个命令行客户端
    private  ConsoleClient consoleClient;

    public Session() {
        this.editorMap = new HashMap<>();
        this.activeEditor = null;
        this.consoleClient = new ConsoleClient();
    }

    public void start() throws Exception {
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
            Command parsedCommand = this.consoleClient.parseCommand(activeEditor, line);
            // TODO: 第一条命令应为 load
            if (isFirstCommand && !(parsedCommand instanceof ReadCommand) && !(parsedCommand instanceof InitCommand)) {
                throw new RuntimeException("First Command must be [read] or [init]");
            }
            isFirstCommand = false;

            // 委托给 executor执行 -- 为了实现 undo和 redo功能
            if (parsedCommand != null) {
                activeEditor.getCommandExecutor().executeCommand(parsedCommand);
            }
        }

        scanner.close();
    }
}
