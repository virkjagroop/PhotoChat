package com.jagroop.photochat.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jagroop.photochat.MainActivity;
import com.jagroop.photochat.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText mEmail, mPassword, mUserName;
    private TextInputLayout mEmailLayout, mPasswordLayout, mUserNameLayout;
    private Button mLoginBtn;
    private RelativeLayout mLoginLayout;
    private TextView mLogin;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    //private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mUserName = findViewById(R.id.userName);
        mEmailLayout = findViewById(R.id.email_layout);
        mPasswordLayout = findViewById(R.id.password_layout);
        mUserNameLayout = findViewById(R.id.userName_layout);
        mLoginBtn = findViewById(R.id.loginBtn);
        mLoginLayout = findViewById(R.id.loginLayout);
        mLogin = findViewById(R.id.login);

        mAuth = FirebaseAuth.getInstance();
        //mDatabase = FirebaseDatabase.getInstance();

        mLoginLayout.setOnClickListener(null);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        mEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && mEmail.getText().toString().isEmpty()) {
                    mEmailLayout.setErrorEnabled(true);
                    mEmailLayout.setError(getString(R.string.please_enter_email));
                }else {
                    mPasswordLayout.setErrorEnabled(false);
                }
            }
        });

        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, final boolean hasFocus) {
                if (!hasFocus && mPassword.getText().toString().isEmpty()) {
                    mPasswordLayout.setErrorEnabled(true);
                    mPasswordLayout.setError(getString(R.string.enter_password));
                } else {
                    mPasswordLayout.setErrorEnabled(false);
                }
            }
        });

        mUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && mUserName.getText().toString().isEmpty()) {
                    mUserNameLayout.setErrorEnabled(true);
                    mUserNameLayout.setError(getString(R.string.please_enter_name));
                } else {
                    mUserNameLayout.setErrorEnabled(false);
                }
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser()!=null) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        };

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String name = mUserName.getText().toString();
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();

                if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            } else {
                                //add name
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child(getString(R.string.fb_db_name_users)).child(userId);

                                Map userInfo = new HashMap();
                                userInfo.put(getString(R.string.fb_db_name_email), email);
                                userInfo.put(getString(R.string.fb_db_name_name), name);
                                userInfo.put(getString(R.string.fb_db_name_profilepic), "default");

                                currentUserDb.updateChildren(userInfo);
                            }
                        }
                    });
                } else Toast.makeText(RegisterActivity.this, R.string.enter_all_details, Toast.LENGTH_SHORT).show();



            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}
