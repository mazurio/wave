package uk.ac.lancaster.wave.Networking.Events.Auth;

import uk.ac.lancaster.wave.Data.Model.User;

public class Authenticated {
    public User user;

    public Authenticated(User user) {
        this.user = user;
    }
}
