package com.example.Dtu_connect;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    EditText un,email,pass;

    public void intiinfo()
    {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.profile);
        ByteArrayOutputStream stream =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();

        ParseFile file = new ParseFile("profileimage.png",byteArray);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e!=null)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


        List<String> empty = new ArrayList<>();
        empty.add(" ");
        ParseObject object = new ParseObject("InFo");

        object.put("email",email.getText().toString());
        object.put("Branch","Not mention");
        object.put("Batch","Not mention");
        object.put("language",empty);
        object.put("skill",empty);
        object.put("project",empty);
        object.put("projecturl",empty);
        object.put("insta","");
        object.put("linkedin","");
        object.put("name",un.getText().toString());
        object.put("des","");

        object.put("image",file);

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();;
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Try again later", Toast.LENGTH_SHORT).show();;
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseInstallation.getCurrentInstallation().saveInBackground();
        un = (EditText)findViewById(R.id.un);
        email=(EditText)findViewById(R.id.unl);
        pass = (EditText)findViewById(R.id.pass);
        pass.setOnKeyListener(this);
        ConstraintLayout cls =(ConstraintLayout)findViewById(R.id.cls);
        ImageView ivs = (ImageView)findViewById(R.id.ivs);
        ivs.setOnClickListener(this);
        cls.setOnClickListener(this);
    }

    public void shift(View view)
    {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void signup(View view)
    {
        Toast.makeText(this,"pressed",Toast.LENGTH_SHORT).show();

        try {
            final ParseUser user = new ParseUser();
            user.setUsername( un.getText().toString());
            user.setEmail(email.getText().toString());
            user.setPassword(pass.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null)
                    {
                        intiinfo();
                        ParseUser.logOut();


                        user.put("branch","");
                        user.put("batch","");
                        user.put("language",new ArrayList<>());
                        user.put("skill",new ArrayList<>());
                        user.put("projectName",new ArrayList<>());
                        user.put("projectDes",new ArrayList<>());
                        user.put("projectUrl",new ArrayList<>());
                        user.put("insta","");
                        user.put("linkedin","");
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null)
                                {
                                    alertDisplayer("Account Created Successfully !","Please Verify Email ",false);
                                }

                            }
                        });
                    }
                    else {
                        ParseUser.logOut();
                        alertDisplayer("Fail to Creat account !",": "+e.getMessage(),true);
                    }
                }
            });

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void alertDisplayer(String title,String message, final boolean error){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if(!error) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

       if(keyCode==event.KEYCODE_ENTER&&event.getAction()==KeyEvent.ACTION_DOWN)
       {
           signup(v);
       }

        return false;
    }

    @Override
    public void onClick(View v) {

        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

    }
}
