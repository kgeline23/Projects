package com.example.bubbletea.Classes;


public class Type {
    public String name,  id;

    public Type(String name) {
        this.name = name;
    }

    public Type(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Type() {
    }
    //to display object as a string in spinner
    @Override
    public String toString() {
        return name;
    }
}