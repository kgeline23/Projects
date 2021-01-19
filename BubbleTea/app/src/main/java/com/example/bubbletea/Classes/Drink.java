package com.example.bubbletea.Classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Drink implements Parcelable {

    public String name, id,  store_id, type_id, category_id, price;
    /*
    public enum TeaType{
        teapresso,
        milchTee,
        grünTee,
        schwarzTee,
        oolongTee,
        Teaccino,
        smoothie,
        soda
    }
     */

    public Drink( String name, String store_id, String category_id, String type_id, String price) {
        this.name = name;
        this.store_id = store_id;
        this.category_id = category_id;
        this.type_id = type_id;
        this.price = price;
    }

    public  Drink(){}

    protected Drink(Parcel in) {
        name = in.readString();
        id = in.readString();
        store_id = in.readString();
        type_id = in.readString();
        category_id = in.readString();
        price = in.readString();
    }

    public static final Creator<Drink> CREATOR = new Creator<Drink>() {
        @Override
        public Drink createFromParcel(Parcel in) {
            return new Drink(in);
        }

        @Override
        public Drink[] newArray(int size) {
            return new Drink[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(store_id);
        dest.writeString(type_id);
        dest.writeString(category_id);
        dest.writeString(price);
    }
}
