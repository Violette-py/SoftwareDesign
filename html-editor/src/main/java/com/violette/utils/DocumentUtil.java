package com.violette.utils;

import com.violette.editor.HtmlElement;
import com.violette.editor.TagElement;

import java.util.List;

/**
 * @author Violette
 * @date 2024/10/31 13:45
 */
public class DocumentUtil {
    public static boolean isIdExists(HtmlElement element, String id) {
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

    public static Pair<TagElement, TagElement> findTargetElementAndItsParent(HtmlElement element, String id) {
        if (element instanceof TagElement tagElement) {
            List<HtmlElement> children = tagElement.getChildren();
            for (HtmlElement child : children) {
                if (child instanceof TagElement && id.equals(((TagElement) child).getId())) {
                    return new Pair<>((TagElement) child, tagElement); // 找到目标元素，返回目标元素及其父元素
                }
                Pair<TagElement, TagElement> result = findTargetElementAndItsParent(child, id);
                if (result.first() != null) {
                    return result; // 递归查找，返回找到的目标元素及其父元素
                }
            }
        }
        return new Pair<>(null, null); // 未找到
    }

}
