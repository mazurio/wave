package uk.ac.lancaster.wave.Networking.Events;

import uk.ac.lancaster.wave.Data.Model.Tag;

public class EventGetTagDone {
    public Tag tag;

    public EventGetTagDone(Tag tag) {
        this.tag = tag;
    }
}
