package com.violette.file;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Violette
 * @date 2024/12/14 1:38
 */
@Data
public class DirectoryNode extends FileComponent {
    private List<FileComponent> children = new ArrayList<>();

    public DirectoryNode() {

    }

    public DirectoryNode(String name) {
        super(name);
    }

    public void addChild(FileComponent element) {
        this.children.add(element);
    }

    @Override
    public void printIndent(int indent, int prefix) {
        System.out.println(" ".repeat(indent + prefix) + this.getName());
        for (FileComponent child : this.children) {
            child.printIndent(indent, indent + prefix);
        }
    }

    @Override
    public void printTree(DirectoryNode parentElement, String prefix) {
        boolean isLast = isLastChild(parentElement);
        String connector = isLast ? "└── " : "├── ";
        System.out.println(prefix + connector + this.getName());

        for (FileComponent child : children) {
            String childPrefix = isLast ? prefix + "    " : prefix + "│   ";
            child.printTree(this, childPrefix);
        }
    }
}
