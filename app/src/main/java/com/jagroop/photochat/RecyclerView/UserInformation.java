package com.jagroop.photochat.RecyclerView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jagroop.photochat.R;


import java.util.ArrayList;

public class UserInformation {

    public static ArrayList<String> userFollowing = new ArrayList<>();

    public void startFeching() {
        userFollowing.clear();

        String uid = FirebaseAuth.getInstance().getUid();

        DatabaseReference userFollowingDb = FirebaseDatabase.getInstance().getReference()
                .child("users").child(uid)
                .child("following");

        userFollowingDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    String uid = dataSnapshot.getRef().getKey();
                    if (uid != null && !userFollowing.contains(dataSnapshot.getRef().getKey())){
                        userFollowing.add(uid);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String uid = dataSnapshot.getRef().getKey();
                    if (uid != null){
                        userFollowing.remove(uid);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
