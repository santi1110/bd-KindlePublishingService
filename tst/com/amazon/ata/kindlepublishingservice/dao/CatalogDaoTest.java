package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CatalogDaoTest {

    @Mock
    private PaginatedQueryList<CatalogItemVersion> list;

    @Mock
    private DynamoDBMapper dynamoDbMapper;

    @InjectMocks
    private CatalogDao catalogDao;

    @BeforeEach
    public void setup(){
        initMocks(this);
    }

    @Test
    public void getBookFromCatalog_bookDoesNotExist_throwsException() {
        // GIVEN
        String invalidBookId = "notABookID";
        when(dynamoDbMapper.query(eq(CatalogItemVersion.class), any(DynamoDBQueryExpression.class))).thenReturn(list);
        when(list.isEmpty()).thenReturn(true);

        // WHEN && THEN
        assertThrows(BookNotFoundException.class, () -> catalogDao.getBookFromCatalog(invalidBookId),
            "Expected BookNotFoundException to be thrown for an invalid bookId.");
    }

    @Test
    public void removeBookFromCatalog_bookAlreadyDeleted_throwsException() {
        // GIVEN
        String bookId = "book.123";
        CatalogItemVersion item = new CatalogItemVersion();
        item.setInactive(true);
        item.setBookId(bookId);
        item.setVersion(2);

        when(dynamoDbMapper.query(eq(CatalogItemVersion.class), any(DynamoDBQueryExpression.class))).thenReturn(list);
        when(list.isEmpty()).thenReturn(false);
        when(list.get(0)).thenReturn(item);

        // WHEN && THEN
        assertThrows(BookNotFoundException.class, () -> catalogDao.removeBookFromCatalog(bookId),
            "Expected BookNotFoundException to be thrown for an invalid bookId.");
    }

    @Test
    public void getBookFromCatalog_bookInactive_throwsException() {
        // GIVEN
        String bookId = "book.123";
        CatalogItemVersion item = new CatalogItemVersion();
        item.setInactive(true);
        item.setBookId(bookId);
        item.setVersion(1);

        when(dynamoDbMapper.query(eq(CatalogItemVersion.class), any(DynamoDBQueryExpression.class))).thenReturn(list);
        when(list.isEmpty()).thenReturn(false);
        when(list.get(0)).thenReturn(item);

        // WHEN && THEN
        assertThrows(BookNotFoundException.class, () -> catalogDao.getBookFromCatalog(bookId),
            "Expected BookNotFoundException to be thrown for an invalid bookId.");
    }

    @Test
    public void getBookFromCatalog_oneVersion_returnVersion1() {
        // GIVEN
        String bookId = "book.123";
        CatalogItemVersion item = new CatalogItemVersion();
        item.setInactive(false);
        item.setBookId(bookId);
        item.setVersion(1);
        ArgumentCaptor<DynamoDBQueryExpression> requestCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);

        when(dynamoDbMapper.query(eq(CatalogItemVersion.class), any(DynamoDBQueryExpression.class))).thenReturn(list);
        when(list.isEmpty()).thenReturn(false);
        when(list.get(0)).thenReturn(item);

        // WHEN
        CatalogItemVersion book = catalogDao.getBookFromCatalog(bookId);

        // THEN
        assertEquals(bookId, book.getBookId());
        assertEquals(1, book.getVersion(), "Expected version 1 of book to be returned");
        assertFalse(book.isInactive(), "Expected book to be active.");

        verify(dynamoDbMapper).query(eq(CatalogItemVersion.class), requestCaptor.capture());
        CatalogItemVersion queriedItem = (CatalogItemVersion) requestCaptor.getValue().getHashKeyValues();
        assertEquals(bookId, queriedItem.getBookId(), "Expected query to look for provided bookId");
        assertEquals(1, requestCaptor.getValue().getLimit(), "Expected query to have a limit set");
    }

    @Test
    public void getBookFromCatalog_twoVersions_returnsVersion2() {
        // GIVEN
        String bookId = "book.123";
        CatalogItemVersion item = new CatalogItemVersion();
        item.setInactive(false);
        item.setBookId(bookId);
        item.setVersion(2);
        ArgumentCaptor<DynamoDBQueryExpression> requestCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);

        when(dynamoDbMapper.query(eq(CatalogItemVersion.class), any(DynamoDBQueryExpression.class))).thenReturn(list);
        when(list.isEmpty()).thenReturn(false);
        when(list.get(0)).thenReturn(item);

        // WHEN
        CatalogItemVersion book = catalogDao.getBookFromCatalog(bookId);

        // THEN
        assertEquals(bookId, book.getBookId());
        assertEquals(2, book.getVersion(), "Expected version 2 of book to be returned");
        assertFalse(book.isInactive(), "Expected book to be active.");

        verify(dynamoDbMapper).query(eq(CatalogItemVersion.class), requestCaptor.capture());
        CatalogItemVersion queriedItem = (CatalogItemVersion) requestCaptor.getValue().getHashKeyValues();
        assertEquals(bookId, queriedItem.getBookId(), "Expected query to look for provided bookId");
        assertEquals(1, requestCaptor.getValue().getLimit(), "Expected query to have a limit set");
    }

    @Test
    public void removeBookFromCatalog_bookDoesNotExist_throwsException() {
        // GIVEN
        String invalidBookId = "notABookID";
        when(dynamoDbMapper.query(eq(CatalogItemVersion.class), any(DynamoDBQueryExpression.class))).thenReturn(list);
        when(list.isEmpty()).thenReturn(true);
        ArgumentCaptor<DynamoDBQueryExpression> requestCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);

        // WHEN && THEN

        assertThrows(BookNotFoundException.class, () -> catalogDao.removeBookFromCatalog(invalidBookId),
            "Expected BookNotFoundException to be thrown for an invalid bookId.");
        verify(dynamoDbMapper).query(eq(CatalogItemVersion.class), requestCaptor.capture());
        CatalogItemVersion queriedItem = (CatalogItemVersion) requestCaptor.getValue().getHashKeyValues();
        assertEquals(invalidBookId, queriedItem.getBookId());
    }

    @Test
    public void removeBookFromCatalog_bookExists_bookMarkedInactive() {
        // GIVEN
        String bookId = "book.123";
        CatalogItemVersion item = new CatalogItemVersion();
        item.setInactive(false);
        item.setBookId(bookId);
        item.setVersion(1);
        ArgumentCaptor<DynamoDBQueryExpression> requestCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);

        when(dynamoDbMapper.query(eq(CatalogItemVersion.class), any(DynamoDBQueryExpression.class))).thenReturn(list);
        when(list.isEmpty()).thenReturn(false);
        when(list.get(0)).thenReturn(item);

        // WHEN
        catalogDao.removeBookFromCatalog(bookId);

        // THEN
        verify(dynamoDbMapper).query(eq(CatalogItemVersion.class), requestCaptor.capture());
        CatalogItemVersion queriedItem = (CatalogItemVersion) requestCaptor.getValue().getHashKeyValues();
        assertEquals(bookId, queriedItem.getBookId());

        verify(dynamoDbMapper).save(item);
        assertTrue(item.isInactive(), "Expected item to be inactive after call to remove.");
    }


    @Test
    public void validateBookId_bookExists_succeeds() {
        // GIVEN
        String bookId = "book.123";
        CatalogItemVersion itemV1 = new CatalogItemVersion();
        itemV1.setInactive(false);
        itemV1.setBookId(bookId);
        itemV1.setVersion(1);
        ArgumentCaptor<DynamoDBQueryExpression> requestCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);

        when(dynamoDbMapper.query(eq(CatalogItemVersion.class), any(DynamoDBQueryExpression.class))).thenReturn(list);
        when(list.isEmpty()).thenReturn(false);
        when(list.get(0)).thenReturn(itemV1);

        // WHEN
        catalogDao.validateBookExists(bookId);

        // THEN
        verify(dynamoDbMapper).query(eq(CatalogItemVersion.class), requestCaptor.capture());
        CatalogItemVersion queriedItem = (CatalogItemVersion) requestCaptor.getValue().getHashKeyValues();
        assertEquals(bookId, queriedItem.getBookId());
    }

    @Test
    public void validateBookId_bookDoesNotExist_throwsException() {
        String invalidBookId = "notABookID";
        when(dynamoDbMapper.query(eq(CatalogItemVersion.class), any(DynamoDBQueryExpression.class))).thenReturn(list);
        when(list.isEmpty()).thenReturn(true);
        ArgumentCaptor<DynamoDBQueryExpression> requestCaptor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);

        // WHEN && THEN
        assertThrows(BookNotFoundException.class, () -> catalogDao.validateBookExists(invalidBookId),
            "Expected BookNotFoundException to be thrown for an invalid bookId.");
        verify(dynamoDbMapper).query(eq(CatalogItemVersion.class), requestCaptor.capture());
        CatalogItemVersion queriedItem = (CatalogItemVersion) requestCaptor.getValue().getHashKeyValues();
        assertEquals(invalidBookId, queriedItem.getBookId());
    }

    @Test
    public void createOrUpdateBook_update_versionUpdated() {
        // GIVEN
        String bookId = "book.123";
        KindleFormattedBook request = KindleFormattedBook.builder()
            .withBookId(bookId)
            .withTitle("Title")
            .withAuthor("Author")
            .withGenre(BookGenre.FANTASY)
            .withText("This is a story.")
            .build();

        CatalogItemVersion itemV1 = new CatalogItemVersion();
        itemV1.setInactive(false);
        itemV1.setBookId(bookId);
        itemV1.setVersion(1);

        when(dynamoDbMapper.query(eq(CatalogItemVersion.class), any(DynamoDBQueryExpression.class))).thenReturn(list);
        when(list.isEmpty()).thenReturn(false);
        when(list.get(0)).thenReturn(itemV1);

        // WHEN
        CatalogItemVersion itemV2 = catalogDao.createOrUpdateBook(request);

        // THEN
        verify(dynamoDbMapper).save(itemV1);
        assertTrue(itemV1.isInactive(), "Expected item to be inactive after call to remove.");
        assertEquals(2, itemV2.getVersion(), "Expected book's version to be " +
            "updated.");
        assertEquals(itemV2.getBookId(), bookId, "Expected updated item to have same bookId");
        assertEquals(itemV2.getAuthor(), request.getAuthor(), "Expected author to match publish request author");
        assertEquals(itemV2.getTitle(), request.getTitle(), "Expected title to match publish request title");
        assertEquals(itemV2.getGenre(), request.getGenre(), "Expected genre to match publish request genre");
        assertEquals(itemV2.getText(), request.getText(), "Expected text to match publish request text");
    }

    @Test
    public void createOrUpdateBook_updateUnknownBook_exceptionThrown() {
        // GIVEN
        String invalidBookId = "invalid";
        KindleFormattedBook request = KindleFormattedBook.builder()
            .withBookId(invalidBookId)
            .withTitle("Title")
            .withAuthor("Author")
            .withGenre(BookGenre.FANTASY)
            .withText("This is a story.")
            .build();

        when(dynamoDbMapper.query(eq(CatalogItemVersion.class), any(DynamoDBQueryExpression.class))).thenReturn(list);
        when(list.isEmpty()).thenReturn(true);

        // WHEN && THEN
        assertThrows(BookNotFoundException.class, () -> catalogDao.createOrUpdateBook(request), "Expected " +
            "exception when trying to update a book that does not exist.");
    }

    @Test
    public void createOrUpdateBook_create_bookSaved() {
        // GIVEN
        KindleFormattedBook request = KindleFormattedBook.builder()
            .withTitle("Title")
            .withAuthor("Author")
            .withGenre(BookGenre.FANTASY)
            .withText("This is a story.")
            .build();

        // WHEN
        CatalogItemVersion itemV1 = catalogDao.createOrUpdateBook(request);

        // THEN
        verify(dynamoDbMapper).save(any(CatalogItemVersion.class));
        assertEquals(1, itemV1.getVersion(), "Expected book's version to be set to 1.");
        assertNotNull(itemV1.getBookId(), "Expected saved book to have an id.");
        assertEquals(itemV1.getAuthor(), request.getAuthor(), "Expected author to match publish request author");
        assertEquals(itemV1.getTitle(), request.getTitle(), "Expected title to match publish request title");
        assertEquals(itemV1.getGenre(), request.getGenre(), "Expected genre to match publish request genre");
        assertEquals(itemV1.getText(), request.getText(), "Expected text to match publish request text");
    }
}
