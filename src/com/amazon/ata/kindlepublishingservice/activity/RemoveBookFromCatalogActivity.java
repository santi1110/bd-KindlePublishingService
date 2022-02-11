package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.RemoveBookFromCatalogResponse;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.inject.Inject;

/**
 * Implementation of the RemoveBookFromCatalogActivity for the ATACurriculumKindlePublishingService's
 * RemoveBookFromCatalog API.
 *
 * This API allows the client to remove a book from the catalog.
 */
public class RemoveBookFromCatalogActivity implements RequestHandler<RemoveBookFromCatalogRequest, RemoveBookFromCatalogResponse> {

    private CatalogDao catalogDao;

    /**
     * Constructs a RemoveBookFromCatalogActivity object.
     @param catalogDao CatalogDao to access data in the CatalogItemVersions table.
     */
    @Inject
    public RemoveBookFromCatalogActivity(CatalogDao catalogDao) {
        this.catalogDao = catalogDao;
    }

    /**
     * Implementing a dummy implementation as part of PPT04.
     * @param request Request object containing the book id associated with the book to remove.
     * @return RemoveBookFromCatalogResponse Response object
     */
    public RemoveBookFromCatalogResponse handleRequest(RemoveBookFromCatalogRequest request, Context context) {
        catalogDao.removeBookFromCatalog(request.getBookId());
        return new RemoveBookFromCatalogResponse();
    }
}
