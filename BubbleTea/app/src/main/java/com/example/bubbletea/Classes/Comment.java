package com.example.bubbletea.Classes;

public class Comment {

    public String id, user_id, drink_id, better_temp, topping, extra;
    public boolean enough_sugar;

    public Comment(String user_id, String drink_id, String better_temp, String topping, String extra, boolean enough_sugar) {
        this.user_id = user_id;
        this.drink_id = drink_id;
        this.better_temp = better_temp;
        this.topping = topping;
        this.extra = extra;
        this.enough_sugar = enough_sugar;
    }

    public Comment(String user_id, String drink_id, String better_temp, String topping, boolean enough_sugar) {
        this.user_id = user_id;
        this.drink_id = drink_id;
        this.better_temp = better_temp;
        this.topping = topping;
        this.enough_sugar = enough_sugar;
    }

    public Comment(){}

    //to display object as a string in spinner
    @Override
    public String toString() {
        return user_id;
    }
}
