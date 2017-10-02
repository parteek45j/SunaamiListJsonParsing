package com.example.parteek.sunaamilist;

/**
 * Created by Parteek on 10/2/2017.
 */

public class Event {
    String title;
    double magnitude;
    long time;
    String url;

    public Event() {
    }

    public Event(String title, double magnitude,long time,String url) {
        this.title = title;
        this.magnitude = magnitude;
        this.time=time;
        this.url=url;
    }

    public String getUrl() {
        return url;
    }

    public long getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public double getMagnitude() {
        return magnitude;
    }
}
