package com.violette.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author Violette
 * @date 2024/10/31 11:17
 */
public class CapturingPrintStream {
    private StringBuilder contents;
    private static final PrintStream originalOut = System.out;

    public static CapturingPrintStream capture() {
        return new CapturingPrintStream();
    }

    private CapturingPrintStream() {
        this.contents = new StringBuilder();
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                if (b == '\n') {
                    contents.append('\n'); // 直接添加'\n'字符
                } else {
                    contents.append((char) b);
                }
                originalOut.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) {
                String str = new String(b, off, len);
                contents.append(str.replace("\r\n", "\n")); // 将"\r\n"替换为"\n"
                originalOut.write(b, off, len);
            }

            @Override
            public void write(byte[] b) throws IOException {
                String str = new String(b);
                contents.append(str.replace("\r\n", "\n")); // 将"\r\n"替换为"\n"
                originalOut.write(b);
            }
        }, true));
    }

    public static void release() {
        System.setOut(originalOut);
    }

    public String getContents() {
        return contents.toString();
    }
}
