package com.example.myapplication;

public class OverRatedSong {
    private String name;
    private String artist;
    private int grade;
    private String gang;

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setGang(String gang) {
        this.gang = gang;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public int getGrade() {
        return grade;
    }

    public String getGang() {
        return gang;
    }

    public OverRatedSong(String name, String artist, int grade, String gang) {
        this.name = name;
        this.artist = artist;
        this.grade = grade;
        this.gang = gang;
    }

    public OverRatedSong() {
        this.name = "";
        this.artist = "";
        this.gang = "";
        this.grade = 0;
    }
}
