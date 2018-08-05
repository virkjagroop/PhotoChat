package com.jagroop.photochat.RecyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.jagroop.photochat.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final String TAG = "RecyclerAdapter";

    private List<Users> userList;
    private Context context;

    public RecyclerAdapter(List<Users> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_recycler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.foundFriend.setText(userList.get(holder.getLayoutPosition()).getName());

        if (UserInformation.userFollowing.contains(userList.get(holder.getLayoutPosition()).getUid())) {
            holder.addFriend.setImageResource(R.drawable.ic_add_friend_green);
        } else {
            holder.addFriend.setImageResource(R.drawable.ic_add_friend_black);
        }

        holder.addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userUid = FirebaseAuth.getInstance().getUid();
                if (!UserInformation.userFollowing.contains(userList.get(holder.getLayoutPosition()).getUid())) {
                    holder.addFriend.setImageResource(R.drawable.ic_add_friend_green);
                    FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child(userUid)
                            .child("following")
                            .child(userList.get(holder.getLayoutPosition()).getUid()).setValue(true);
                } else {
                    holder.addFriend.setImageResource(R.drawable.ic_add_friend_black);
                    FirebaseDatabase.getInstance().getReference()
                            .child("users")
                            .child(userUid)
                            .child("following")
                            .child(userList.get(holder.getLayoutPosition()).getUid()).removeValue();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
