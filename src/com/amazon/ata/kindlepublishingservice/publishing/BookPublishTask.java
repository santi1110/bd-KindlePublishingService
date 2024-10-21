package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

public class BookPublishTask implements Runnable {

    private static final Logger log = LogManager.getLogger(BookPublishTask.class);

    private final BookPublishRequestManager requestManager;
    private final CatalogDao catalogDao;
    private final PublishingStatusDao publishingStatusDao;

    @Inject
    public BookPublishTask(BookPublishRequestManager requestManager, CatalogDao catalogDao, PublishingStatusDao publishingStatusDao) {
        this.requestManager = requestManager;
        this.catalogDao = catalogDao;
        this.publishingStatusDao = publishingStatusDao;
    }

    @Override
    public void run() {
        BookPublishRequest request = null;
        try {
            request = requestManager.getBookPublishRequestToProcess();
            if (request == null) {
                return; // No requests to process
            }

            // Update the publishing status to IN_PROGRESS
            publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.IN_PROGRESS, request.getBookId());

            // Publish the book to the catalog
            catalogDao.publishBook(request);

            // Update the publishing status to SUCCESSFUL
            publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.SUCCESSFUL, request.getBookId());
        } catch (Exception e) {
            log.error("Error processing publishing request", e);
            // Handle failed publishing status update
            publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.FAILED, request.getBookId());

        }
    }
}

