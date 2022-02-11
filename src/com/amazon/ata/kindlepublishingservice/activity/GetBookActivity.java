package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazon.ata.kindlepublishingservice.GetBookRequest;
import com.amazon.ata.kindlepublishingservice.GetBookResponse;
import com.amazon.ata.kindlepublishingservice.clients.RecommendationClient;
import com.amazon.ata.kindlepublishingservice.converters.CatalogCoralConverter;
import com.amazon.ata.kindlepublishingservice.converters.RecommendationsCoralConverter;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.recommendationsservice.types.BookRecommendation;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.List;
import javax.inject.Inject;

/**
 * Implementation of the GetBookActivity for the ATACurriculumKindlePublishingService's
 * GetBook API.
 *
 * This API allows the client to retrieve a book.
 */

public class GetBookActivity implements RequestHandler<GetBookRequest, GetBookResponse> {
    private RecommendationClient recommendationClient;
    private CatalogDao catalogDao;

    /**
     * Instantiates a new GetBookActivity object.
     *
     * @param catalogDao CatalogDao to access the Catalog table.
     * @param recommendationClient Returns recommendations based on genre.
     */
    @Inject
    public GetBookActivity(CatalogDao catalogDao, RecommendationClient recommendationClient) {
        this.catalogDao = catalogDao;
        this.recommendationClient = recommendationClient;
    }

    /**
     * Retrieves the book associated with the provided book id.
     *
     * @param request Request object containing the book ID associated with the book to get from the Catalog.
     * @return GetBookResponse Response object containing the requested book.
     */

    public GetBookResponse handleRequest(final GetBookRequest request, Context context) {
        CatalogItemVersion book = catalogDao.getBookFromCatalog(request.getBookId());
        List<BookRecommendation> recommendations = recommendationClient.getBookRecommendations(
            BookGenre.valueOf(book.getGenre().name()));
        return GetBookResponse.builder()
            .withBook(CatalogCoralConverter.toCoral(book))
            .withRecommendations(RecommendationsCoralConverter.toCoral(recommendations))
            .build();
    }
}
