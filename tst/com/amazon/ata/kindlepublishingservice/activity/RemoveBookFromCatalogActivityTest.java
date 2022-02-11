package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.RemoveBookFromCatalogResponse;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemoveBookFromCatalogActivityTest {

    private static final String BOOK_ID = "book123";

    @Mock
    private CatalogDao catalogDao;

    @InjectMocks
    private RemoveBookFromCatalogActivity activity;


    @BeforeEach
    public void setup(){
        initMocks(this);
    }

    @Test
    public void execute_requestProvided_returnsNonNullResponse() {
        // GIVEN
        RemoveBookFromCatalogRequest request = RemoveBookFromCatalogRequest
            .builder()
            .withBookId(BOOK_ID)
            .build();

        // WHEN
        RemoveBookFromCatalogResponse response = activity.handleRequest(request, null);

        // THEN
        assertNotNull(response, "Expected a response when executing the operation.");
    }

    @Test
    public void execute_bookExists_bookRemoved() {
        // GIVEN
        RemoveBookFromCatalogRequest request = RemoveBookFromCatalogRequest
            .builder()
            .withBookId(BOOK_ID)
            .build();

        // WHEN
        RemoveBookFromCatalogResponse response = activity.handleRequest(request, null);

        // THEN
        assertNotNull(response, "Expected request to return a non-null response.");
        verify(catalogDao).removeBookFromCatalog(BOOK_ID);
    }

    @Test
    public void execute_bookDoesNotExist_throwsException() {
        // GIVEN
        RemoveBookFromCatalogRequest request = RemoveBookFromCatalogRequest
            .builder()
            .withBookId("notAbook.123")
            .build();

        doThrow(new BookNotFoundException("No book found")).when(catalogDao)
            .removeBookFromCatalog("notAbook.123");

        // WHEN & THEN
        assertThrows(BookNotFoundException.class, () -> activity.handleRequest(request, null), "Expected activity to " +
            "throw an exception if the book can't be found.");
    }
}
