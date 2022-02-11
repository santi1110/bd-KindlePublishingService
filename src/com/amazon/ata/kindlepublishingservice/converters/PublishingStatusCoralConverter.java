package com.amazon.ata.kindlepublishingservice.converters;

import com.amazon.ata.coral.converter.CoralConverterUtil;
import com.amazon.ata.kindlepublishingservice.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;

import java.util.List;

/**
 * Converters for Publishing Status related objects.
 */
public class PublishingStatusCoralConverter {

    private PublishingStatusCoralConverter(){}

    /**
     * Convert a list of {@link PublishingStatusItem} elements (from database) into external coral representation: a
     * list of {@link PublishingStatusRecord} elements.
     *
     * @param publishingHistory list of PublishingStatusItem elements to be converted to coral representation
     * @return a list of converted PublishingStatusItem elements
     */
    public static List<PublishingStatusRecord> toCoral(List<PublishingStatusItem> publishingHistory) {
        return CoralConverterUtil.convertList(publishingHistory, PublishingStatusCoralConverter::toCoral);
    }

    /**
     * Converts a single {@link PublishingStatusItem} (from database) into an external coral
     * {@link PublishingStatusRecord}.
     *
     * @param publishingStatus the PublishingStatusItem to be converted
     * @return the converted PublishingStatusItem
     */
    public static PublishingStatusRecord toCoral(PublishingStatusItem publishingStatus) {
        return PublishingStatusRecord.builder()
            .withBookId(publishingStatus.getBookId())
            .withStatus(publishingStatus.getStatus().toString())
            .withStatusMessage(publishingStatus.getStatusMessage())
            .build();
    }
}
