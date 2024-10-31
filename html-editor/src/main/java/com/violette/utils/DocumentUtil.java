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

    /**
     * 在HTML文档中查找具有指定ID的元素。
     * @param element 待搜索的元素
     * @param id 要搜索的ID
     * @return 如果找到返回对应的元素，否则返回null
     */
    public static TagElement findElementById(HtmlElement element, String id) {
        if (element instanceof TagElement tagElement) {
            if (id.equals(tagElement.getId())) {
                return tagElement;
            }
            for (HtmlElement child : (tagElement.getChildren())) {
                TagElement found = findElementById(child, id);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    public static Pair<TagElement, TagElement> findElementAndItsParent(HtmlElement element, String id) {
        if (element instanceof TagElement tagElement) {
            List<HtmlElement> children = tagElement.getChildren();
            for (HtmlElement child : children) {
                if (child instanceof TagElement && id.equals(((TagElement) child).getId())) {
                    return new Pair<>((TagElement) child, tagElement); // 找到目标元素，返回目标元素及其父元素
                }
                Pair<TagElement, TagElement> result = findElementAndItsParent(child, id);
                if (result.first() != null) {
                    return result; // 递归查找，返回找到的目标元素及其父元素
                }
            }
        }
        return new Pair<>(null, null); // 未找到
    }

}
