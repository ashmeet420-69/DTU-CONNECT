package com.example.Dtu_connect.Adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.Dtu_connect.R;

import java.util.ArrayList;

public class Language_SkillAdapter extends ArrayAdapter {

    public static ArrayList<String> data;
    private final Activity context;
    private int resource;

    public Language_SkillAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> data) {
        super(context, resource, data);
        this.data=data;
        this.context = (Activity) context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.grid,null,true);
        }

        Toast.makeText(context, ""+data.size(), Toast.LENGTH_SHORT).show();

        final String datas = getItem(position).toString();
        final TextView editText = convertView.findViewById(R.id.textView2);
        /*editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before,int count) {

            }
        } );*/
        if(datas.equals(" ")) {
            editText.setText("      +       ");
        }
        else
        editText.setText(datas);

        return convertView;
    }
}
