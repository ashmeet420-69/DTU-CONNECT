package com.example.Dtu_connect.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Dtu_connect.About;
import com.example.Dtu_connect.R;
import com.example.Dtu_connect.UserData;
import com.example.Dtu_connect.UserList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.parse.Parse.getApplicationContext;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    ArrayList<UserData> users;
    private Context context;

    public  UserAdapter(Context context,ArrayList<UserData> users)
    {
        this.context=context;
        this.users=users;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userdatadisplay,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        UserData data = users.get(position);
        Picasso.with(getApplicationContext()).load(data.image.getUrl()).into(holder.iv);
        holder.batch.setText(data.batch);
        holder.branch.setText(data.branch);
        holder.name.setText(data.name);
        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,About.class);
                intent.putExtra("user",users.get(position).email);
                Toast.makeText(getApplicationContext(), ""+users.get(position).email, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return users.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView name,batch,branch;
        View mview;
        public MyViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.names);
            branch=itemView.findViewById(R.id.branchs);
            batch = itemView.findViewById(R.id.Batchs);
            iv = itemView.findViewById(R.id.profileimage);
            mview=itemView;
        }
    }


}
