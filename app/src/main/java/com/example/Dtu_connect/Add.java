package com.example.Dtu_connect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Add extends AppCompatActivity {

    Bitmap bitmap;
    TextView caption;

    ProgressBar progressBar;
    public void upload(View view)
    {

        view.setVisibility(View.INVISIBLE);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);

        ByteArrayOutputStream stream =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();

        ParseFile file = new ParseFile("image.png",byteArray);

        ParseObject object =new ParseObject("Image");
        List<String> emptyList = new ArrayList<>();
        object.put("commentBy",emptyList);
        //List<String> emptyList = new ArrayList<>();
        object.put("comment",emptyList);
        if(object.getList("likeby")==null)
        {
            List<String> emptyList1 = new ArrayList<>();
            object.put("likeby",emptyList1);
        }
        object.put("likes",0);
        object.put("image",file);
        object.put("username",ParseUser.getCurrentUser().getUsername());
        object.put("postdate",date);
        object.put("Content",String.valueOf(caption.getText()));
        progressBar.setVisibility(View.VISIBLE);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                } else
                    Toast.makeText(getApplicationContext(), "error try again", Toast.LENGTH_SHORT).show();
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer percentDone) {
                progressBar.setProgress(0);
                progressBar.setProgress(percentDone);
                if(percentDone==100)
                    progressBar.setVisibility(View.GONE);
            }
        });
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Toast.makeText(getApplicationContext(),"uploaded",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Feedspage.class));
                }

                else
                {
                    Toast.makeText(getApplicationContext(),"error try again",Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.setVisibility(View.VISIBLE);

    }

    public void getphoto(){
        Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1)
        {
            if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                getphoto();
            }
        }
    }

    @Override
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

        }}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("LET THE WORLD KNOW");
        caption = (TextView)findViewById(R.id.caption);

        progressBar = (ProgressBar)findViewById(R.id.progress);
        if(ParseUser.getCurrentUser()==null)
        {
            Toast.makeText(this,"You are Logout Please Signin back",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,LoginActivity.class));
        }

        Toast.makeText(this, "ADD", Toast.LENGTH_SHORT).show();

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


        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        else{
            getphoto();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1&&resultCode==RESULT_OK&&data!=null)
        {
            Uri uri=data.getData();

            try {
                 bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                ImageView iv = (ImageView)findViewById(R.id.iv);
                iv.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
