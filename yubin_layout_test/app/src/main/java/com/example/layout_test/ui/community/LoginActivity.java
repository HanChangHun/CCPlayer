package com.example.layout_test.ui.community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.layout_test.MainActivity;
import com.example.layout_test.R;

public class LoginActivity extends AppCompatActivity {
    EditText mEmail, mPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText)findViewById(R.id.edittextEmail);
        mPW = (EditText)findViewById(R.id.edittextPW);
        Button btnLogin = findViewById(R.id.btnSubmit);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> {
            // 로그인
            String email = mEmail.getText().toString().trim(),
                    pw = mPW.getText().toString().trim();

            MainActivity.getFireAuth().signInWithEmailAndPassword(email, pw).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_LONG).show();
                    // 되돌아가기
                    finish();
                }
                else {
                    Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
                }
            });
        });

        btnRegister.setOnClickListener(v -> {
            // 회원가입
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}