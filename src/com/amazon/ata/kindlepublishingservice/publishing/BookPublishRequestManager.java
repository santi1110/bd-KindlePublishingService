package com.amazon.ata.kindlepublishingservice.publishing;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BookPublishRequestManager {

    private final Queue<BookPublishRequest> requestQueue;

    public BookPublishRequestManager() {
        this.requestQueue = new ConcurrentLinkedQueue<>();
    }

    // Adds a new BookPublishRequest to the queue
    public void addBookPublishRequest(BookPublishRequest request) {
        requestQueue.add(request);
    }

    // Retrieves the next BookPublishRequest from the queue to be processed
    public BookPublishRequest getBookPublishRequestToProcess() {
        return requestQueue.poll();  // Returns null if the queue is empty
    }
}