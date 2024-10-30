package com.violette.exception;

/**
 * @author Violette
 * @date 2024/10/31 3:58
 */
public class RepeatedException extends Exception {
    public RepeatedException(String className, String value) {
        super(String.format("已存在 %s = %s 的元素", className, value));
    }
}
