package editor;

import command.Command;
import command.impl.InsertCommand;
import invoker.CommandExecutor;
import receiver.Document;

import java.util.Scanner;

public class MarkdownEditor {

//    private TitleElement root;

//    public MarkdownEditor() {
//        this.root = new TitleElement();  // 根节点为空
//    }

    public void start() {
        CommandExecutor commandExecutor = new CommandExecutor();
        Scanner scanner = new Scanner(System.in);

        System.out.println("This is a Markdown editor, write whatever you want here.");

        // 初始化文档树的根节点：lineNumber=0, content=""
        Document.setRoot(new TitleElement());

        while (true) {
            // 读取用户输入
            String inputCommand = scanner.nextLine().trim();
            // 解析命令
            Command parsedCommand = parseCommand(inputCommand);
            // 委托给 executor执行 -- 为了实现 undo和 redo功能
            commandExecutor.execute(parsedCommand);
        }
    }

    private Command parseCommand(String command) {

        if (command == null || command.isEmpty()) {
            throw new IllegalArgumentException("Command cannot be empty");
        }

        String[] parts = command.split(" ");
        String action = parts[0].toLowerCase();
        String content = command.substring(parts[0].length()).trim();

        Command parsedCommand = null;

        switch (action) {

            /* 文件操作 */

            case "load":
                if (parts.length > 1) {
                    String filePath = parts[1];
                    // loadFile(filePath);
                } else {
                    System.out.println("Usage: load 文件路径");
                }
                break;

            case "save":
                // saveFile();
                break;

            /* 编辑功能 */

            case "insert":

                if (parts.length < 2) {
                    throw new IllegalArgumentException("Unknown Command");
                }

                MarkdownElement root = Document.getRoot();
                int lineNumber;

                // 解析可能存在的行号参数
                if (parts.length >= 3 && parts[1].matches("-?\\d+")) {

                    lineNumber = Integer.parseInt(parts[1]);
                    if (lineNumber < 0 || lineNumber > root.getSize() + 1) { // 不能出现跨行插入（即不能出现空行）
                        throw new IllegalArgumentException("Illegal Line Number");
                    }

                    // 取出第二个空格后的内容，作为写入文本
                    content = command.trim().substring(command.indexOf(" ", command.indexOf(" ") + 1)).trim();
                } else {
                    lineNumber = root.getSize() + 1; // 没有输入行号，则在末尾插入
                }

                parsedCommand = new InsertCommand(lineNumber, content); // 在底层判断是标题还是普通文本

                break;

            case "append-head":
                // 执行在文件头部插入操作
                // ...
                break;

            case "append-tail":
                // 执行在文件尾部插入操作
                // ...
                break;

            case "delete":
                if (parts.length >= 2) {
                    // 执行删除标题/文本或行号的操作
                    // ...
                } else {
                    System.out.println("Usage: delete 标题/文本 或 delete 行号");
                }
                break;

            case "undo":
                // 执行撤销上一次编辑命令的操作
                // ...
                break;

            case "redo":
                // 执行重新执行上一次撤销的命令的操作
                // ...
                break;

            /* 显示功能 */

            case "list":
                // 执行以文本形式显示内容的操作
                // ...
                break;

            case "list-tree":
                // 执行以树形结构显示内容的操作
                // ...
                break;

            case "dir-tree":
                if (parts.length > 1) {
                    String directory = parts[1];
                    // 执行以树形结构显示指定目录下的内容的操作
                    // ...
                } else {
                    // 默认显示当前工作区下的内容
                    // ...
                }
                break;

            /* 日志模块 */


            /* 统计模块 */


            default:
                System.out.println("Unknown command: " + command);
        }

        return parsedCommand;
    }

}