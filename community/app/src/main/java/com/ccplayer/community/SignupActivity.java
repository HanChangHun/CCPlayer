package com.ccplayer.community;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.ccplayer.community.community.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    EditText mEmail, mName, mPW, mPWConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Set actionbar title
        ActionBar bar = getSupportActionBar();
        bar.setTitle("회원가입");

        // Setup the firebase
        auth = FirebaseAuth.getInstance();

        mEmail = (EditText)findViewById(R.id.edittext_email);
        mName = (EditText)findViewById(R.id.edittext_name);
        mPW = (EditText)findViewById(R.id.edittext_pw);
        mPWConfirm = (EditText)findViewById(R.id.edittext_confirm);

        Button btnSignup = findViewById(R.id.btn_submit);
        btnSignup.setOnClickListener(v -> {
            // On click: check PW and register
            String  email = mEmail.getText().toString().trim(),
                    pw = mPW.getText().toString().trim(),
                    pwconfirm = mPWConfirm.getText().toString().trim(),
                    name = mName.getText().toString().trim();

            // email check
            //final Pattern p = Pattern.compile("^[a-zA-X0-9]@[a-zA-Z0-9].[a-zA-Z0-9]");
            //Matcher m = p.matcher(email);
            if (true) {//m.matches()) {
                mEmail.setError(null);

                if (pw.equals(pwconfirm)) {
                    mPW.setError(null);
                    mPWConfirm.setError(null);

                    Log.d("REGISTER", "register: " + email + " | " + pw);

                    auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            String  uEmail = user.getEmail(), uID = user.getUid(), uName = name;

                            // Create map containing user info
                            User userData = new User(uID, uName, uEmail, pw);
                            // Store user info map to firebase DB
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = database.getReference("users");
                            ref.child(uID).setValue(userData.toMap());

                            Toast.makeText(this, "회원가입에 성공하였습니다.", Toast.LENGTH_LONG).show();

                            // Move to boards activity
                            startActivity(new Intent(this, BoardsActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else { // PW =/= PW confirm
                    mPW.setError("");
                    mPWConfirm.setError("패스워드가 일치하지 않습니다!");
                }
            }
            else {
                mEmail.setError("올바른 형식의 이메일을 입력해 주세요!");
            }
        });
    }
}
