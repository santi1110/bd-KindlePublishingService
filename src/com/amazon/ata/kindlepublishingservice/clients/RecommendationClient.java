package com.amazon.ata.kindlepublishingservice.clients;

import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazon.ata.recommendationsservice.types.BookRecommendation;

import java.util.List;

public interface RecommendationClient {

    /**
     * Returns a list of book recommendations based on the passed in genre.
     * @param genre genre to get recommendations for.
     * @return list of book recommendations.
     */
    List<BookRecommendation> getBookRecommendations(BookGenre genre);
}
