package com.violette.exception;

/**
 * @author Violette
 * @date 2024/10/31 4:04
 */
public class NotExistsException extends Exception {
    public NotExistsException(String className, String value) {
        super(String.format("不存在 %s = %s 的元素", className, value));
    }
}
