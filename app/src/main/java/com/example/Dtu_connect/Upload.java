package com.example.Dtu_connect;

import java.util.ArrayList;
import java.util.Date;

public class Upload {

    public String id;
    public String name;
    public Date timestamp;
    public String course;
    public String uploaderMail;
    public String semester;
    public String branch;
    public String unit;
    public String url;
    public String author;
    public int download;
    public ArrayList<String> tags;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String course, String semester, String branch, String unit,String author, int download, String url, Date timestamp, String mail, ArrayList tags,String id) {
        this.id=id;
        this.name = name;
        this.course = course;
        this.semester = semester;
        this.branch = branch;
        this.unit = unit;
        this.author = author;
        this.download = download;
        this.url = url;
        this.timestamp = timestamp;
        this.uploaderMail = mail;
        this.tags = tags;
    }

    public String getid() {
        return id;
    }
    public String getCourse() {
        return course;
    }

    public String getSemester() {
        return semester;
    }

    public String getBranch() {
        return branch;
    }

    public String getUnit() {
        return unit;
    }

    public String getAuthor() {
        return author;
    }

    public int getDownload() {
        return download;
    }

    public String getName() {
        return name;
    }

    public String getUploaderMail() {
        return uploaderMail;
    }

    public String getUrl() {
        return url;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public ArrayList<String> getTags() {
        return tags;
    }
}
