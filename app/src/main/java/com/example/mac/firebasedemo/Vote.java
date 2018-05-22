package com.example.mac.firebasedemo;

import java.util.Date;
import java.util.HashMap;

public class Vote {
    private String song1;
    private String song2;
    private String song3;
    private Integer song1count;
    private Integer song2count;
    private Integer song3count;
    private String email;
    private long timestamp;

    public Vote() {
    }


    //All initialized variables for the Vote object
    //which we will then be updating
    public Vote(String song1, String song2, String song3) {
        this.song1 = song1;
        this.song2 = song2;
        this.song3 = song3;
        song1count = 0;
        song2count = 0;
        song3count = 0;
    }


    //Simple get functions to get the timestamp, each song
    //song count
    public long getTimestamp() {
        return timestamp;
    }
    public String getSong1() {
        return song1;
    }
    public String getSong2() {
        return song2;
    }
    public String getSong3() {
        return song3;
    }
    public Integer getSong1count() {
        return song1count;
    }
    public Integer getSong2count() {
        return song2count;
    }
    public Integer getSong3count() {
        return song3count;
    }


    //Setters for setting songs and song counts

    public void setSong1count(Integer song1count) {this.song1count= song1count;}
    public void setSong2count(Integer song2count) {this.song2count= song2count;}
    public void setSong3count(Integer song3count) {this.song3count= song3count;}
    public void setSong1(String song1)
    {
        this.song1 = song1;
    }
    public void setSong2(String song1)
    {
        this.song2 = song2;
    }
    public void setSong(String song1)
    {
        this.song3 = song3;
    }



}
