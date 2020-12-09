package com.example.Dtu_connect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.Dtu_connect.Adapters.Language_SkillAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.example.Dtu_connect.About.edit;

public class ProjectDialog extends DialogFragment {
    UserData userData;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        GridView gridView = new GridView(getContext());
        gridView.setAdapter(new Language_SkillAdapter(getContext(), android.R.layout.simple_list_item_1, userData.projectname));
        gridView.setNumColumns(2);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String url =null;
                if(userData.projecturl.size()-1>=position)
                {
                    url = userData.projecturl.get(position);
                }
                if(url!=null)
                {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }

            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                return false;
            }
        });

        // Set grid view to alertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(gridView)
                .setNegativeButton((edit) ? "Ok":"", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton((edit)?"Edit":"Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(edit)
                {

                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("InFo");
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
                                        for(ParseObject object :objects)
                                        {
                                            object.put("Project",Language_SkillAdapter.data);
                                            object.saveInBackground();
                                        }
                                    }
                                }
                            }
                        }
                    });
                }

            }
        });




        return builder.create();
    }

    public ProjectDialog(UserData userData)
    {
        this.userData=userData;
    }
}
