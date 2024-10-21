package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.requests.SubmitBookForPublishingRequest;
import com.amazon.ata.kindlepublishingservice.models.response.SubmitBookForPublishingResponse;
import com.amazon.ata.kindlepublishingservice.converters.BookPublishRequestConverter;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequest;

import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequestManager;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Implementation of the SubmitBookForPublishingActivity for ATACurriculumKindlePublishingService's
 * SubmitBookForPublishing API.
 *
 * This API allows the client to submit a new book to be published in the catalog or update an existing book.
 */
public class SubmitBookForPublishingActivity {

    private PublishingStatusDao publishingStatusDao;
    private final CatalogDao catalogDao;
    private final BookPublishRequestManager requestManager;

    /**
     * Instantiates a new SubmitBookForPublishingActivity object.
     *
     * @param publishingStatusDao PublishingStatusDao to access the publishing status table.
     */
    @Inject
    public SubmitBookForPublishingActivity(PublishingStatusDao publishingStatusDao,
                                           CatalogDao catalogDao,
                                           BookPublishRequestManager requestManager) {
        this.publishingStatusDao = publishingStatusDao;
        this.catalogDao = catalogDao;
        this.requestManager = requestManager;  // Remove manual initialization
    }

    /**
     * Submits the book in the request for publishing.
     *
     * @param request Request object containing the book data to be published. If the request is updating an existing
     *                book, then the corresponding book id should be provided. Otherwise, the request will be treated
     *                as a new book.
     * @return SubmitBookForPublishingResponse Response object that includes the publishing status id, which can be used
     * to check the publishing state of the book.
     */
    public SubmitBookForPublishingResponse execute(SubmitBookForPublishingRequest request) {
 /*       final BookPublishRequest bookPublishRequest = BookPublishRequestConverter.toBookPublishRequest(request);

        // Check if the book ID exists (only for updates)
        if (bookPublishRequest.getBookId() != null) {
            catalogDao.validateBookExists(bookPublishRequest.getBookId());
        }

        // Add the book publish request to the request manager queue for processing
        requestManager.addBookPublishRequest(bookPublishRequest);

        // Set the initial publishing status to "QUEUED"
        PublishingStatusItem item = publishingStatusDao.setPublishingStatus(
                bookPublishRequest.getPublishingRecordId(),
                PublishingRecordStatus.QUEUED,
                bookPublishRequest.getBookId()
        );

        // Return the response with the publishing record ID
        return SubmitBookForPublishingResponse.builder()
                .withPublishingRecordId(item.getPublishingRecordId())
                .build();
    }*/
 /*       final BookPublishRequest bookPublishRequest = BookPublishRequestConverter.toBookPublishRequest(request);

        // Validate book exists if bookId is provided
        if (bookPublishRequest.getBookId() != null) {
            catalogDao.validateBookExists(bookPublishRequest.getBookId());
        }

        // Set status to QUEUED initially
        PublishingStatusItem statusItem = publishingStatusDao.setPublishingStatus(
                bookPublishRequest.getPublishingRecordId(),
                PublishingRecordStatus.QUEUED,
                bookPublishRequest.getBookId()
        );

        // Add the book publish request to the request manager (this should trigger IN_PROGRESS)
        requestManager.addBookPublishRequest(bookPublishRequest);

        // Immediately after adding to the request manager, update status to IN_PROGRESS
        publishingStatusDao.setPublishingStatus(
                bookPublishRequest.getPublishingRecordId(),
                PublishingRecordStatus.IN_PROGRESS,
                bookPublishRequest.getBookId()
        );

        // After completing the process, set status to SUCCESSFUL
        publishingStatusDao.setPublishingStatus(
                bookPublishRequest.getPublishingRecordId(),
                PublishingRecordStatus.SUCCESSFUL,
                bookPublishRequest.getBookId()
        );

        return SubmitBookForPublishingResponse.builder()
                .withPublishingRecordId(statusItem.getPublishingRecordId())
                .build();
    }*/
        BookPublishRequest bookPublishRequest = BookPublishRequestConverter.toBookPublishRequest(request);

        String bookId = bookPublishRequest.getBookId();

        // If bookId is null, generate a new one (new book)
        if (bookId == null) {
            bookId = "book." + UUID.randomUUID().toString();  // Generate new bookId
            bookPublishRequest = BookPublishRequest.builder()
                    .withPublishingRecordId(bookPublishRequest.getPublishingRecordId())
                    .withBookId(bookId)  // Set the new bookId
                    .withTitle(bookPublishRequest.getTitle())
                    .withAuthor(bookPublishRequest.getAuthor())
                    .withText(bookPublishRequest.getText())
                    .withGenre(bookPublishRequest.getGenre())
                    .build();
        } else {
            // Validate book exists if bookId is provided
            catalogDao.validateBookExists(bookPublishRequest.getBookId());
        }

        // Set status to QUEUED initially
        PublishingStatusItem statusItem = publishingStatusDao.setPublishingStatus(
                bookPublishRequest.getPublishingRecordId(),
                PublishingRecordStatus.QUEUED,
                bookId
        );

        // Add the book publish request to the request manager (this should trigger IN_PROGRESS)
        requestManager.addBookPublishRequest(bookPublishRequest);

        // Immediately after adding to the request manager, update status to IN_PROGRESS
        publishingStatusDao.setPublishingStatus(
                bookPublishRequest.getPublishingRecordId(),
                PublishingRecordStatus.IN_PROGRESS,
                bookId
        );

        // Publish the book and increment the version if it exists
        catalogDao.publishBook(bookPublishRequest);

        // After successfully publishing, set status to SUCCESSFUL
        publishingStatusDao.setPublishingStatus(
                bookPublishRequest.getPublishingRecordId(),
                PublishingRecordStatus.SUCCESSFUL,
                bookId
        );

        return SubmitBookForPublishingResponse.builder()
                .withPublishingRecordId(statusItem.getPublishingRecordId())
                .build();
    }
}