package com.jagroop.photochat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jagroop.photochat.RecyclerView.RecyclerAdapter;
import com.jagroop.photochat.RecyclerView.Users;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    Toolbar toolbar;
    RecyclerView recyclerView;
    private EditText searchBox;
    ImageView searchFriends;
    private List<Users> userList = new ArrayList<>();
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.searchToolbar);
        setSupportActionBar(toolbar);

        searchBox = findViewById(R.id.searchBox);
        searchFriends = findViewById(R.id.searchFriends);

        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(false);

        adapter = new RecyclerAdapter(userList  , getApplication());
        recyclerView.setAdapter(adapter);

        searchFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: searchFriends");
                if (!searchBox.getText().toString().isEmpty()) {
                    clearUserList();
                    listenForData();
                }
            }
        });
    }

    private void clearUserList() {
        this.userList.clear();
        int size = userList.size();
        adapter.notifyItemRangeChanged(0, size);
    }

    private void listenForData() {
        DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child(getString(R.string.fb_db_name_users));
        Query query = usersDb.orderByChild(getString(R.string.fb_db_name_email))
                .startAt(searchBox.getText().toString());


        Log.d(TAG, "listenForData: " + searchBox.getText().toString());

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: ");
                String userEmail = "", userUid = dataSnapshot.getRef().getKey(), userName = "";
                if (dataSnapshot.child(getString(R.string.fb_db_name_email)).getValue() != null) {
                    userEmail = dataSnapshot.child(getString(R.string.fb_db_name_email)).getValue().toString();
                    userName = dataSnapshot.child(getString(R.string.fb_db_name_name)).getValue().toString();
                }
                if (!userEmail.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                    Users user = new Users(userName, userEmail, userUid);
                    userList.add(user);
                    int size = userList.size();
                    adapter.notifyItemRangeChanged(0, size);
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "onChildAdded: size = " + userList.size());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private List<Users> getDataSet() {
        listenForData();
        return userList;
    }


}
