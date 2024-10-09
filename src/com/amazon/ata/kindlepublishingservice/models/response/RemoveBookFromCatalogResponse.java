package com.amazon.ata.kindlepublishingservice.models.response;

public class RemoveBookFromCatalogResponse {
    private String message;

    // Constructor accepting a String
    public RemoveBookFromCatalogResponse(String message) {
        this.message = message;
    }

    // Getter and setter for message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

