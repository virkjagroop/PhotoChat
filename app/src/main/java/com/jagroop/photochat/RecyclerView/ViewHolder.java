package com.jagroop.photochat.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jagroop.photochat.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView foundFriend;
    ImageView addFriend;

    public ViewHolder(View itemView) {
        super(itemView);
        foundFriend = itemView.findViewById(R.id.foundFriend);
        addFriend = itemView.findViewById(R.id.addFriend);
    }

}
