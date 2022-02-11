package com.amazon.ata.kindlepublishingservice;

import java.util.Objects;

public class GetBookRequest {
    private String bookId;

    public GetBookRequest(String bookId) {
        this.bookId = bookId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public GetBookRequest(Builder builder) {
        this.bookId = builder.bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetBookRequest that = (GetBookRequest) o;
        return Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }

    public static Builder builder() {return new Builder();}

    public static final class Builder {
        private String bookId;

        private Builder() {

        }

        public Builder withBookId(String bookIdToUse) {
            this.bookId = bookIdToUse;
            return this;
        }

        public GetBookRequest build() { return new GetBookRequest(this); }
    }
}
