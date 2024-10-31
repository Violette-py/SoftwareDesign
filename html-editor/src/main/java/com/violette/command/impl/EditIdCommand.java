package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.HtmlDocument;
import com.violette.editor.TagElement;
import com.violette.exception.NotExistsException;
import com.violette.exception.RepeatedException;
import com.violette.utils.DocumentUtil;
import lombok.Data;

/**
 * @author Violette
 * @date 2024/10/31 15:09
 * @description 编辑某元素的 id
 *
 * edit-id oldId newId
 * @Param oldId 现有元素的 id
 * @Param newId 新 id，注意 id 不能与其他元素重复
 */
@Data
public class EditIdCommand implements Command {
    private HtmlDocument document;
    private String oldId;
    private String newId;
    private TagElement element; // 要更改ID的元素

    /**
     * EditIdCommand 的构造函数。
     * @param document HTML文档。
     * @param oldId 元素当前的ID。
     * @param newId 要更改为的新ID。
     * @throws NotExistsException 如果指定的旧ID不存在。
     * @throws RepeatedException 如果新ID已存在。
     */
    public EditIdCommand(HtmlDocument document, String oldId, String newId) throws NotExistsException, RepeatedException {
        this.document = document;
        this.oldId = oldId;
        this.newId = newId;

        // 查找旧ID对应的元素
        this.element = DocumentUtil.findElementById(document, oldId);
        if (this.element == null) {
            throw new NotExistsException("id", oldId);
        }

        // 检查新ID是否已存在
        if (DocumentUtil.isIdExists(document, newId) && !oldId.equals(newId)) {
            throw new RepeatedException("id", newId);
        }
    }

    @Override
    public void execute() {
        // 更改元素的ID
        element.setId(newId);
    }

    @Override
    public void undo() {
        // 撤销更改，将ID改回旧值
        element.setId(oldId);
    }

    @Override
    public void redo() {
        // 重做更改，再次将ID更改为新值
        element.setId(newId);
    }

    @Override
    public boolean isDisplayCommand() {
        return false;
    }

    @Override
    public boolean isIOCommand() {
        return false;
    }
}
