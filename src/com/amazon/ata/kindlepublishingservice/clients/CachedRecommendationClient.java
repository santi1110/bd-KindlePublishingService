package com.amazon.ata.kindlepublishingservice.clients;

import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazon.ata.kindlepublishingservice.metrics.MetricsConstants;
//import com.amazon.ata.kindlepublishingservice.metrics.MetricsPublisher;
import com.amazon.ata.recommendationsservice.types.BookRecommendation;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
//import javax.measure.unit.Unit;

/**
 * DAO used to call RecommendationsService, backed by a cache.
 */
public class CachedRecommendationClient implements RecommendationClient {

    private final LoadingCache<BookGenre, List<BookRecommendation>> bookRecommendationsCache;
//    private final MetricsPublisher metricsPublisher;

    /**
     * Instantiates a new CachingRecommendationsServiceClient object.
     *
     * @param delegateRecommendationClient RecommendationsServiceClient used to call RecommendationsService.
     */
    @Inject
    public CachedRecommendationClient(RecommendationClient delegateRecommendationClient) {
//        this.metricsPublisher = metricsPublisher;
        bookRecommendationsCache = CacheBuilder.newBuilder()
                .maximumSize(BookGenre.values().length + 1)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(CacheLoader.from(delegateRecommendationClient::getBookRecommendations));
    }


    /**
     * Get book recommendations based on passed in genre.
     *
     * @param genre to get recommendations for.
     * @return list of book recommendations.
     */
    @Override
    public List<BookRecommendation> getBookRecommendations(BookGenre genre) {
//        metricsPublisher.addCount(MetricsConstants.BOOK_RECOMMENDATIONS_CACHE_QUERY_COUNT, 1, Unit.ONE);
        return bookRecommendationsCache.getUnchecked(genre);
    }
}
