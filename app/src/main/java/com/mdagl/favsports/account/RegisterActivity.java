package com.mdagl.favsports.account;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mdagl.favsports.ProfileActivity;
import com.mdagl.favsports.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity_";

    private TextInputLayout mRegisterName;
    private TextInputLayout mRegisterEmail;
    private TextInputLayout mRegisterPassword;
    private ProgressBar mProgressBar;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressBar = findViewById(R.id.register_progressbar);
        mRegisterName = findViewById(R.id.register_name);
        mRegisterEmail = findViewById(R.id.register_email);
        mRegisterPassword = findViewById(R.id.register_pass);
        Button createAccountBtn = findViewById(R.id.register_btn);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String displayNameText = mRegisterName.getEditText().getText().toString();
                String emailText = mRegisterEmail.getEditText().getText().toString();
                String passwordText = mRegisterPassword.getEditText().getText().toString();

                if (!TextUtils.isEmpty(displayNameText) || !TextUtils.isEmpty(emailText) || !TextUtils.isEmpty(passwordText)) {
                    registerUser(displayNameText, emailText, passwordText);
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void registerUser(final String displayNameText, String emailText, String passwordText) {

        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mProgressBar.setVisibility(View.GONE);

                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            final String uId = currentUser.getUid();
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();

                            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uId);
                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", displayNameText);
                            userMap.put("image", "default");
                            userMap.put("thumbImage", "default");
                            userMap.put("device_token", deviceToken);
                            mDatabaseReference.setValue(userMap).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d(TAG, "createUserWithEmail:success");
                                            Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Cannot register user. Please check the form and try again", Toast.LENGTH_SHORT).show();

                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }
}
