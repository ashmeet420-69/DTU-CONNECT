package com.example.Dtu_connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UploadNotes extends AppCompatActivity {

    final static int PICK_PDF_CODE = 2342;
    private Intent Data = null;
    private EditText fileName, author;
    Intent receiverdIntent;
    TextView tv8;
    ImageView imageView;
    String receivedAction;
    String receivedType;
    Uri recievedUri;
    //these are the views
    TextView textViewStatus;
    //    EditText editTextFilename,author;
    ProgressBar progressBar;
    Button upload;
    Spinner semester, branch, course, unit;
    //the firebase objects for storage and database
   //-----------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notes);

        tv8 = findViewById(R.id.textView11);
        imageView=findViewById(R.id.imageView11);

        //getting firebase objects -------------------------

        /*tv_title = findViewById(R.id.tvtitle);
        tv_title.setText("Upload Notes");
        if(SaveSharedPreference.getCheckedItem(this)==0)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        else if(SaveSharedPreference.getCheckedItem(this)==1)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else if(SaveSharedPreference.getCheckedItem(this)==2)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
*/
        //getting the views
        textViewStatus = findViewById(R.id.textViewStatus);
        semester = findViewById(R.id.spinnerSem);
        branch = findViewById(R.id.spinnerBranch);
        course = findViewById(R.id.spinnerCourse);
        unit = findViewById(R.id.spinnerUnit);
        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(semester.getSelectedItem().toString().equals("Syllabus")){
                    unit.setVisibility(View.INVISIBLE);
                    tv8.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                    unit.setSelection(0);
                }
                else{
                    unit.setVisibility(View.VISIBLE);
                    tv8.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        editTextFilename = findViewById(R.id.FileName);
        progressBar =  findViewById(R.id.UploadNotesProgressBar);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        upload = findViewById(R.id.selectNotes);
        if(onSharedIntent()) {
            if (recievedUri!=null) {
                Log.i("Upload Notes", "onCreate: "+ recievedUri);
                UploadNotes.this.Data=receiverdIntent;
                UploadNotes.this.Data.setData(recievedUri);
                upload.setText("Upload");
            }
        }
//        author=findViewById(R.id.author);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPDF();

            }
        });

    }

    //this function will get the pdf from the storage
    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions(UploadNotes.this, new String[]{"Manifest.permission.READ_EXTERNAL_STORAGE"},100);
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                    Uri.parse("package:" + getPackageName()));
//            startActivity(intent);
//            return;
//        }
        if(UploadNotes.this.Data == null) {
            if (ContextCompat.checkSelfPermission(UploadNotes.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                // Requesting the permission
                ActivityCompat.requestPermissions(UploadNotes.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
            } else {

                //creating an intent for file chooser
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_CODE);
            }
        }
        else{
            StringBuilder filenaam = new StringBuilder();
            String str = recievedUri.getLastPathSegment();
            int slash =-1;
            if(str.contains("/")){
                slash = str.indexOf("/");
                filenaam.append(str.substring(slash,str.length()-1));
            }
            String str1  = str.substring(slash+1,str.length()-1);
            if(str1.contains(".")) {
                int dot = str1.indexOf(".");
                filenaam.append(str1.substring(0, dot));
            }
            alertDialog(filenaam.toString());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getPDF();

        } else {
            Toast.makeText(UploadNotes.this,
                    "Storage Permission Denied",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            StringBuilder filenaam = new StringBuilder();
            if (data.getData() != null) {
                String str = data.getData().getLastPathSegment();
                int slash =-1;
                if(str.contains("/")){
                    slash = str.indexOf("/");
                    filenaam.append(str.substring(slash,str.length()-1));
                }
                String str1  = str.substring(slash+1,str.length()-1);
                if(str1.contains(".")) {
                    int dot = str1.indexOf(".");
                    filenaam.append(str1.substring(0, dot));
                }

//                    editTextFilename.setText(str);
                //uploading the file
                UploadNotes.this.Data = data;
//                NotesDialog notesDialog = new NotesDialog();
//                notesDialog.show(getSupportFragmentManager(),"Notes Dialog");
                alertDialog(filenaam.toString());


//                findViewById(R.id.uploadNotes).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        uploadFile(data.getData());
//                    }
//                });

            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void alertDialog(String filenaam) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadNotes.this);

        LayoutInflater inflater = getLayoutInflater().from(UploadNotes.this);
        final View view = inflater.inflate(R.layout.layout_dialog_notes,null);



        builder.setView(view)
                .setTitle("Set File Name")
                .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UploadNotes.this.Data = null;
                    }
                });
        fileName = view.findViewById(R.id.fileName);
        author = view.findViewById(R.id.authorName);



        final AlertDialog dialog = builder.create();
        dialog.show();
        fileName.setText(filenaam);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                        boolean isError = false;
                String file = fileName.getText().toString();
                String authorName = author.getText().toString();
