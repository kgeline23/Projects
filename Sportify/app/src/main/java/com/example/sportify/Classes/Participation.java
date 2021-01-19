package com.example.sportify.Classes;

import java.sql.Time;
import java.util.Date;

public class Participation {
    public String email, eventid, id;

    public Participation() {
    }

    public Participation(String id, String eventid, String email) {
        this.id = id;
        this.eventid = eventid;
        this.email = email;
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

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
