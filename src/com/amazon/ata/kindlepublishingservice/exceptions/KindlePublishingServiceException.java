package com.amazon.ata.kindlepublishingservice.exceptions;

/**
 * Exception to be thrown when a book cannot be found in the catalog.
 */
public class KindlePublishingServiceException extends RuntimeException {

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public KindlePublishingServiceException(String message) {
        super(message);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public KindlePublishingServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}