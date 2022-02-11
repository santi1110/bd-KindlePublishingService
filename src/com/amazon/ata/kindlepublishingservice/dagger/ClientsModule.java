package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazon.ata.kindlepublishingservice.clients.CachedRecommendationClient;
import com.amazon.ata.kindlepublishingservice.clients.RecommendationClient;
import com.amazon.ata.kindlepublishingservice.clients.RecommendationsServiceClient;
//import com.amazon.ata.kindlepublishingservice.metrics.MetricsPublisher;
import com.amazon.ata.recommendationsservice.RecommendationsService;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ClientsModule {

    @Singleton
    @Provides
    public RecommendationsServiceClient provideRecommendationsServiceClient(
        RecommendationsService recommendationsService) {
        return new RecommendationsServiceClient(recommendationsService);//, metricsPublisher);
    }

    @Singleton
    @Provides
    public RecommendationClient provideRecommendationClient(RecommendationsServiceClient recommendationsServiceClient)
    {//MetricsPublisher metricsPublisher) {
        return new CachedRecommendationClient(recommendationsServiceClient);
    }
}
