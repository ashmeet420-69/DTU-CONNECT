package com.example.Dtu_connect;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class notes extends Fragment {

    Spinner semester, branch, course, unit;
    TextView tv8,tv_title;
    ImageView imageView;
    Button viewnotes;
    public notes() {
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

        View view= inflater.inflate(R.layout.fragment_notes, container, false);
        tv8 = view.findViewById(R.id.textView11);
        imageView=view.findViewById(R.id.imageView11);
        semester = view.findViewById(R.id.spinnerSem2);
        branch = view.findViewById(R.id.spinnerBranch2);
        course = view.findViewById(R.id.spinnerCourse2);
        unit = view.findViewById(R.id.spinnerUnit2);

        viewnotes=view.findViewById(R.id.ViewNotes);
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



        viewnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String c = course.getSelectedItem().toString();
                final String u = unit.getSelectedItem().toString();
                final String s = semester.getSelectedItem().toString();
                final String b = branch.getSelectedItem().toString();


                Toast.makeText(getActivity(), ""+u, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),DownloadNotes.class);
                intent.putExtra("course",c);
                intent.putExtra("branch",b);
                intent.putExtra("semester",s);
                intent.putExtra("unit",u);
                startActivity(intent);
            }
        });



        return view;
    }
}