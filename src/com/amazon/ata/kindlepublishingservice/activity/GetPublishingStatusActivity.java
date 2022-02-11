package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.GetPublishingStatusResponse;
import com.amazon.ata.kindlepublishingservice.converters.PublishingStatusCoralConverter;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.List;
import javax.inject.Inject;

/**
 * Implementation of the GetPublishingStatusActivity for ATACurriculumKindlePublishingService's
 * GetPublishingStatus API.
 *
 * This API allows the client to retrieve the publishing status updates of a book publish request.
 */
public class GetPublishingStatusActivity implements RequestHandler<GetPublishingStatusRequest, GetPublishingStatusResponse> {

    private PublishingStatusDao publishingStatusDao;

    /**
     * Instantiates a new GetPublishingStatusActivity object.
     *
     * @param publishingStatusDao PublishingStatusDao to access the publishing status table.
     */
    @Inject
    public GetPublishingStatusActivity(PublishingStatusDao publishingStatusDao) {
        this.publishingStatusDao = publishingStatusDao;
    }

    /**
     * Retrieves the publishing status record associated with the provided id. The response contains the current
     * status of the publishing workflow. A message may also be included to provide additional details. A bookId will
     * be present if the publishing status is SUCCESSFUL.
     *
     * @param request request containing the publishingRecordId
     * @return a response object containing the publishing record's status along with a message and bookId if publishing
     *         has been successful
     */
    public GetPublishingStatusResponse handleRequest(final GetPublishingStatusRequest request, Context context) {
        List<PublishingStatusItem> publishingHistory = publishingStatusDao
            .getPublishingStatuses(request.getPublishingRecordId());

        return GetPublishingStatusResponse.builder()
            .withPublishingStatusHistory(PublishingStatusCoralConverter.toCoral(publishingHistory))
            .build();
    }
}
