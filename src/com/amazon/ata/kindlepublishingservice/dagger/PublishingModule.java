package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazon.ata.kindlepublishingservice.activity.SubmitBookForPublishingActivity;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequestManager;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublisher;

import com.amazon.ata.kindlepublishingservice.publishing.NoOpTask;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Singleton;

@Module
public class PublishingModule {

    @Provides
    @Singleton
    public BookPublisher provideBookPublisher(ScheduledExecutorService scheduledExecutorService) {
        return new BookPublisher(scheduledExecutorService, new NoOpTask());
    }

    @Provides
    @Singleton
    public ScheduledExecutorService provideBookPublisherScheduler() {
        return Executors.newScheduledThreadPool(1);
    }

    @Provides
    public SubmitBookForPublishingActivity provideSubmitBookForPublishingActivity(
            PublishingStatusDao publishingStatusDao,
            CatalogDao catalogDao,
            BookPublishRequestManager bookPublishRequestManager) {
        return new SubmitBookForPublishingActivity(publishingStatusDao, catalogDao, bookPublishRequestManager);
    }

    @Provides
    public CatalogDao provideCatalogDao(DynamoDBMapper dynamoDBMapper) {
        return new CatalogDao(dynamoDBMapper);
    }

    @Provides
    public BookPublishRequestManager provideBookPublishRequestManager() {
        return new BookPublishRequestManager();
    }



}
