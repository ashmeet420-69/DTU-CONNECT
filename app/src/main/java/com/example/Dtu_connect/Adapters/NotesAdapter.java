package com.example.Dtu_connect.Adapters;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Dtu_connect.BuildConfig;
import com.example.Dtu_connect.R;
import com.example.Dtu_connect.Upload;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Upload> noteslist;
    private ArrayList<Upload> noteslistfull;
    private EditText answer;
    ParseUser user = ParseUser.getCurrentUser();
    String name;

    public NotesAdapter(Context context, ArrayList<Upload> noteslist) {
        this.context = context;
        this.noteslist = noteslist;
        noteslistfull = new ArrayList<>(noteslist);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notes,parent,false);
        return new NotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        //Set Values in card
        final Upload notes = noteslist.get(position);
        holder.title.setText(notes.getName());
        holder.author.setText(notes.getAuthor());
        holder.noOfDown.setText("No. of Downloads: " + String.valueOf(notes.getDownload()));
        name=notes.getName();

        ArrayList<String> selectedTags = new ArrayList<>();
        if (notes.getTags()!=null)
            selectedTags = (ArrayList<String>) notes.getTags().clone();

        //Tags Recycler View
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        TagsAdapter recyclerAdapter = new TagsAdapter(context, selectedTags);
        holder.recyclerView.setAdapter(recyclerAdapter);

        final ArrayList<String> finalSelectedTags = selectedTags;
        holder.itv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File("/storage/emulated/0/Android/data/"+ BuildConfig.APPLICATION_ID+"/files/Notes/Download Notes"+File.separator+notes.getName()+".pdf");
                if(file.exists()) {
                    openfile(file.getAbsolutePath());
                    Log.d("upload", "onClick: already exists");
                }
                else {
                    downloadfile(notes.getUrl(),notes.getName());
                    final int downloads = notes.getDownload() + 1;
                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Notes");
                    query.whereEqualTo("objectId",notes.id);

                   query.findInBackground(new FindCallback<ParseObject>() {
                       @Override
                       public void done(List<ParseObject> objects, ParseException e) {
                           if(e==null)
                           {
                               if(objects!=null)
                               {
                                   if(objects.size()>0)
                                   {
                                       Toast.makeText(context, ""+downloads+objects.size(), Toast.LENGTH_SHORT).show();
                                       for(ParseObject object :objects)
                                       {
                                           object.put("download",downloads);object.saveInBackground();
                                       }
                                   }
                               }
                           }
                           else {
                               Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                       }
                   });



                    Log.d("upload", "onClick: download");
                }


            }
        });


        final ArrayList<String> finalSelectedTags1 = selectedTags;
        final ArrayList<String> finalSelectedTags2 = selectedTags;
        final ArrayList<String> finalSelectedTags3 = selectedTags;
        final ArrayList<String> finalSelectedTags4 = selectedTags;
        final ArrayList<String> finalSelectedTags5 = selectedTags;
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(context,v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.notes_overflow, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.report: {
//                                Toast.makeText(context, "Report Notes", Toast.LENGTH_SHORT).show();
//                                ReportsDialog reportsDialog = new ReportsDialog(notes.getTimestamp());
//                                reportsDialog.show(((AppCompatActivity) context).getSupportFragmentManager(),"Report Dialog");
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                                LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
                                final View view = inflater.inflate(R.layout.layout_dialog_report, null);

                                builder.setView(view)
                                        .setTitle("State Your Concern")
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });

                                answer = view.findViewById(R.id.answer);
                                final AlertDialog dialog = builder.create();
                                dialog.show();
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String text = answer.getText().toString();
                                        if(text.isEmpty())
                                            answer.setError("Please enter your problem");
                                        else if(text.length()<20)
                                            answer.setError("Minimum 20 characters required");
                                        else {
                                            submitReport(text, notes.getTimestamp());
                                            dialog.dismiss();
                                        }
                                    }
                                });
                            }
                            break;

                            case R.id.tagover:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                LayoutInflater inflater = ((AppCompatActivity)context).getLayoutInflater();
                                View view = inflater.inflate(R.layout.layout_tag_dialog, null);
                                final boolean[] etuB = {true};
                                final boolean[] shortB = {true};
                                final boolean[] longB = {true};

                                final boolean[] ttpB = {true};
                                final Button etu = view.findViewById(R.id.etuTag);
                                final Button shortt = view.findViewById(R.id.shortTag);
                                final Button longt = view.findViewById(R.id.longTag);
                                final Button ttp = view.findViewById(R.id.ttpTag);
