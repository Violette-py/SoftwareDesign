package com.violette.editor;

import com.violette.command.CommandExecutor;
import com.violette.document.HtmlDocument;
import lombok.Data;

/**
 * @author Violette
 * @date 2024/10/31 0:59
 */
@Data
public class HtmlEditor {
    private String filepath;
    private HtmlDocument document;
    private CommandExecutor commandExecutor;
    //    private boolean showId;
    private boolean isSaved;

    public HtmlEditor(String filepath) {
        this.filepath = filepath;
        this.document = new HtmlDocument();
        this.commandExecutor = new CommandExecutor();
//        this.showId = true;
        this.isSaved = true;
    }
}