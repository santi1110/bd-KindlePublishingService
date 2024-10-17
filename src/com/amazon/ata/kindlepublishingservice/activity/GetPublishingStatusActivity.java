package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.exceptions.PublishingStatusNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;
import com.amazonaws.services.lambda.runtime.Context;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class GetPublishingStatusActivity {

    private final PublishingStatusDao publishingStatusDao;

    @Inject
    public GetPublishingStatusActivity(PublishingStatusDao publishingStatusDao) {
        this.publishingStatusDao = publishingStatusDao;
    }

    public GetPublishingStatusResponse execute(GetPublishingStatusRequest publishingStatusRequest) {
        // Query the DynamoDB table for publishing statuses
        String publishingRecordId = publishingStatusRequest.getPublishingRecordId();

        // Query the DynamoDB table for publishing statuses using the provided ID
        List<PublishingStatusItem> statusItems = publishingStatusDao.getPublishingStatuses(publishingRecordId);

        // Throw an exception if no items are found
        if (statusItems.isEmpty()) {
            throw new PublishingStatusNotFoundException("No publishing statuses found for ID: " + publishingRecordId);
        }

        // Convert PublishingStatusItem to PublishingStatusRecord
        List<PublishingStatusRecord> statusRecords = statusItems.stream()
                .map(item -> PublishingStatusRecord.builder()
                        .withStatus(item.getStatus().name())  // Convert enum to string
                        .withStatusMessage(item.getStatusMessage())
                        .withBookId(item.getBookId())
                        .build())
                .collect(Collectors.toList());

        // Return the response with the converted records
        return GetPublishingStatusResponse.builder()
                .withPublishingStatusHistory(statusRecords)
                .build();
    }
}
