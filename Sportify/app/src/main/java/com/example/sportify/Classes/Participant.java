package com.example.sportify.Classes;

public class Participant {
    public String email;
    String eventid;

    public Participant() {}

    public Participant( String email, String eventId) {
        this.email = email;
        this.eventid = eventId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventId) {
        this.eventid = eventId;
    }

    public String toString() {
        return this.email;
    }
}
