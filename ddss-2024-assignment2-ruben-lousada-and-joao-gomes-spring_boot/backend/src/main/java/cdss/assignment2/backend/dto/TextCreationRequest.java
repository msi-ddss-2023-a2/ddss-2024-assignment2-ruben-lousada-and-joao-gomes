package cdss.assignment2.backend.dto;

import cdss.assignment2.backend.model.Account;
import lombok.Data;

public class TextCreationRequest {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
