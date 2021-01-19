package com.example.fhictcompanion;

import java.util.ArrayList;

public class Json {

    private String room, subject, tabv, start, end, description,classes;


    public Json(String room, String subject, String tabv, String start, String end, String description, String classes)
    {
        this.setClasses(classes);
        this.setDescription(description);
        this.setEnd(end);
        this.setStart(start);
        this.setSubject(subject);
        this.setTabv(tabv);
        this.setRoom(room);

    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTabv() {
        return tabv;
    }

    public void setTabv(String tabv) {
        this.tabv = tabv;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }
}
