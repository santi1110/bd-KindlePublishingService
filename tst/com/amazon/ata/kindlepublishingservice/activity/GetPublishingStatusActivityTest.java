package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.GetPublishingStatusResponse;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.PublishingStatusNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetPublishingStatusActivityTest {
    private static String PUBLISHING_RECORD_ID = "publishingstatus.123";

    @Mock
    private PublishingStatusDao publishingStatusDao;

    @InjectMocks
    private GetPublishingStatusActivity activity;

    @BeforeEach
    public void setup(){
        initMocks(this);
    }

    @Test
    public void execute_publishingRecordExists_returnsRecord() {
        // GIVEN
        GetPublishingStatusRequest request = GetPublishingStatusRequest
            .builder()
            .withPublishingRecordId(PUBLISHING_RECORD_ID)
            .build();

        PublishingStatusItem item = new PublishingStatusItem();
        item.setStatus(PublishingRecordStatus.QUEUED);
        item.setPublishingRecordId(PUBLISHING_RECORD_ID);

        when(publishingStatusDao.getPublishingStatuses(PUBLISHING_RECORD_ID)).thenReturn(Arrays.asList(item));

        // WHEN
        GetPublishingStatusResponse response = activity.handleRequest(request, null);

        // THEN
        assertNotNull(response, "Expected request to return a non-null response.");
        assertNotNull(response.getPublishingStatusHistory(), "Expected a non null list in the response.");
        assertEquals(1, response.getPublishingStatusHistory().size(), "Expected 1 record.");
        assertEquals(PublishingRecordStatus.QUEUED.name(), response.getPublishingStatusHistory().get(0).getStatus(),
            "Expected record in response to have queued status.");
    }

    @Test
    public void execute_publishingRecordsExists_returnsRecords() {
        // GIVEN
        GetPublishingStatusRequest request = GetPublishingStatusRequest
            .builder()
            .withPublishingRecordId(PUBLISHING_RECORD_ID)
            .build();

        PublishingStatusItem item1 = new PublishingStatusItem();
        item1.setStatus(PublishingRecordStatus.QUEUED);
        item1.setPublishingRecordId(PUBLISHING_RECORD_ID);

        PublishingStatusItem item2 = new PublishingStatusItem();
        item2.setStatus(PublishingRecordStatus.IN_PROGRESS);
        item2.setPublishingRecordId(PUBLISHING_RECORD_ID);

        when(publishingStatusDao.getPublishingStatuses(PUBLISHING_RECORD_ID)).thenReturn(Arrays.asList(item1, item2));

        // WHEN
        GetPublishingStatusResponse response = activity.handleRequest(request, null);

        // THEN
        assertNotNull(response, "Expected request to return a non-null response.");
        assertNotNull(response.getPublishingStatusHistory(), "Expected a non null list in the response.");
        assertEquals(2, response.getPublishingStatusHistory().size(), "Expected 2 records.");
        assertEquals(PublishingRecordStatus.QUEUED.name(), response.getPublishingStatusHistory().get(0).getStatus(),
            "Expected first record in response to have queued status.");
        assertEquals(PublishingRecordStatus.IN_PROGRESS.name(), response.getPublishingStatusHistory().get(1).getStatus(),
            "Expected second record in response to have in_progress status.");
    }

    @Test
    public void execute_publishingRecordDoesNotExist_throwsException() {
        // GIVEN
        GetPublishingStatusRequest request = GetPublishingStatusRequest
            .builder()
            .withPublishingRecordId("notAPublishingId")
            .build();

        when(publishingStatusDao.getPublishingStatuses("notAPublishingId")).
            thenThrow(new PublishingStatusNotFoundException("No publishing records found"));

        // WHEN & THEN
        assertThrows(PublishingStatusNotFoundException.class, () -> activity.handleRequest(request, null),
            "Expected activity to throw an exception if the publishing record can't be found.");
    }
}
