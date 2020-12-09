package com.example.Dtu_connect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Dtu_connect.Adapters.Language_SkillAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class About extends AppCompatActivity {

    UserData userData;
    Bitmap bitmap;
    ProgressBar progressBar;
    public static  boolean edit=false ;

    public ImageView profilepic;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1&&resultCode==RESULT_OK&&data!=null)
        {
            Uri uri=data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                profilepic.setImageBitmap(bitmap);

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Confirmation")
                        .setMessage("Do You Want To Upload This As Your Profile Picture")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                progressBar.setVisibility(View.VISIBLE);
                                ByteArrayOutputStream stream =new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                                byte[] byteArray = stream.toByteArray();

                                final ParseFile file = new ParseFile("profileimage.png",byteArray);
                                file.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {

                                        if (e != null) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
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

                                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("InFo");
                                query.whereEqualTo("email",ParseUser.getCurrentUser().getEmail());
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if(e==null)
                                        {
                                            if(objects!=null)
                                            {
                                                if(objects.size()>0)
                                                {
                                                    for (ParseObject object:objects)
                                                    {
                                                        object.put("image",file);
                                                        object.saveInBackground(new SaveCallback() {
                                                            @Override
                                                            public void done(ParseException e) {
                                                                if(e==null)
                                                                {
                                                                    Toast.makeText(About.this, "Profile image updated ", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });

                            }
                        }).show();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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



    private void getUserData(String email) {
        userData = new UserData();

        ParseQuery<ParseObject> query = new ParseQuery("InFo");
        query.whereEqualTo("email", email);

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
                                ParseFile file=(ParseFile)object.getParseFile("image");
                                userData.image=file;
                                Picasso.with(getApplicationContext()).load(userData.image.getUrl()).into(profilepic);
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
                            }
                        }

                    }
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(),UploadChoice.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home: {
                        startActivity(new Intent(getApplicationContext(),Feedspage.class));
                        overridePendingTransition(0,0);
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








        progressBar = findViewById(R.id.ProfileProgressBar);
        TextView change = findViewById(R.id.image);
        profilepic = (ImageView)findViewById(R.id.imageView3);
        Intent intent = getIntent();
        String email = intent.getStringExtra("user");
        getUserData(email);

        String emails = ParseUser.getCurrentUser().getEmail();
        Toast.makeText(this, ""+email+"  "+ emails, Toast.LENGTH_SHORT).show();
        if(emails.equals(email))
        {
            edit=true;
        }

        if(edit)
        {
            change.setVisibility(View.VISIBLE);
            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(About.this, "click", Toast.LENGTH_SHORT).show();

                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }
                    else{
                        getphoto();
                    }

                }
            });
            profilepic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }


        TextView about = findViewById(R.id.about);
        TextView lan = findViewById(R.id.language);
        TextView skill = findViewById(R.id.skill);
        TextView pro = findViewById(R.id.project);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutDialog dialog = new AboutDialog(userData);
                dialog.show(getSupportFragmentManager(),"");
            }
        });
        lan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               LanguageDialog dialog = new LanguageDialog(userData);
               dialog.show(getSupportFragmentManager(),"");
               // showAlertDialog();
            }
        });
        skill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SkillDialog dialog = new SkillDialog(userData);
                dialog.show(getSupportFragmentManager(),"");
            }
        });
        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectDialog dialog = new ProjectDialog(userData);
                dialog.show(getSupportFragmentManager(),"");
            }
        });

    }


}