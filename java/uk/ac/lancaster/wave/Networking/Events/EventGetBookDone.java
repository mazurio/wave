package uk.ac.lancaster.wave.Networking.Events;

import uk.ac.lancaster.wave.Data.Model.Book;

/**
 * Created by Mazur on 26/02/2014.
 */
public class EventGetBookDone {
    public Book book;

    public EventGetBookDone(Book book) {
        this.book = book;
    }
}
