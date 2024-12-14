package com.violette.file;

import lombok.Data;

import java.util.List;

/**
 * @author Violette
 * @date 2024/12/14 1:44
 */
@Data
public abstract class FileComponent {
    private String name;

    public FileComponent() {

    }

    public FileComponent(String name) {
        this.name = name;
    }

    abstract void printIndent(int indent, int prefix);

    abstract void printTree(DirectoryNode parentElement, String prefix);

    public boolean isLastChild(DirectoryNode parentElement) {
        if (parentElement == null) {
            return false; // 如果没有父元素，肯定不是最后一个孩子
        }
        List<FileComponent> siblings = parentElement.getChildren();
        return siblings.indexOf(this) == siblings.size() - 1;
    }
}
