package uk.ac.lancaster.wave.Data.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "Module")
public class Module implements Serializable {
    @DatabaseField(index = true, id = true)
    public String id;

    @DatabaseField
    public String title;

    public Module() { }
    public Module(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
