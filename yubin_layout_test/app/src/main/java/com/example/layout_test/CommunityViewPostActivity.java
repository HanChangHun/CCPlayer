package com.example.layout_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.layout_test.ui.community.PostItem;

public class CommunityViewPostActivity extends AppCompatActivity {
    TextView mPostTitle, mPostBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_view_post);

        mPostTitle = findViewById(R.id.textPostTitle);
        mPostBody = findViewById(R.id.textPostBody);

        // Intent에서 가져온 ID로 게시글 내용 가져오기
        String postID = getIntent().getStringExtra("postID");

        Log.d("VIEWITEM", "Fetch post id: " + postID);
        MainActivity.getFireDB().getReference().child("posts").child(postID).get().addOnCompleteListener(task -> {
            PostItem result = task.getResult().getValue(PostItem.class);
            if (task.isSuccessful() && result != null) {
                // 글 내용 보여주기
                mPostTitle.setText(result.title);
                mPostBody.setText(result.body);
            }
            else {
                Toast.makeText(this, "게시글을 로딩하는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}