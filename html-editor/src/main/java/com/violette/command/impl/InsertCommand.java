package com.violette.command.impl;

import com.violette.command.Command;
import com.violette.editor.HtmlDocument;
import com.violette.editor.HtmlElement;
import com.violette.editor.TagElement;
import com.violette.editor.TextElement;
import com.violette.exception.NotExistsException;
import com.violette.exception.RepeatedException;
import lombok.Data;

import java.util.List;

/**
 * @author Violette
 * @date 2024/10/30 23:58
 */
@Data
public class InsertCommand implements Command {
    private HtmlDocument document;
    private TagElement newElement;
    private TagElement refElement;   // 参考元素，即新元素将插入到该元素之前
    private TagElement parentElement; // 参考元素的直接父节点，用于插入新元素
    private String textContent;

    public InsertCommand(HtmlDocument document, String tagName, String idValue, String insertLocation, String textContent) throws NotExistsException, RepeatedException{
        this.document = document;
        this.textContent = textContent;
        // 寻找插入位置
        Pair<TagElement, TagElement> result = findElementAndParent(document, insertLocation);
        this.refElement = result.first;
        this.parentElement = result.second;
        if (this.refElement == null || this.parentElement == null) {
            throw new NotExistsException("id", insertLocation);
        }
        // 确保id唯一
        if (isIdExists(document, idValue)) {
            throw new RepeatedException("id", idValue);
        }
        this.newElement = new TagElement(tagName, idValue);
    }

    @Override
    public void execute() {
        int index = parentElement.getChildren().indexOf(refElement);
        parentElement.getChildren().add(index, newElement); // 在 refElement 之前插入
        if (textContent != null && !textContent.isEmpty()) {  // FIXME: 空格是否要作为文本内容
            newElement.addChild(new TextElement(textContent)); // 添加文本内容作为子元素
        }
    }

    @Override
    public void undo() {
        parentElement.getChildren().remove(newElement);
    }

    @Override
    public void redo() {
        int index = parentElement.getChildren().indexOf(refElement);
        parentElement.getChildren().add(index, newElement); // 在 refElement 之前插入
    }

    private Pair<TagElement, TagElement> findElementAndParent(HtmlElement element, String id) {
        if (element instanceof TagElement tagElement) {
            List<HtmlElement> children = tagElement.getChildren();
            for (HtmlElement child : children) {
                if (child instanceof TagElement && id.equals(((TagElement) child).getId())) {
                    return new Pair<>((TagElement) child, tagElement); // 找到目标元素，返回目标元素及其父元素
                }
                Pair<TagElement, TagElement> result = findElementAndParent(child, id);
                if (result.first != null) {
                    return result; // 递归查找，返回找到的目标元素及其父元素
                }
            }
        }
        return new Pair<>(null, null); // 未找到
    }

    private boolean isIdExists(HtmlElement element, String id) {
        if (element instanceof TagElement tagElement) {
            if (id.equals(tagElement.getId())) {
                return true;
            }
            for (HtmlElement child : tagElement.getChildren()) {
                if (isIdExists(child, id)) {
                    return true;
                }
            }
        }
        return false;
    }

    private record Pair<T1, T2>(T1 first, T2 second) {
    }
}
