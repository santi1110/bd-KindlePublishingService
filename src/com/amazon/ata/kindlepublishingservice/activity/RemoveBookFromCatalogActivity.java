package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;
import com.amazonaws.services.lambda.runtime.Context;

import javax.inject.Inject;

public class RemoveBookFromCatalogActivity {
    @Inject
    RemoveBookFromCatalogActivity() {}
    public RemoveBookFromCatalogResponse handleRequest(RemoveBookFromCatalogRequest removeBookFromCatalogRequest, Context context) {
        return null;
    }
}
