package com.violette.exception;

/**
 * @author Violette
 * @date 2024/10/31 4:04
 */
public class NotExistsException extends Exception {
    public NotExistsException(String className) {
        super(String.format("不存在 %s", className));
    }

    public NotExistsException(String className, String value) {
        super(String.format("不存在 %s = %s 的元素", className, value));
    }

    public NotExistsException(String className, String value, String elementName) {
        super(String.format("不存在 %s = %s 的 %s", className, value, elementName));
    }
}
