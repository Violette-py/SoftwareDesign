package com.violette.file;

import lombok.Data;

import java.io.File;
import java.nio.file.Path;
import java.util.Set;

/**
 * @author Violette
 * @date 2024/12/14 2:05
 */
@Data
public class ProjectDirectory extends DirectoryNode {
    private Set<String> unSavedFilePaths; // 未保存文件的绝对路径

    public ProjectDirectory(Set<String> unSavedFilePaths) {
        super("html-editor");
        this.unSavedFilePaths = unSavedFilePaths;
    }

    /*
     * 构建文件系统树模型
     * */
    public void buildTree(File dir, DirectoryNode node) {
        File[] files = dir.listFiles(); // 当前目录下的子文件(夹)
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                Path filePath = file.toPath(); // 当前文件的绝对路径
//                Path relativePath = this.getRootPath().relativize(filePath); // 当前文件相对于根目录的相对路径
                if (file.isDirectory()) {
                    DirectoryNode subDir = new DirectoryNode(name);
                    node.addChild(subDir);
                    buildTree(file, subDir);
                } else {
                    // TODO: 这里的name是纯文件名，而传参进来的是相对于根目录的文件路径
                    FileNode fileNode = new FileNode(name, this.unSavedFilePaths.contains(filePath.toString()));
//                    FileNode fileNode = new FileNode(name, this.unSavedFilePaths.contains(relativePath.toString()));
                    node.addChild(fileNode);
                }
            }
        }
    }

    @Override
    public void printIndent(int indent, int prefix) {
        System.out.println(this.getName());
        for (FileComponent child : this.getChildren()) {
            child.printIndent(indent, prefix);
        }
    }

    @Override
    public void printTree(DirectoryNode parentElement, String prefix) {
        System.out.println(this.getName());
        for (int i = 0; i < this.getChildren().size(); i++) {
            this.getChildren().get(i).printTree(this, "");
        }
    }

}
