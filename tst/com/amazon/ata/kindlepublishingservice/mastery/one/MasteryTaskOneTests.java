package com.amazon.ata.kindlepublishingservice.mastery.one;

import com.amazon.ata.kindlepublishingservice.GetBookRequest;
import com.amazon.ata.kindlepublishingservice.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.exceptions.KindlePublishingClientException;

import com.amazon.ata.kindlepublishingservice.helpers.IntegrationTestBase;
import com.amazon.ata.kindlepublishingservice.helpers.KindlePublishingServiceTctTestDao;
import com.amazon.ata.kindlepublishingservice.helpers.KindlePublishingServiceTctTestDao.CatalogItemVersion;
import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

public class MasteryTaskOneTests extends IntegrationTestBase {
    private String bookId;

    private static final KindlePublishingServiceTctTestDao KINDLE_PUBLISHING_SERVICE_TCT_TEST_DAO =
            new KindlePublishingServiceTctTestDao(
                    new DynamoDBMapper(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build()));
    /**
     * Ensure the test infra is ready for test run, including creating the client.
     */
//    @BeforeClass
//    public void setup() {
//        super.setup();
//    }

    @BeforeMethod
    public void setupTest() {
        bookId = "MT01_RemoveBookTest_" + UUID.randomUUID();
        // sysout because Hydra doesn't support log4j -> CloudWatch out of the box
        // https://tiny.amazon.com/1hfswiy1t/wamazbinviewHydrFAQ
        System.out.println("Executing RemoveBook test with bookId " + bookId);
    }

    @Test
    public void removeBook_activeCatalogItem_setsToInactive() {
        // GIVEN
        CatalogItemVersion catalogItemVersion = new CatalogItemVersion();
        catalogItemVersion.setBookId(bookId);
        catalogItemVersion.setVersion(1);
        catalogItemVersion.setAuthor("author");
        catalogItemVersion.setGenre(BookGenre.ACTION);
        catalogItemVersion.setText("text");
        catalogItemVersion.setTitle("title");
        catalogItemVersion.setInactive(false);
        super.getTestDao().save(catalogItemVersion);

        RemoveBookFromCatalogRequest removeBookFromCatalogRequest = new RemoveBookFromCatalogRequest();
        removeBookFromCatalogRequest.setBookId(bookId);

        // WHEN
        super.kindlePublishingServiceClient.newRemoveBookFromCatalogCall()
            .call(removeBookFromCatalogRequest);

        // THEN
        CatalogItemVersion result = super.getTestDao().load(catalogItemVersion);

        assertTrue(result.isInactive(), String.format("Expected catalog item [bookId: %s, version: %s] to be set to " +
            "inactive after the calling the RemoveBook API, but got [%s]",
            catalogItemVersion.getBookId(),
            catalogItemVersion.getVersion(),
            result));
        assertGetBookRequestThrowsKindlePublishingClientException();
    }

    @Test
    public void removeBook_multipleCatalogItemVersion_marksLatestVersionInactive() {
        // GIVEN a first, inactive version
        CatalogItemVersion firstVersion = new CatalogItemVersion();
        firstVersion.setBookId(bookId);
        firstVersion.setVersion(1);
        firstVersion.setAuthor("author");
        firstVersion.setGenre(BookGenre.ACTION);
        firstVersion.setText("text");
        firstVersion.setTitle("title");
        firstVersion.setInactive(true);
        super.getTestDao().save(firstVersion);
        // and a second, active version
        CatalogItemVersion secondVersion = new CatalogItemVersion(firstVersion);
        secondVersion.setVersion(2);
        secondVersion.setInactive(false);
        super.getTestDao().save(secondVersion);

        RemoveBookFromCatalogRequest removeBookFromCatalogRequest = new RemoveBookFromCatalogRequest();
        removeBookFromCatalogRequest.setBookId(bookId);

        // WHEN we remove the catalog item
        super.kindlePublishingServiceClient.newRemoveBookFromCatalogCall()
            .call(removeBookFromCatalogRequest);

        // THEN it should only update the second version
        CatalogItemVersion savedSecondVersion = super.getTestDao().load(secondVersion);
        assertTrue(savedSecondVersion.isInactive(), String.format("Expected catalog item [bookId: %s, version: %s]" +
                " to be updated to inactive after calling the RemoveBook API, but got [%s]",
            secondVersion.getBookId(),
            secondVersion.getVersion(),
            savedSecondVersion));

        // and not update the first version
        CatalogItemVersion savedFirstVersion = super.getTestDao().load(firstVersion);
        assertEquals(savedFirstVersion, firstVersion, String.format("Expected earlier catalog item version" +
                "[bookId: %s, version: %s] to not be updated after calling the RemoveBook API.",
            firstVersion.getBookId(),
            firstVersion.getVersion()));

        assertGetBookRequestThrowsKindlePublishingClientException();
    }

    @Test
    public void removeBook_inactiveCatalogItem_throwsKindlePublishingClientException() {
        // GIVEN
        CatalogItemVersion catalogItemVersion = new CatalogItemVersion();
        catalogItemVersion.setBookId(bookId);
        catalogItemVersion.setVersion(1);
        catalogItemVersion.setInactive(true);
        super.getTestDao().save(catalogItemVersion);

        RemoveBookFromCatalogRequest removeBookFromCatalogRequest = new RemoveBookFromCatalogRequest();
        removeBookFromCatalogRequest.setBookId(bookId);

        // WHEN + THEN
        assertThrows(KindlePublishingClientException.class, () ->
            super.kindlePublishingServiceClient.newRemoveBookFromCatalogCall()
                .call(removeBookFromCatalogRequest));
    }

    @Test
    public void removeBook_itemDoesNotExist_throwsKindlePublishingClientException() {
        // GIVEN
        RemoveBookFromCatalogRequest removeBookFromCatalogRequest = new RemoveBookFromCatalogRequest();
        removeBookFromCatalogRequest.setBookId(UUID.randomUUID().toString());

        // WHEN + THEN
        assertThrows(KindlePublishingClientException.class, () ->
            super.kindlePublishingServiceClient.newRemoveBookFromCatalogCall()
                .call(removeBookFromCatalogRequest));
    }

    private void assertGetBookRequestThrowsKindlePublishingClientException() {
        GetBookRequest getBookRequest = new GetBookRequest();
        getBookRequest.setBookId(bookId);
        assertThrows(KindlePublishingClientException.class, () ->
            super.kindlePublishingServiceClient.newGetBookCall().call(getBookRequest));
    }
}
