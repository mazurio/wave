package uk.ac.lancaster.wave.Networking.Events;

import uk.ac.lancaster.wave.Data.Model.Contact;

public class EventGetContactDone {
    public Contact contact;

    public EventGetContactDone(Contact contact) {
        this.contact = contact;
    }
}
