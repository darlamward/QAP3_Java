package com.keyin.utils;

import java.util.Date;
public class BooksCheckedOut {
    private int checkout_id;
    private int book_id;
    private int patron_id;
    private Date return_date;
    private Date checkout_date;
    private Date due_date;


    public BooksCheckedOut(int checkout_id, int book_id, int patron_id, Date return_date, Date checkout_date,
                           Date due_date) {
        this.checkout_id = checkout_id;
        this.book_id = book_id;
        this.patron_id = patron_id;
        this.return_date = return_date;
        this.checkout_date = checkout_date;
        this.due_date = due_date;

    }

    @Override
    public String toString() {
        return  "Book ID: " + book_id +
                ", Patron ID: " + patron_id +
                ", Return Date: " + return_date +
                ", Checkout Date: " + checkout_date +
                ", Due Date: " + due_date;
    }
}