//                                Button doneButton = view.findViewById(R.id.doneButton);

                                etu.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        etuB[0] = !etuB[0];
                                        if (!etuB[0] && !finalSelectedTags1.contains("Easy to understand")){
                                            finalSelectedTags1.add("Easy to understand");
                                        }
                                        v.setBackgroundColor(etuB[0] ? Color.parseColor("#6FFF6F") : Color.parseColor("#506FFF6F"));

                                    }
                                });
                                shortt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        shortB[0] = !shortB[0];
                                        if (!shortB[0] && !finalSelectedTags2.contains("Short")){
                                            finalSelectedTags2.add("Short");
                                        }
                                        v.setBackgroundColor(shortB[0] ? Color.parseColor("#FBFF61") : Color.parseColor("#50FBFF61"));
                                    }
                                });
                                longt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        longB[0] = !longB[0];
                                        if (!longB[0] && !finalSelectedTags3.contains("Long")){
                                            finalSelectedTags3.add("Long");
                                        }
                                        v.setBackgroundColor(longB[0] ? Color.parseColor("#FF6A6A") : Color.parseColor("#50FF6A6A"));
                                    }
                                });
                                ttp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ttpB[0] = !ttpB[0];
                                        if (!ttpB[0] && !finalSelectedTags4.contains("To the point")){
                                            finalSelectedTags4.add("To the point");
                                        }
                                        v.setBackgroundColor(ttpB[0] ? Color.parseColor("#6AFFEC") : Color.parseColor("#506AFFEC"));
                                    }
                                });

                                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ArrayList<String> stringUpload = new ArrayList<>();
                                        if (finalSelectedTags5.isEmpty()){
                                            stringUpload = notes.getTags();
                                        }
                                        else
                                            stringUpload = finalSelectedTags5;
                                        Upload upload = new Upload(notes.getName(),
                                                notes.getCourse(),
                                                notes.getSemester(),
                                                notes.getBranch(),
                                                notes.getUnit(),
                                                notes.getAuthor(), notes.getDownload(), notes.getUrl(), notes.getTimestamp(),notes.getUploaderMail(),stringUpload,notes.getid());
                               
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//                                        if (!etuB[0]){
//                                            etuB[0]=true;
//                                            etu.setBackgroundResource(R.drawable.button_design);
//                                        }
//                                        if (!shortB[0]){
//                                            shortB[0]=true;
//                                            shortt.setBackgroundResource(R.drawable.button_design);
//                                        }
//                                        if (!longB[0]){
//                                            longB[0]=true;
//                                            longt.setBackgroundResource(R.drawable.button_design);
//                                        }
//                                        if (!ttpB[0]){
//                                            ttpB[0]=true;
//                                            ttp.setBackgroundResource(R.drawable.button_design);
//
//                                        }
                                    }
                                });

                                builder.setView(view);
                                AlertDialog dialog = builder.create();
                                dialog.show();


                        }
                        return true;
                    }
                });
                popup.getMenu().findItem(R.id.deletenotes).setVisible(false);
                popup.getMenu().findItem(R.id.details).setVisible(false);
                if(notes.getUploaderMail().equals(user.getEmail()))
                    popup.getMenu().findItem(R.id.tagover).setEnabled(false);
                popup.show();
            }
        });

    }
    public void downloadfile(String url, String name) {
        final DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setMimeType("application/pdf");
        request.setDestinationInExternalFilesDir(context,"Notes/Download Notes",name+".pdf");
        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(false);
        final long id = downloadManager.enqueue(request);
        Toast.makeText(context,"Downloading..... Please Wait!",Toast.LENGTH_LONG).show();
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(id));
                if (c != null) {
                    c.moveToFirst();
                    try {
                        String fileUri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        File mFile = new File(Uri.parse(fileUri).getPath());
                        String fileName = mFile.getAbsolutePath();
                        openfile(fileName);
                    } catch (Exception e) {
                        Log.e("error", "Could not open the downloaded file");
                    }
                }
            }
        };
        context.registerReceiver(onComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void openfile(String path){
        Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",new File(path));
//        Log.d("Upload", "openfile:uri being sent in intent "+uri+"\n Actual path: "+uri);
        context.getApplicationContext().grantUriPermission(context.getPackageName(),uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Log.d("Upload", "openfile: "+ path);
        intent.setDataAndType(uri, "application/pdf");
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return noteslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,author,noOfDown;
        ImageButton report;
        View itv;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.authorname);
            report = itemView.findViewById(R.id.reportButton);
            noOfDown = itemView.findViewById(R.id.download);
            itv = itemView;
            recyclerView = itemView.findViewById(R.id.tagsRecycler);
        }
    }

    public Filter getFilter() {
        return notesfilter;
    }
    private Filter notesfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Upload> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length()==0){
                filteredList.addAll(noteslistfull);
            }
            else
            {
                String filterpattern = constraint.toString().toLowerCase().trim();
                for (Upload item: noteslistfull){
                    if(item.getName().toLowerCase().contains(filterpattern) || item.getAuthor().toLowerCase().contains(filterpattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            noteslist.clear();
            noteslist.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public void submitReport(String text, Date timeStamp) {
//--------------------------------------------------------------------------------------------------------
        Toast.makeText(context, text+" "+timeStamp, Toast.LENGTH_SHORT).show();
    }

}