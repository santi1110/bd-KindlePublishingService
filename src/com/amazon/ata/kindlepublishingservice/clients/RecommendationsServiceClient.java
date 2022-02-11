package com.amazon.ata.kindlepublishingservice.clients;

import com.amazon.ata.recommendationsservice.types.BookGenre;
// import com.amazon.ata.kindlepublishingservice.metrics.MetricsConstants;
//import com.amazon.ata.kindlepublishingservice.metrics.MetricsPublisher;
import com.amazon.ata.recommendationsservice.RecommendationsService;
import com.amazon.ata.recommendationsservice.types.BookRecommendation;

import java.util.List;
import javax.inject.Inject;


/**
 * Client used to call Recommendations Service.
 */
public class RecommendationsServiceClient implements RecommendationClient {

    private final RecommendationsService recommendationsService;
    //private final MetricsPublisher metricsPublisher;

    /**
     * Instantiates a new RecommendationsServiceClient.
     *
     * @param service RecommendationsService to call.
     */
    @Inject
    public RecommendationsServiceClient(RecommendationsService service) {
        this.recommendationsService = service;
//        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Returns a list of book recommendations based on the passed in genre. An empty list will be returned
     * if no recommendations are found or cannot be generated.
     * @param genre genre to get recommendations for.
     * @return list of book recommendations.
     */
    @Override
    public List<BookRecommendation> getBookRecommendations(BookGenre genre) {
        final double startTime = System.currentTimeMillis();

        List<BookRecommendation> recommendations = recommendationsService.getBookRecommendations(
            BookGenre.valueOf(genre.name()));

        final double endTime = System.currentTimeMillis() - startTime;
        //metricsPublisher.addTime(MetricsConstants.GET_BOOK_RECOMMENDATIONS_TIME, endTime, SI.MILLI(SI.SECOND));
        //metricsPublisher.addCount(MetricsConstants.GET_BOOK_RECOMMENDATIONS_COUNT, 1, Unit.One);

        return recommendations;
    }
}
