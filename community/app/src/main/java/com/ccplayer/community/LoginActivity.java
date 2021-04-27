package com.ccplayer.community;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ccplayer.community.community.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    EditText mEmail, mName, mPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set actionbar title
        ActionBar bar = getSupportActionBar();
        bar.setTitle("로그인");

        // Setup the firebase
        auth = FirebaseAuth.getInstance();

        mEmail = (EditText)findViewById(R.id.edittext_email);
        mPW = (EditText)findViewById(R.id.edittext_pw);

        Button btnLogin = findViewById(R.id.btn_submit);
        btnLogin.setOnClickListener(v -> {
            // On click: check PW and register
            String  email = mEmail.getText().toString().trim(),
                    pw = mPW.getText().toString().trim();

            // email check
            //final Pattern p = Pattern.compile("^[a-zA-X0-9]@[a-zA-Z0-9].[a-zA-Z0-9]");
            //Matcher m = p.matcher(email);
            if (true) {//m.matches()) {
                Log.d("LOGIN", "register: " + email + " | " + pw);

                mEmail.setError(null);
                auth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_LONG).show();

                        // Move to boards activity
                        startActivity(new Intent(this, BoardsActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                mEmail.setError("올바른 형식의 이메일을 입력해 주세요!");
            }
        });
    }
}