//                        isError=true;
                if(file.isEmpty() && authorName.isEmpty()){
                    fileName.setError("Filename cannot be empty");
                    author.setError("Author name cannot be empty");
                }
                else if(file.isEmpty()){
//                            isError = true;
                    fileName.setError("Filename cannot be empty");
                }
                else if(authorName.isEmpty()){
//                            isError = true;
                    author.setError("Author name cannot be empty");
                }
                else {
                    try {
                        applyTexts(file, authorName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    UploadNotes.this.Data = null;
                    dialog.dismiss();
                }
            }
        });
        fileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fileName.setError(null);
            }
        });
        author.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                author.setError(null);
            }
        });
    }


    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    //this method is uploading the file
    //the code is same as the previous tutorial
    //so we are not explaining it
    private void uploadFile(Uri data, final String filename, final String authorname) throws IOException {

        progressBar.setVisibility(View.VISIBLE);
        InputStream iStream =   getContentResolver().openInputStream(data);
        byte[] inputData = getBytes(iStream);


        ParseFile file = new ParseFile("notes.pdf",inputData);

        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e!=null)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer percentDone) {

                progressBar.setProgress(0);
                textViewStatus.setText("");
                progressBar.setProgress(percentDone);
                if(percentDone==100)
                    progressBar.setVisibility(View.GONE);
            }
        });

        ParseObject object =new ParseObject("Notes");
        object.put("notes",file);
        object.put("authorname",authorname);
        object.put("filename",filename);
        object.put("course",course.getSelectedItem().toString());
        object.put("branch",branch.getSelectedItem().toString());
        object.put("unit",unit.getSelectedItem().toString());
        object.put("semester",semester.getSelectedItem().toString());
        object.put("dowmload",0);
        object.put("email",ParseUser.getCurrentUser().getEmail());
        List<String> emptyList = new ArrayList<>();
        object.put("tag",emptyList);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e==null)
                {
                    textViewStatus.setText("File Uploaded Successfully");
                }
                else
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void applyTexts(String filename, String authorname) throws IOException {

        if (UploadNotes.this.Data.getData() == null){
            Toast.makeText(this, "Data is null", Toast.LENGTH_SHORT).show();
        }
        else
           uploadFile(UploadNotes.this.Data.getData(),filename,authorname);
    }
    public boolean onSharedIntent(){
        receiverdIntent = getIntent();
        Bundle bundle = receiverdIntent.getExtras();
        if(bundle!=null) {
            receivedAction = receiverdIntent.getAction();
            receivedType = receiverdIntent.getType();
            if (receiverdIntent != null) {
                Log.i("Upload Notes", "onSharedIntent: " + receivedType + "::::" + receivedAction);
                if (receivedType.contains("pdf")) {
                    recievedUri = receiverdIntent.getParcelableExtra(Intent.EXTRA_STREAM);
                    if (recievedUri != null) {
                        Log.i("Upload Notes", "onSharedIntent: " + recievedUri.toString());
                        return true;
                    }

                }
            }
        }
        return false;
    }
}