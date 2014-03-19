package uk.ac.lancaster.wave.Data.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "Book")
public class Book implements Serializable {
    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public long timestamp;

    @DatabaseField(index = true)
    public String title;

    @DatabaseField
    public String author;

    @DatabaseField
    public String cover;

    public Book() { }
    public Book(String id, long timestamp, String title, String author, String cover) {
        this.id = id;
        this.timestamp = timestamp;
        this.title = title;
        this.author = author;
        this.cover = cover;
    }
}
