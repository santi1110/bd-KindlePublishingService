package com.amazon.ata.kindlepublishingservice.converters;

import com.amazon.ata.kindlepublishingservice.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PublishingStatusCoralConverterTest {

    private static final String PUBLISHING_ID = "publishingstatus.123";
    private static final String STATUS_MESSAGE = "Successful";
    private static final String BOOK_ID = "book.123";

    @Test
    public void toCoral_singleItem_returnsStatusRecord() {
        // GIVEN
        PublishingStatusItem item = new PublishingStatusItem();
        item.setPublishingRecordId(PUBLISHING_ID);
        item.setStatusMessage(STATUS_MESSAGE);
        item.setStatus(PublishingRecordStatus.SUCCESSFUL);
        item.setBookId(BOOK_ID);

        // WHEN
        PublishingStatusRecord result = PublishingStatusCoralConverter.toCoral(item);

        // THEN
        verifyConversion(item, result);
    }

    @Test
    public void toCoral_multipleItems_returnsMultipleStatusRecords() {
        // GIVEN
        PublishingStatusItem item1 = new PublishingStatusItem();
        item1.setPublishingRecordId(PUBLISHING_ID);
        item1.setStatusMessage(STATUS_MESSAGE);
        item1.setStatus(PublishingRecordStatus.IN_PROGRESS);

        PublishingStatusItem item2 = new PublishingStatusItem();
        item2.setPublishingRecordId(PUBLISHING_ID);
        item2.setStatusMessage(STATUS_MESSAGE);
        item2.setStatus(PublishingRecordStatus.SUCCESSFUL);
        item2.setBookId(BOOK_ID);

        // WHEN
        List<PublishingStatusRecord> results = PublishingStatusCoralConverter.toCoral(Arrays.asList(item1, item2));

        // THEN
        assertEquals(2, results.size(), "Expected 2 items to be converted.");

        PublishingStatusRecord record1 = results.get(0);
        verifyConversion(item1, record1);

        PublishingStatusRecord record2 = results.get(1);
        verifyConversion(item2, record2);
    }

    private void verifyConversion(PublishingStatusItem item, PublishingStatusRecord result) {
        assertEquals(item.getStatusMessage(), result.getStatusMessage(), "Status message incorrectly converted.");
        assertEquals(item.getStatus().name(), result.getStatus(), "Status incorrectly converted.");
        assertEquals(item.getBookId(), result.getBookId(), "BookId incorrectly converted.");
    }

}
