package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazon.ata.kindlepublishingservice.clients.RecommendationClient;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import dagger.Component;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Component(modules = {
        ClientsModule.class,
        DataAccessModule.class,
        PublishingModule.class
})
public interface ApplicationComponent {

}
