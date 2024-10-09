package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;
import com.amazonaws.services.lambda.runtime.Context;

import javax.inject.Inject;

public class RemoveBookFromCatalogActivity {
    private final CatalogDao catalogDao;

    @Inject
    public RemoveBookFromCatalogActivity(CatalogDao catalogDao) {
        this.catalogDao = catalogDao;
    }

    public RemoveBookFromCatalogResponse execute(RemoveBookFromCatalogRequest removeBookFromCatalogRequest) {
        String bookId = removeBookFromCatalogRequest.getBookId();

        // Retrieve the CatalogItemVersion by bookId
        CatalogItemVersion catalogItemVersion = catalogDao.getBookFromCatalog(bookId);

        // Check if the book was found and is not inactive
        if (catalogItemVersion == null || catalogItemVersion.isInactive()) {
            // If the book is not found or is inactive, throw the BookNotFoundException
            throw new BookNotFoundException("Book with ID " + bookId + " not found or is inactive.");
        }

        // Mark the book as inactive (soft delete) or proceed with any other removal logic
        catalogDao.removeBookFromCatalog(bookId);

        // Return an appropriate response
        return new RemoveBookFromCatalogResponse("Book successfully marked as inactive.");
    }
}
