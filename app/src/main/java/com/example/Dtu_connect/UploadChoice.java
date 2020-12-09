package com.example.Dtu_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UploadChoice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_choice);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.add);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Feedspage.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile: {
                        ParseQuery<ParseObject> query = new ParseQuery("InFo");
                        query.whereEqualTo("email", ParseUser.getCurrentUser().getEmail());

                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if(e==null)
                                {
                                    if(objects!=null)
                                    {
                                        if(objects.size()>0)
                                        {

                                            for(ParseObject object:objects)
                                            {
                                                String email = object.getString("email");
                                                Toast.makeText(getApplicationContext(), ""+ email, Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(getApplicationContext(),About.class);
                                                intent.putExtra("user",email);
                                                startActivity(intent);
                                                overridePendingTransition(0,0);
                                            }
                                        }

                                    }
                                }
                            }
                        });

                        return true;
                    }
                    case R.id.users:
                        startActivity(new Intent(getApplicationContext(),UserList.class));
                        overridePendingTransition(0,0);
                        return true;
                }


                return false;
            }
        });


        ImageButton notes = findViewById(R.id.note);
        ImageButton image = findViewById(R.id.image);
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(),UploadNotes.class);
                startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(),Add.class);
                startActivity(intent);
            }
        });
    }
}