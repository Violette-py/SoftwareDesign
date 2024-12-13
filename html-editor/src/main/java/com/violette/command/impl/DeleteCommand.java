package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.document.HtmlDocument;
import com.violette.document.HtmlElement;
import com.violette.document.TagElement;
import com.violette.exception.NotExistsException;
import com.violette.utils.DocumentUtil;
import com.violette.utils.Pair;

import java.util.List;

/**
 * @author Violette
 * @date 2024/10/31 17:10
 */
public class DeleteCommand extends Command {
    private HtmlDocument document;
    private TagElement elementToDelete; // 要删除的元素
    private TagElement parentElement; // 要删除元素的父元素
    private List<HtmlElement> childrenBeforeDeletion; // 删除前父元素的孩子列表

    /**
     * DeleteCommand 的构造函数。
     *
     * @param document  HTML文档。
     * @param elementId 要删除的元素的ID。
     * @throws NotExistsException 如果指定的元素ID不存在。
     */
    public DeleteCommand(HtmlDocument document, String elementId) throws NotExistsException {
        super(CommandType.EDIT);

        this.document = document;

        // 查找指定ID的元素及其父元素
        Pair<TagElement, TagElement> result = DocumentUtil.findElementAndItsParentById(document, elementId);
        this.elementToDelete = result.first();
        this.parentElement = result.second();
        if (this.elementToDelete == null || this.parentElement == null) {
            throw new NotExistsException("id", elementId);
        }

        // 保存删除前的孩子列表
        this.childrenBeforeDeletion = this.parentElement.getChildren();
    }

    @Override
    public void execute() {
        // 从父元素中移除元素
        this.parentElement.removeChild(this.elementToDelete);
    }

    @Override
    public void undo() {
        // 撤销删除操作：将元素重新添加回父元素
        if (this.childrenBeforeDeletion != null && this.elementToDelete != null) {
            int index = this.childrenBeforeDeletion.indexOf(this.elementToDelete);
            if (index == -1) {
                this.parentElement.addChild(this.elementToDelete); // 如果找不到原位置，则添加到末尾
            } else {
                this.parentElement.addChild(index, this.elementToDelete); // 恢复到原位置
            }
        }
    }

    @Override
    public void redo() {
        // 重做删除操作：再次从父元素中移除元素
        this.parentElement.removeChild(this.elementToDelete);
    }
}