package com.example.Dtu_connect;

import android.net.Uri;

import com.parse.ParseFile;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class UserData implements Serializable {


    public ParseFile image;
    public String discription;
    public String branch;
    public String batch;
    public ArrayList<String> language;
    public ArrayList<String> skill;
    public ArrayList<String> projectname;
    public ArrayList<String> projecturl;
    public String email;
    public String name;

}
