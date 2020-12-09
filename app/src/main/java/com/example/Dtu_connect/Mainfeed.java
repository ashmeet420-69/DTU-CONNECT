package com.example.Dtu_connect;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Dtu_connect.Adapters.PhotoAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.parse.Parse.getApplicationContext;


public class Mainfeed extends ListFragment {

    ArrayList<Date>postdate= new ArrayList<>();
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> caption=new ArrayList<>();
    ArrayList<String> photo =new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    ArrayList<ArrayList<String>> likeby =new ArrayList<ArrayList<String>>();
    ArrayList<Integer> likes = new ArrayList<>();
    ArrayList<ArrayList<String>> commentBy=new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> comment =new ArrayList<ArrayList<String>>();
    public static ListView Feedlist;
    public static PhotoAdapter adapter1;

    public Mainfeed() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_mainfeed, container, false);


        if(ParseUser.getCurrentUser()==null)
        {
            Toast.makeText(getContext(),"You are Logout Please Signin back",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(),LoginActivity.class));
        }

        if(ParseUser.getCurrentUser().get("isFollowing")==null)
        {
            List<String> emptyList = new ArrayList<>();
            ParseUser.getCurrentUser().put("isFollowing",emptyList);
        }


        ParseQuery<ParseObject> query =new ParseQuery<ParseObject>("Image");

       // query.whereContainedIn("username",ParseUser.getCurrentUser().getList("isFollowing"));
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null)
                {

                    if(objects.size()>0)
                    {

                        for(ParseObject object : objects)
                        {
                            ParseFile file=(ParseFile)object.getParseFile("image");
                            photo.add(file.getUrl());


                            postdate.add(object.getDate("postdate"));
                            name.add(object.getString("username"));
                            caption.add(object.getString("Content"));
                            id.add(object.getObjectId());
                            likes.add(object.getInt("likes"));
                            ArrayList<String> testStringArrayList = (ArrayList<String>)object.get("likeby");
                            likeby.add(testStringArrayList);
                            ArrayList<String> testStringArrayList1 = (ArrayList<String>)object.get("commentBy");
                            commentBy.add(testStringArrayList1);
                            ArrayList<String> testStringArrayList2 = (ArrayList<String>)object.get("comment");
                            comment.add(testStringArrayList2);
                            // Toast.makeText(getApplicationContext(),object.getObjectId(),Toast.LENGTH_SHORT).show();

//                            Toast.makeText(getApplicationContext(),String.valueOf(id.size())+id.get(0),Toast.LENGTH_SHORT).show();
                            adapter1.notifyDataSetChanged();
                        }
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"ERROR : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });



        adapter1 = new PhotoAdapter(getActivity(),photo,postdate,name,caption,id,likes,likeby,commentBy,comment);
        setListAdapter(adapter1);
        return view;
    }
}