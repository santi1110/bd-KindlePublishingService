package com.amazon.ata.kindlepublishingservice.controllers;

import com.amazon.ata.kindlepublishingservice.*;
import com.amazon.ata.kindlepublishingservice.activity.GetBookActivity;
import com.amazon.ata.kindlepublishingservice.activity.GetPublishingStatusActivity;
import com.amazon.ata.kindlepublishingservice.activity.RemoveBookFromCatalogActivity;
import com.amazon.ata.kindlepublishingservice.activity.SubmitBookForPublishingActivity;
import com.amazon.ata.kindlepublishingservice.dagger.ApplicationComponent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class Controller {
    private static final ApplicationComponent component = App.component;

    @GetMapping(value = "/publishing_status/{id}", produces = {"application/json"})
    public ResponseEntity<?> getPublishingStatus(@PathVariable String id) {
        GetPublishingStatusActivity publishingStatusActivity = component.provideGetPublishingStatusActivity();
        GetPublishingStatusRequest publishingStatusRequest = GetPublishingStatusRequest
                .builder().withPublishingRecordId(id).build();
        return new ResponseEntity<>(publishingStatusActivity.handleRequest(publishingStatusRequest, null), HttpStatus.OK);
    }

    @GetMapping(value = "/books/{id}", produces = {"application/json"})
    public ResponseEntity<?> getBook(@PathVariable String id) {
        GetBookActivity bookActivity = component.provideGetBookActivity();
        GetBookRequest getBookRequest = GetBookRequest.builder().withBookId(id).build();
        return new ResponseEntity<>(bookActivity.handleRequest(getBookRequest, null), HttpStatus.OK);
    }

    @DeleteMapping(value = "/books/{id}")
    public ResponseEntity<?> removeBook(@PathVariable String id) {
        RemoveBookFromCatalogActivity removeBookFromCatalogActivity = component.provideRemoveBookFromCatalogActivity();
        RemoveBookFromCatalogRequest removeBookFromCatalogRequest = RemoveBookFromCatalogRequest.builder()
                .withBookId(id).build();
        removeBookFromCatalogActivity.handleRequest(removeBookFromCatalogRequest, null);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/books", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> submitBookForPublishing(@Valid @RequestBody Book book) {
        SubmitBookForPublishingActivity submitBookForPublishingActivity = component.provideSubmitBookForPublishingActivity();
        SubmitBookForPublishingRequest submitBookForPublishingRequest = SubmitBookForPublishingRequest
                .builder()
                .withBookId(book.getBookId())
                .withAuthor(book.getAuthor())
                .withGenre(book.getGenre())
                .withText(book.getText())
                .withTitle(book.getTitle())
                .build();
        SubmitBookForPublishingResponse response = submitBookForPublishingActivity.handleRequest(submitBookForPublishingRequest, null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
