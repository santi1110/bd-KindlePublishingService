package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import javax.inject.Inject;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion getBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;
    }

    /**
     * Deactivates the current version of the book with the specified book id from the CatalogItemVersions table. All
     * versions of this book will now be inactive. Throws a BookNotFoundException if the latest version is not active or
     * no version is found.
     *
     * @param bookId The book id corresponding to the book to deactivate.
     */
    public void removeBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        markBookInactive(book);
    }

    /**
     * Creates a new active version of the provided book. If the book does not exist the version is 1. If a version of
     * the book exists in the catalog, the version will be one higher than the previous version. The previous version
     * will be marked as inactive. Each book will have only one active version at a time. Throws a BookNotFoundException
     * if an update is made to a nonexistent book.
     * @param formattedBook - contains all catalog information in the required kindle format
     * @return the book that is saved in the database
     */
    public CatalogItemVersion createOrUpdateBook(KindleFormattedBook formattedBook) {

        if (StringUtils.isNotEmpty(formattedBook.getBookId())) {
            return updateBook(formattedBook);
        }
        return createBook(formattedBook);
    }

    private CatalogItemVersion updateBook(KindleFormattedBook formattedBook) {

        CatalogItemVersion existingVersion = getLatestVersionOfBook(formattedBook.getBookId());
        if (existingVersion == null) {
            throw new BookNotFoundException(String.format("Unable to update a book with no " +
                "versions. bookId: %s", formattedBook.getBookId()));
        }

        CatalogItemVersion book = new CatalogItemVersion();

        book.setBookId(formattedBook.getBookId());

        book.setTitle(formattedBook.getTitle());
        book.setAuthor(formattedBook.getAuthor());
        book.setText(formattedBook.getText());
        book.setGenre(formattedBook.getGenre());

        book.setInactive(false);
        book.setVersion(existingVersion.getVersion() + 1);

        saveBookInCatalog(book);
        markBookInactive(existingVersion);

        return book;
    }

    private CatalogItemVersion createBook(KindleFormattedBook formattedBook) {

        CatalogItemVersion book = new CatalogItemVersion();

        book.setBookId(KindlePublishingUtils.generateBookId());

        book.setTitle(formattedBook.getTitle());
        book.setAuthor(formattedBook.getAuthor());
        book.setText(formattedBook.getText());
        book.setGenre(formattedBook.getGenre());

        book.setInactive(false);
        book.setVersion(1);

        saveBookInCatalog(book);

        return book;
    }

    private void markBookInactive(CatalogItemVersion book) {
        book.setInactive(true);
        saveBookInCatalog(book);
    }

    private CatalogItemVersion saveBookInCatalog(CatalogItemVersion book) {
        dynamoDbMapper.save(book);
        return book;
    }

    /**
     * Validates whether any version (active or not) exists with the provided book id in the catalog.
     *
     * @param bookId The book id to validate.
     * @throws BookNotFoundException if no version is found in the catalog for the given book id.
     */
    public void validateBookExists(String bookId) {
        CatalogItemVersion result = getLatestVersionOfBook(bookId);

        if (result == null) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }
    }

    // Returns null if no version exists for the provided bookId
    private CatalogItemVersion getLatestVersionOfBook(String bookId) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
            .withHashKeyValues(book)
            .withScanIndexForward(false)
            .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }
}
