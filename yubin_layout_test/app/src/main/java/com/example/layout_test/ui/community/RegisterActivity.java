package com.example.layout_test.ui.community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.layout_test.MainActivity;
import com.example.layout_test.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText mEmail, mName, mPW, mPWConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = (EditText)findViewById(R.id.edittextEmail);
        mName = (EditText)findViewById(R.id.edittextName);
        mPW = (EditText)findViewById(R.id.edittextPW);
        mPWConfirm = (EditText)findViewById(R.id.edittextPWConfirm);
        Button btnSignup = findViewById(R.id.btnSubmit);

        btnSignup.setOnClickListener(v -> {
            String  email = mEmail.getText().toString().trim(),
                    pw = mPW.getText().toString().trim(),
                    pwconfirm = mPWConfirm.getText().toString().trim(),
                    name = mName.getText().toString().trim();

            if (pw.equals(pwconfirm)) {
                mPW.setError(null);
                mPWConfirm.setError(null);

                FirebaseAuth auth = MainActivity.getFireAuth();
                auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        String  uEmail = user.getEmail(), uID = user.getUid(), uName = name;

                        // 새 유저 만들기
                        UserItem userData = new UserItem(uID, uName, uEmail, pw);

                        DatabaseReference ref = MainActivity.getFireDB().getReference("users");
                        ref.child(uID).setValue(userData);

                        Toast.makeText(this, "회원가입에 성공하였습니다.", Toast.LENGTH_LONG).show();

                        // 되돌아가기
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
        });
    }
}