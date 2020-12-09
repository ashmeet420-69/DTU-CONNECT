package com.example.Dtu_connect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

import java.util.List;

import static com.example.Dtu_connect.About.edit;

public class SkillDialog extends DialogFragment {
    UserData userData;
    public SkillDialog(UserData userData)
    {
        this.userData=userData;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.skilldialog,null);

       //  GridView langgrid = (GridView) view.findViewById(R.id.langgrid);
        GridView langgrid = new GridView(getContext());
        langgrid.setNumColumns(2);
        Language_SkillAdapter adapter=new Language_SkillAdapter(getActivity(), android.R.layout.simple_list_item_1, userData.skill);
        langgrid.setAdapter(adapter);


        builder.setView(langgrid)
                .setTitle("")
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
                                                    object.put("language",Language_SkillAdapter.data);
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
}

