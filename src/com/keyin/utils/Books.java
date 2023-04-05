package com.keyin.utils;
import java.util.Date;
public class Books {
    private int book_id;
    private String title;
    private int author_id;
    private String publisher;

    private  Date publishing_date;
    private String isbn;

    public Books(int book_id, String title, int author_id, String publisher, Date publishing_date, String isbn) {
        this.book_id = book_id;
        this.title = title;
        this.author_id = author_id;
        this.publisher = publisher;
        this.publishing_date = publishing_date;
        this.isbn = isbn;
    }

    public int getId() {
        return book_id;
    }

    public String getTitle() {
        return title;
    }


    public int getAuthor_id() {
        return author_id;
    }

    public String getPublisher() {
        return publisher;
    }

    public Date getPublishing_date() { return publishing_date; }

    public String getIsbn() {
        return isbn;
    }

    @Override
    public String toString() {
        return  "Book ID: " + book_id +
                ", Title: " + title + '\'' +
                ", Author ID: " + author_id +
                ", Publisher: '" + publisher + '\'' +
                ", Publish Date: " + publishing_date +
                ", ISBN: " + isbn + '\'';
    }
}