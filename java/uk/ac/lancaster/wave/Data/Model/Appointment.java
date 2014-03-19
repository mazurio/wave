package uk.ac.lancaster.wave.Data.Model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable(tableName = "Appointment")
public class Appointment implements Serializable {
    public static final String REQUEST = "REQUESTED";
    public static final String CONFIRM = "CONFIRMED";
    public static final String CANCEL = "CANCELED";

    @DatabaseField(id = true)
    public long timestamp;

    @DatabaseField(dataType = DataType.LONG_STRING)
    public String title;

    @DatabaseField(dataType = DataType.LONG_STRING)
    public String description;

    @DatabaseField(dataType = DataType.STRING)
    public String status;

    @DatabaseField(dataType = DataType.STRING)
    public String sender;

    @DatabaseField(dataType = DataType.STRING)
    public String receiver;

    @DatabaseField
    public long date;

    public Appointment() { }
    public Appointment(
            long timestamp,
            String title,
            String description,
            String status,
            String sender,
            String receiver,
            long date)
    {
        this.timestamp = timestamp;

        this.title = title;
        this.description = description;
        this.status = status;

        this.sender = sender;
        this.receiver = receiver;

        this.date = date;
    }
}