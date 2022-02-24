package com.amazon.ata.kindlepublishingservice;

import com.amazon.ata.kindlepublishingservice.activity.GetBookActivity;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishTask;

public class Main {
    public static void main(String[] args) {
        Runnable bookPublishTask = new BookPublishTask();
        Thread thread = new Thread(bookPublishTask);
        thread.start();
    }
}
