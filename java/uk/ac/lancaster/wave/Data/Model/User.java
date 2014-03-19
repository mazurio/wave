package uk.ac.lancaster.wave.Data.Model;

import java.io.Serializable;

public class User implements Serializable {
    public String username;
    public String photo;

    public String balance;

    public String firstname;
    public String lastname;

    public String description;

    public String email;
    public String phone;

    public User(
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

        this.email = email;
        this.phone = phone;
    }
}