package com.example.layout_test.ui.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.layout_test.MainActivity;
import com.example.layout_test.R;
import com.example.layout_test.ui.community.db.UserItem;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class SetPWActivity extends AppCompatActivity {
    EditText mPWOld, mPW, mPWConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pw);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        mPW = (EditText)findViewById(R.id.edittextPW);
        mPWConfirm = (EditText)findViewById(R.id.edittextPWConfirm);
        mPWOld = (EditText)findViewById(R.id.edittextOldPW);
        Button btnSignup = findViewById(R.id.btnSubmit);

        // Check if user is logged in
        if (user == null) {
            finish();
            Toast.makeText(this, "로그인이 필요한 작업입니다.", Toast.LENGTH_LONG).show();
            return;
        }

        // Listener
        btnSignup.setOnClickListener(v -> {
            String  pw = mPW.getText().toString().trim(),
                    pwconfirm = mPWConfirm.getText().toString().trim(),
                    pwold = mPWOld.getText().toString().trim()
                    ;

            if (pw.equals(pwconfirm)) {
                mPW.setError(null);
                mPWConfirm.setError(null);
                mPWOld.setError(null);

                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), pwold);

                user.reauthenticate(credential).addOnCompleteListener(this, authtask -> {
                    if (authtask.isSuccessful()) {
                        user.updatePassword(pw).addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "패스워드 변경에 성공하였습니다.", Toast.LENGTH_LONG).show();
                                // 되돌아가기
                                finish();
                            }
                            else {
                                Toast.makeText(this, "패스워드 변경에 실패하였습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        mPWOld.setError("패스워드가 일치하지 않습니다!");
                        //Toast.makeText(this, "패스워드가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
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