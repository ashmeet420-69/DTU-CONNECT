package com.example.Dtu_connect;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.Dtu_connect.ui.main.SectionsPagerAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Feedspage extends AppCompatActivity {


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.logout:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;

            default: return false;

        }
    }

    UserData userData = new UserData();

   // String email;
    public String getUserdata()
    {
        final String[] email = new String[1];

        return email[0];

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedspage);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(),UploadChoice.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile: {


                       // String email = getUserdata();
                       // intent.putExtra("user",email);



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



        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }
}