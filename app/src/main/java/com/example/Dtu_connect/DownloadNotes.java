package com.example.Dtu_connect;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Dtu_connect.Adapters.NotesAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DownloadNotes extends AppCompatActivity {

    public static final String EXTRA_COURSE = "course";
    public static final String EXTRA_BRANCH = "branch";
    public static final String EXTRA_SEMESTER = "semester";
    public static final String EXTRA_UNIT = "unit";
    public static ArrayList<Upload> uploadList;

    private RecyclerView recyclerView;
    NotesAdapter notesAdapter;

    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_notes);

        Intent intent = getIntent();
        final String receivedCourse = intent.getStringExtra(EXTRA_COURSE);
        final String receivedBranch = intent.getStringExtra(EXTRA_BRANCH);
        final String receivedSemester = intent.getStringExtra(EXTRA_SEMESTER);
        final String receivedUnit = intent.getStringExtra(EXTRA_UNIT);




        uploadList = new ArrayList<>();

        ParseQuery<ParseObject> query =new ParseQuery<ParseObject>("Notes");

        query.whereEqualTo("course",receivedCourse);
        query.whereEqualTo("branch",receivedBranch);
        query.whereEqualTo("unit",receivedUnit);
        query.whereEqualTo("semester",receivedSemester);
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

                                ArrayList<String> tag = (ArrayList<String>)object.get("tag");
                                ParseFile file=(ParseFile)object.getParseFile("notes");
                                Upload upload = new Upload(object.getString("filename"),object.getString("course"),object.getString("semester"),object.getString("branch"),object.getString("unit"),object.getString("authorname"),object.getInt("download"),file.getUrl(),object.getDate("updatedAt"),object.getString("email"),tag,object.getObjectId());
                                uploadList.add(upload);
                                if(uploadList.isEmpty()){
//                    Toast.makeText(getApplicationContext(),"No PDFs Found",Toast.LENGTH_LONG).show();
                                    Snackbar.make(findViewById(R.id.relativeshit),"No PDFs Found",Snackbar.LENGTH_LONG).show();
                                }

                                else
                                {
                                    Collections.reverse(uploadList);
                                    recyclerView = findViewById(R.id.downloadRecycler);
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(DownloadNotes.this));
                                    notesAdapter = new NotesAdapter(DownloadNotes.this, uploadList);
                                    recyclerView.setAdapter(notesAdapter);
                                }

                            }
                        }
                    }
                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view,menu);
        MenuItem searchItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Search by Name or Author");
        EditText searchedittext = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        searchedittext.setTextColor(Color.WHITE);
        searchedittext.setHintTextColor(Color.parseColor("#50F3F9FE"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    notesAdapter.getFilter().filter(newText);
                }
                catch (Exception e )
                {
                }
                return false;
            }
        });
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                return true;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}