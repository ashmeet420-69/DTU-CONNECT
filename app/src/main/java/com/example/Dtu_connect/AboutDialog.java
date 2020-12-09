package com.example.Dtu_connect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static com.example.Dtu_connect.About.edit;

public class AboutDialog extends DialogFragment {

    UserData userData;
    public AboutDialog(UserData userData)
    {
        this.userData=userData;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.aboutdialog,null);

        final TextView name = (TextView)view.findViewById(R.id.name);
        final TextView branch = (TextView)view.findViewById(R.id.branch);
        final TextView batch = (TextView)view.findViewById(R.id.batch);
        final TextView des = (TextView)view.findViewById(R.id.des);

        name.setText(userData.name);
        branch.setText(userData.branch);
        batch.setText(userData.batch);
        des.setText(userData.discription);


        builder.setView(view)
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
                            final String Name = name.getText().toString();
                            final String Branch = branch.getText().toString();
                            final String Batch = batch.getText().toString();
                            final String Des = des.getText().toString();

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
                                                    object.put("name",Name);
                                                    object.put("Branch",Branch);
                                                    object.put("Batch",Batch);
                                                    object.put("des",Des);
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
