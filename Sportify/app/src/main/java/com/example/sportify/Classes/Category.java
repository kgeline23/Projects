package com.example.sportify.Classes;

public class Category {

    private String id;
    public String name;

    public Category(String name) {
        this.name = name;
    }
    public Category(String name, String id) {
        this.name = name;
        this.id = id;
    }
    public Category(){}

    //to display object as a string in spinner
    @Override
    public String toString() {
        return name;
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
}
