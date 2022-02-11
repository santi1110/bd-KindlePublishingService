package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 /**
 * A runnable capable of publishing a book from the provided BookPublishRequestQueue. If no requests are present in the
 * queue, the run method returns immediately.
 */
public class BookPublishTask implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(BookPublishTask.class);

    private final BookPublishRequestManager requestManager;
    private final PublishingStatusDao publishingStatusDao;
    private final CatalogDao catalogDao;

    /**
     * Instantiates a new BookPublishingTask object.
     *
     * @param requestManager maintains a queue of books to be processed.
     * @param publishingStatusDao PublishingStatusDao to access the publishing status table.
     * @param catalogDao CatalogDao to access the catalog table.
     */
    @Inject
    public BookPublishTask(BookPublishRequestManager requestManager, PublishingStatusDao publishingStatusDao,
                           CatalogDao catalogDao) {
        this.requestManager = requestManager;
        this.publishingStatusDao = publishingStatusDao;
        this.catalogDao = catalogDao;
    }

    @Override
    public void run() {
        BookPublishRequest publishingRequest = requestManager.getBookPublishRequestToProcess();
        if (publishingRequest == null) {
            return;
        }

        try {
            publishingStatusDao.setPublishingStatus(publishingRequest.getPublishingRecordId(),
                PublishingRecordStatus.IN_PROGRESS,
                publishingRequest.getBookId());

            KindleFormattedBook formattedBook = KindleFormatConverter.format(publishingRequest);
            CatalogItemVersion catalogItem = catalogDao.createOrUpdateBook(formattedBook);
            publishingStatusDao.setPublishingStatus(publishingRequest.getPublishingRecordId(),
                PublishingRecordStatus.SUCCESSFUL,
                catalogItem.getBookId());
        } catch (Exception e) {
            publishingStatusDao.setPublishingStatus(publishingRequest.getPublishingRecordId(),
                PublishingRecordStatus.FAILED,
                publishingRequest.getBookId(),
                e.getMessage());
        }
    }
}
