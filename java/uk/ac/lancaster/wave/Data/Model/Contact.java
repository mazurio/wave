package uk.ac.lancaster.wave.Data.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "Contact")
public class Contact implements Serializable {
    @DatabaseField(index = true, id = true)
    public String username;

    @DatabaseField
    public String photo;

    @DatabaseField
    public String balance;

    @DatabaseField
    public String firstname;

    @DatabaseField
    public String lastname;

    @DatabaseField
    public String description;
    public String readableDescription;

    @DatabaseField
    public String email;

    @DatabaseField
    public String phone;

    public Contact() { }
    public Contact(
            String username,
            String photo,
            String balance,
            String firstname,
            String lastname,
            String description,
            String email,
            String phone) {
        this.username = username;
        this.photo = photo;

        this.balance = balance;

        this.firstname = firstname;
        this.lastname = lastname;

        this.description = description;

        // Set first character of description to uppercase.
        //  - e.g. student becomes Student
        this.readableDescription = Character.toUpperCase(description.charAt(0)) + description.substring(1);

        this.email = email;
        this.phone = phone;
    }

    public String getDescription() {
        return Character.toUpperCase(description.charAt(0)) + description.substring(1);
    }
}