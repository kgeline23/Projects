package com.example.bubbletea.Classes;

public class Category {
    public String name,  id;

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Category() {
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return name;
    }
}
