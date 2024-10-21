package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequest;
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
     *
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

    private CatalogItemVersion getLatestVersionOfBook(String bookId) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);  // Set the partition key (bookId)

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
                .withHashKeyValues(book)  // Query based on bookId
                .withScanIndexForward(false)  // Retrieve the latest version
                .withLimit(1);  // Only fetch the latest version

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);

        if (results == null || results.isEmpty()) {
            return null;  // No results found
        }

        return results.get(0);  // Return the latest version
    }

    public void removeBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }
        // Mark the book as inactive
        book.setInactive(true);

        // Save the updated book version
        dynamoDbMapper.save(book);
    }

    public void validateBookExists(String bookId) {
        try {
            CatalogItemVersion book = getLatestVersionOfBook(bookId);
            if (book == null) {
                throw new BookNotFoundException(String.format("Book with id %s not found.", bookId));
            }
        } catch (Exception e) {
            // Log the exception and rethrow it to capture more context
            System.err.println("Error while validating book with id: " + bookId);
            e.printStackTrace();
            throw new BookNotFoundException(String.format("Book with id %s not found due to an error.", bookId), e);

        }
    }

    public void publishBook(BookPublishRequest request) {
        try {
            // Retrieve the latest version of the book
            CatalogItemVersion latestVersion = getLatestVersionOfBook(request.getBookId());

            CatalogItemVersion newVersion = new CatalogItemVersion();
            newVersion.setBookId(request.getBookId());
            newVersion.setTitle(request.getTitle());
            newVersion.setAuthor(request.getAuthor());
            newVersion.setText(request.getText());
            newVersion.setGenre(request.getGenre());
            newVersion.setInactive(false);  // Mark as active

            // If the book already exists, increment the version number
            if (latestVersion != null) {
                newVersion.setVersion(latestVersion.getVersion() + 1);
                // Mark the old version as inactive
                latestVersion.setInactive(true);
                dynamoDbMapper.save(latestVersion); // Save the old version with inactive status
            } else {
                // This is the first version of the book
                newVersion.setVersion(1);
            }

            // Save the new version to DynamoDB
            dynamoDbMapper.save(newVersion);

        } catch (Exception e) {
            throw new RuntimeException("Failed to publish book", e);
        }
    }
}