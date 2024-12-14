package com.violette.file;

import java.nio.file.Path;

/**
 * @author Violette
 * @date 2024/12/14 1:38
 */
public class FileNode extends FileComponent {
    private Boolean unsaved;

    public FileNode(String name, Boolean unsaved) {
        super(name);
        this.unsaved = unsaved;
    }

    @Override
    public void printIndent(int indent, int prefix) {
        System.out.println(" ".repeat(indent + prefix) + this.getName() + saveMarker());
    }

    @Override
    public void printTree(DirectoryNode parentElement, String prefix) {
        String connector = isLastChild(parentElement) ? "└── " : "├── ";
        System.out.println(prefix + connector + this.getName() + saveMarker());
    }

    private String saveMarker() {
        return this.unsaved ? "*" : "";
    }
}
