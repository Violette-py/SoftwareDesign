package com.violette.utils;

import com.violette.document.HtmlElement;
import com.violette.document.TagElement;
import com.violette.document.TextElement;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

/**
 * @author Violette
 * @date 2024/12/13 14:26
 */
public class HtmlConverter {

    /**
     * Jsoup的Document对象 => 自定义的HTML模型
     *
     * @param jsoupElement Jsoup的Element对象。
     * @param tagElement   自定义的TagElement对象。
     */
    public static void convertJsoupModelToCustomModel(Element jsoupElement, TagElement tagElement) {
        String tagName = jsoupElement.tagName();
        tagElement.setTagName(tagName);

        // 处理当前元素id
        for (Attribute attribute : jsoupElement.attributes()) {
            if ("id".equals(attribute.getKey())) {
                tagElement.setId(attribute.getValue());
            }
        }

        // 处理子元素和文本节点
        for (Node node : jsoupElement.childNodes()) { // 不要用.children()
            if (node instanceof Element childJsoupElement) {
                // 默认id为标签名
                TagElement childTagElement = new TagElement(childJsoupElement.tagName(), childJsoupElement.hasAttr("id") ? childJsoupElement.attr("id") : childJsoupElement.tagName());
                convertJsoupModelToCustomModel(childJsoupElement, childTagElement);
                tagElement.addChild(childTagElement);
            } else if (node instanceof TextNode textNode) {
                String text = textNode.text();
                if (!text.trim().isEmpty()) {
                    tagElement.addChild(new TextElement(text));
                }
            }
        }
    }


    /**
     * 自定义的HTML模型 => Jsoup的Document对象
     *
     * @param htmlElement 自定义的HtmlElement对象。
     * @return Jsoup的Document对象。
     */
    public static Node convertCustomModelToJsoupModel(HtmlElement htmlElement) {
        if (htmlElement instanceof TextElement textElement) {
            return new TextNode(textElement.getText());
        } else if (htmlElement instanceof TagElement tagElement) {
            Element jsoupElement = new Element(tagElement.getTagName()).attr("id", tagElement.getId());
            for (HtmlElement child : tagElement.getChildren()) {
                jsoupElement.appendChild(convertCustomModelToJsoupModel(child));
            }
            return jsoupElement;
        }
        return new TextNode("");
    }
}
