package uk.ac.lancaster.wave.Data.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "Tag")
public class Tag implements Serializable {
    @DatabaseField(index = true, id = true)
    public String id;

    @DatabaseField(index = true)
    public String title;

    @DatabaseField
    public String description;

    @DatabaseField
    public String image;

    public Tag() { }
    public Tag(String id, String title, String description, String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
    }
}
