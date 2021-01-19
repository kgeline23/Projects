package com.example.fhictcompanion.Models;

import java.io.Serializable;

public class PeopleModel implements Serializable {

    private String name, id,email,office;
    String image;

    public PeopleModel(String id, String name,String email,String image, String office) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.email = email;
        this.office = office;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

