package com.example.Dtu_connect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.Dtu_connect.Adapters.UserAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        final ArrayList<UserData> users = new ArrayList<>();


        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("InFo");


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
                                UserData userData = new UserData();
                                ParseFile file=(ParseFile)object.getParseFile("image");
                                userData.image=file;
                                userData.name=  object.getString("name");
                                // Toast.makeText(getApplicationContext(), ""+userData.name, Toast.LENGTH_SHORT).show();
                                userData.email = object.getString("email");
                                userData.batch = object.getString("Batch");
                                userData.branch = object.getString("Branch");
                                userData.discription=object.getString("des");
                                userData.language = (ArrayList<String>) object.get("language");
                                userData.skill = (ArrayList<String>) object.get("skill");
                                userData.projectname=(ArrayList<String>)object.get("project");
                                userData.projecturl=(ArrayList<String>)object.get("projecturl");
                                // email = object.getString("email");
                                users.add(userData);

                            }
                            RecyclerView rv = findViewById(R.id.userlist);
                            UserAdapter userAdapter = new UserAdapter(UserList.this,users);
                            RecyclerView.LayoutManager manager = new LinearLayoutManager(getParent());
                            rv.setLayoutManager(manager);
                            rv.addItemDecoration(new DividerItemDecoration(getBaseContext(), LinearLayoutManager.VERTICAL));
                            rv.setAdapter(userAdapter);

                        }

                    }
                }
            }
        });

        Toast.makeText(getApplicationContext(), ""+users.size(), Toast.LENGTH_SHORT).show();

    }

}