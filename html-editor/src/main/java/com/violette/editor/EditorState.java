package com.violette.editor;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Violette
 * @date 2024/12/13 21:02
 */
@Data
public class EditorState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String filePath;
    private Boolean showId;

    public EditorState(HtmlEditor editor) {
        this.filePath = editor.getFilepath();
        this.showId = editor.getShowId();
    }
}
