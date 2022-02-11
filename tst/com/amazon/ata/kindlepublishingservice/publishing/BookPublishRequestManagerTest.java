package com.amazon.ata.kindlepublishingservice.publishing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BookPublishRequestManagerTest {

    private Queue<BookPublishRequest> ingestionQueue;
    private BookPublishRequestManager requestManager;

    @BeforeEach
    public void setup(){
        ingestionQueue = new LinkedList<>();
        requestManager = new BookPublishRequestManager(ingestionQueue);
    }

    @Test
    public void addBookPublishRequestToQueue_emptyQueue_addedToEnd() {
        // GIVEN
        BookPublishRequest request = BookPublishRequest.builder()
            .withPublishingRecordId("publishing.123")
            .build();

        // WHEN
        requestManager.addBookPublishRequest(request);

        // THEN
        assertEquals(request, requestManager.getBookPublishRequestToProcess(), "Expected the first request" +
            "added to the queue to be the first removed.");
        assertEquals(0, ingestionQueue.size(), "Expected queue to have size 0 after processing " +
            "its requests");
    }

    @Test
    public void addBookPublishRequestToQueue_nonemptyQueue_addedToEnd() {
        // GIVEN
        BookPublishRequest request1 = BookPublishRequest.builder()
            .withPublishingRecordId("publishing.123")
            .build();
        BookPublishRequest request2 = BookPublishRequest.builder()
            .withPublishingRecordId("publishing.456")
            .build();
        requestManager.addBookPublishRequest(request1);

        // WHEN
        requestManager.addBookPublishRequest(request2);

        // THEN
        // pop off the first request so we can verify the second request added gets popped off second
        requestManager.getBookPublishRequestToProcess();
        assertEquals(request2, requestManager.getBookPublishRequestToProcess(), "Expected requests" +
            "added to the queue to be added to the end of the queue.");
        assertEquals(0, ingestionQueue.size(), "Expected queue to have size 0 after processing " +
            "its requests");
    }

    @Test
    public void getBookPublishRequestToProcess_emptyQueue_nullReturned() {
        // GIVEN - empty queue

        // WHEN
        BookPublishRequest request = requestManager.getBookPublishRequestToProcess();

        // THEN
        assertNull(request, "Expected a null request to be returned from an empty queue.");
    }


    @Test
    public void getBookPublishRequestToProcess_nonemptyQueue_frontReturned() {
        // GIVEN
        BookPublishRequest request1 = BookPublishRequest.builder()
            .withPublishingRecordId("publishing.123")
            .build();
        BookPublishRequest request2 = BookPublishRequest.builder()
            .withPublishingRecordId("publishing.456")
            .build();
        requestManager.addBookPublishRequest(request1);
        requestManager.addBookPublishRequest(request2);

        // WHEN
        BookPublishRequest request = requestManager.getBookPublishRequestToProcess();

        // THEN
        assertEquals(request1, request, "Expected request added to the queue first to be removed first.");
        assertEquals(1, ingestionQueue.size(), "Expected queue to have size 1 after processing " +
            "only 1 of its 2 requests");
    }
}
