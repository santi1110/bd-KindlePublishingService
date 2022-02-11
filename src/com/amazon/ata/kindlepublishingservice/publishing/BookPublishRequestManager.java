package com.amazon.ata.kindlepublishingservice.publishing;

import java.util.Queue;
import javax.inject.Inject;

/**
 * The BookPublishRequestManager keeps a track of submitted BookPublishRequests, ensuring they are returned in the
 * order that they were submitted.
 */
public class BookPublishRequestManager {

    private Queue<BookPublishRequest> requestQueue;

    /**
     * Constructs a BookPublishRequestManager object.
     * @param requestQueue Queue to maintain order of bookRequests to be processed.
     */
    @Inject
    public BookPublishRequestManager(Queue<BookPublishRequest> requestQueue) {
        this.requestQueue = requestQueue;
    }

    /**
     * Adds BookPublishRequest to the end of the request queue.
     *
     * @param request BookPublishRequest to add.
     */
    public void addBookPublishRequest(BookPublishRequest request) {
        requestQueue.add(request);
    }

    /**
     * Removes the BookPublishRequest next in line from the request queue, and returns it.
     *
     * @return a BookPublishRequest, which will be null if the request queue is empty.
     */
    public BookPublishRequest getBookPublishRequestToProcess() {
        if (requestQueue.isEmpty()) {
            return null;
        }
        return requestQueue.remove();
    }
}
