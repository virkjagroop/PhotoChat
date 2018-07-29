package com.jagroop.photochat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

public class ShowCaptureActivity extends AppCompatActivity {
    private static final String TAG = "ShowCaptureActivity";

    private Toolbar mToolbar;
    private ImageView mCapturedImage;

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

            Bitmap rotatedBitmap = rotateBitmap(decodedBitmap);

            mCapturedImage.setImageBitmap(rotatedBitmap);
        }

    }

}
