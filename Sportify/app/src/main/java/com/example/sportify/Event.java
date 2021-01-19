package com.example.sportify;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    private double lat;
    private double lon;
    private String eventName;
    public String eventCategory;
    private String creator;

    public String id;

    public Event(){}

    public Event(double lat, double lon, String eventName, String eventCategory, String ct) {
        this.lat = lat;
        this.lon = lon;
        this.eventName = eventName;
        this.eventCategory = eventCategory;
        this.creator = ct;
    }

    protected Event(Parcel in) {
        lat = in.readDouble();
        lon = in.readDouble();
        eventName = in.readString();
        eventCategory = in.readString();
        creator = in.readString();
        id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(eventName);
        dest.writeString(eventCategory);
        dest.writeString(creator);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public String getCreator(){return  creator;}


}
