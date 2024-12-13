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
    private HtmlDocument document;
    private CommandExecutor commandExecutor;

    public HtmlEditor() {
        this.document = new HtmlDocument();
        this.commandExecutor = new CommandExecutor();
    }
}