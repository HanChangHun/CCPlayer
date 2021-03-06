package com.example.layout_test.ui.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.layout_test.MainActivity;
import com.example.layout_test.R;
import com.example.layout_test.ui.community.LoginActivity;
import com.example.layout_test.ui.community.db.UserItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class MypageFragment extends Fragment {
    View root;
    TextView tvUsername, tvEmail, tvInfoPosts, tvInfoStudyTime;
    ImageView imgProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.screen_mypage, container, false);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        tvUsername = root.findViewById(R.id.mypage_username);
        tvEmail = root.findViewById(R.id.mypage_email);
        tvInfoPosts = root.findViewById(R.id.mypage_user_posts);
        tvInfoStudyTime = root.findViewById(R.id.mypage_user_studytime);
        Button btnLogin = root.findViewById(R.id.mypage_login_button);
        Button btnSetPW = root.findViewById(R.id.mypage_set_pw);

        // Set Listener
        btnLogin.setOnClickListener(v -> {
            onLoginClick(v);
        });

        btnSetPW.setOnClickListener(v -> {
            startActivityForResult(new Intent(getContext(), SetPWActivity.class), 0);
            // user.updatePassword();
            //Toast.makeText(getContext(), "PW set", Toast.LENGTH_SHORT).show();
        });

        // Check if user is logged in
        updateUserInfo(user);

        return root;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_menu_mypage, menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_mypage1) {
            // ????????????
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "???????????? ???????????????.", Toast.LENGTH_SHORT);
            }
            updateUserInfo(FirebaseAuth.getInstance().getCurrentUser());
        }

        return super.onOptionsItemSelected(item);
    }

    // ???????????? ??????????????? ????????? ???????????? ????????? ??????
    public void setLoginScreen (boolean visible) {
        View loginRequired = root.findViewById(R.id.mypage_login_please);
        if (visible) {
            loginRequired.setVisibility(View.VISIBLE);
            loginRequired.setClickable(true);
        }
        else {
            loginRequired.setVisibility(View.GONE);
            loginRequired.setClickable(false);
        }
    }

    // ????????? ?????? ??????
    public void onLoginClick (View view) {
        startActivityForResult(new Intent(getContext(), LoginActivity.class), 1);
    }

    private void updateUserInfo (FirebaseUser user) {
        if (user != null) {
            // Logged in
            // Hide the login message
            setLoginScreen(false);

            // Update the user info from DB
            String userID = user.getUid();
            DatabaseReference dbref = MainActivity.getFireDB().getReference();
            dbref.child("users").child(userID).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().getValue() != null) {
                    UserItem userData = task.getResult().getValue(UserItem.class);

                    // Username
                    tvUsername.setText(userData.name);
                    // Email
                    tvEmail.setText(userData.email);
                }
                else {
                    Toast.makeText(getContext(), "????????? ????????? ??????????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }
            });
            dbref.child("user-posts").child(userID).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().getValue() != null) {
                    // Posts num
                    tvInfoPosts.setText("????????? ?????????: " + task.getResult().getChildrenCount() + "???");
                }
                else {
                    Toast.makeText(getContext(), "????????? ????????? ??????????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            // Log in required
            // Show the login message
            setLoginScreen(true);

            // Username
            tvUsername.setText("Username");
            // Email
            tvEmail.setText("Email");
            // Posts num
            tvInfoPosts.setText("????????? ?????????: 0???");
            // Study time
            tvInfoStudyTime.setText("????????? ??????: 0???");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (    requestCode == 0 || // set pw request
                requestCode == 1    // login request
            ) {
            // Update page
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            updateUserInfo(user);
        }
    }
}
