package uk.ac.lancaster.wave.Data.Model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable(tableName = "Announcement")
public class Announcement implements Serializable {
    @DatabaseField(dataType = DataType.STRING)
    public String module;

    @DatabaseField(dataType = DataType.STRING)
    public String user;

    @DatabaseField(dataType = DataType.LONG_STRING)
    public String title;

    @DatabaseField(dataType = DataType.LONG_STRING)
    public String description;

    @DatabaseField(id = true)
    public long timestamp;

    public Announcement() { }
    public Announcement(String module, String user, String title, String description, long timestamp) {
        this.module = module;
        this.user = user;

        this.title = title;
        this.description = description;

        this.timestamp = timestamp;
    }
}
