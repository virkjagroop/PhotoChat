package com.jagroop.photochat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ShowCaptureActivity extends AppCompatActivity {
    private static final String TAG = "ShowCaptureActivity";

    private Toolbar mToolbar;
    private ImageView mCapturedImage;

    private Bitmap rotatedBitmap;
    private String uid;

    private Bitmap rotateBitmap(Bitmap decodedBitmap) {
        int w = decodedBitmap.getWidth();
        int h = decodedBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(decodedBitmap, 0, 0, w, h, matrix, true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_capture);

        Log.d(TAG, "onCreate: showCatureActivity");

        mToolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(mToolbar);

        mCapturedImage = findViewById(R.id.capturedImage);

        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        byte[] b = bundle.getByteArray(getString(R.string.intent_extra_captured_image));

        if (b!=null) {
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

            rotatedBitmap = rotateBitmap(decodedBitmap);

            mCapturedImage.setImageBitmap(rotatedBitmap);
            uid = FirebaseAuth.getInstance().getUid();
        }

    }

    public void sendToStories(View view) {
        final DatabaseReference userStoryDb = FirebaseDatabase.getInstance().getReference().child(getString(R.string.fb_db_name_users)).child(uid).child(getString(R.string.fb_db_name_stories));
        final String key = userStoryDb.push().getKey();

        StorageReference filePath = FirebaseStorage.getInstance().getReference().child(getString(R.string.fb_db_name_users)).child(uid).child(key);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] dataToUpload = baos.toByteArray();

        UploadTask uploadTask = filePath.putBytes(dataToUpload);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String storyUrl = uri.toString();

                        Long currentTimeStamp = System.currentTimeMillis();
                        Long endTimeStamp = currentTimeStamp + (24*60*60*1000);

                        Map<String, Object> mapToUpload = new HashMap<>();
                        mapToUpload.put(getString(R.string.fb_db_name_storyUrl), storyUrl);
                        mapToUpload.put(getString(R.string.fb_db_name_currentTimeStamp), currentTimeStamp);
                        mapToUpload.put(getString(R.string.fb_db_name_endTimeStamp), endTimeStamp);

                        userStoryDb.child(key).setValue(mapToUpload);
                        finish();
                    }
                });

                
            }
        });



        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShowCaptureActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
