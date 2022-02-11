package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BookPublishTaskTest {

    private static final String BOOK_ID = "book.123";
    private static final String PUBLISHING_ID = "publishing.123";

    @Mock
    private PublishingStatusDao publishingStatusDao;

    @Mock
    private CatalogDao catalogDao;

    @Mock
    private BookPublishRequestManager requestManager;

    @InjectMocks
    private BookPublishTask bookPublishTask;

    @BeforeEach
    public void setup() {
        initMocks(this);
    }

    @Test
    public void run_queueEmpty_noProcessing() {
        // GIVEN
        when(requestManager.getBookPublishRequestToProcess()).thenReturn(null);

        // WHEN
        bookPublishTask.run();

        // THEN
        verifyZeroInteractions(catalogDao);
        verifyZeroInteractions(publishingStatusDao);
    }

    @Test
    public void run_createBookFails_publishingStatusFailed() {
        // GIVEN
        BookPublishRequest request = BookPublishRequest.builder()
            .withPublishingRecordId(PUBLISHING_ID)
            .withText("Text")
            .build();
        when(requestManager.getBookPublishRequestToProcess()).thenReturn(request);

        String exceptionMessage = "Can't find book in catalog";
        when(catalogDao.createOrUpdateBook(any(KindleFormattedBook.class)))
            .thenThrow(new BookNotFoundException(exceptionMessage));

        // WHEN
        bookPublishTask.run();

        // THEN
        verify(publishingStatusDao).setPublishingStatus(request.getPublishingRecordId(),
            PublishingRecordStatus.IN_PROGRESS,
            request.getBookId());
        verify(publishingStatusDao).setPublishingStatus(request.getPublishingRecordId(),
            PublishingRecordStatus.FAILED,
            request.getBookId(),
            exceptionMessage);
    }

    @Test
    public void run_updateBook_publishingStatusSuccessful() {
        // GIVEN
        BookPublishRequest request = BookPublishRequest.builder()
            .withPublishingRecordId(PUBLISHING_ID)
            .withText("Text")
            .withBookId(BOOK_ID)
            .build();
        when(requestManager.getBookPublishRequestToProcess()).thenReturn(request);

        CatalogItemVersion item = new CatalogItemVersion();
        item.setBookId(BOOK_ID);

        when(catalogDao.createOrUpdateBook(any(KindleFormattedBook.class))).thenReturn(item);

        // WHEN
        bookPublishTask.run();

        // THEN
        verify(publishingStatusDao).setPublishingStatus(request.getPublishingRecordId(),
            PublishingRecordStatus.IN_PROGRESS,
            BOOK_ID);
        verify(publishingStatusDao).setPublishingStatus(request.getPublishingRecordId(),
            PublishingRecordStatus.SUCCESSFUL,
            BOOK_ID);
        verify(catalogDao).createOrUpdateBook(any(KindleFormattedBook.class));
    }

    @Test
    public void run_createBook_publishingStatusSuccessful() {
        // GIVEN
        BookPublishRequest request = BookPublishRequest.builder()
            .withPublishingRecordId(PUBLISHING_ID)
            .withText("Text")
            .build();
        when(requestManager.getBookPublishRequestToProcess()).thenReturn(request);

        CatalogItemVersion item = new CatalogItemVersion();
        item.setBookId(BOOK_ID);

        when(catalogDao.createOrUpdateBook(any(KindleFormattedBook.class))).thenReturn(item);

        // WHEN
        bookPublishTask.run();

        // THEN
        verify(publishingStatusDao).setPublishingStatus(request.getPublishingRecordId(),
            PublishingRecordStatus.IN_PROGRESS,
            null);
        verify(publishingStatusDao).setPublishingStatus(request.getPublishingRecordId(),
            PublishingRecordStatus.SUCCESSFUL,
            BOOK_ID);
        verify(catalogDao).createOrUpdateBook(any(KindleFormattedBook.class));
    }
}