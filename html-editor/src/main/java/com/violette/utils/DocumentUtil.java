package com.violette.utils;

import com.violette.document.HtmlElement;
import com.violette.document.TagElement;
import com.violette.document.TextElement;

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
     * 在HTML文档中查找具有指定ID的元素
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

    public static Pair<TagElement, TagElement> findElementAndItsParentById(HtmlElement element, String id) {
        if (element instanceof TagElement tagElement) {
            List<HtmlElement> children = tagElement.getChildren();
            for (HtmlElement child : children) {
                if (child instanceof TagElement && id.equals(((TagElement) child).getId())) {
                    return new Pair<>((TagElement) child, tagElement); // 找到目标元素，返回目标元素及其父元素
                }
                Pair<TagElement, TagElement> result = findElementAndItsParentById(child, id);
                if (result.first() != null) {
                    return result; // 递归查找，返回找到的目标元素及其父元素
                }
            }
        }
        return new Pair<>(null, null); // 未找到
    }

    /**
     * 在TagElement元素中查找文本元素
     * 注意：TagElement元素内部可以包含文本内容，但文本必须处在该元素的其他子元素之前，
     * 即如果存在文本元素，一定是第一个孩子
     * @param element 要搜索的元素。
     * @return 文本元素，如果没有则返回null。
     */
    public static TextElement findTextElementInTagElement(TagElement element) {
        for (HtmlElement child : element.getChildren()) {
            if (child instanceof TextElement) {
                return (TextElement) child;
            }
            break;  // 如果存在文本元素，一定是第一个孩子
        }
        return null;
    }

}
