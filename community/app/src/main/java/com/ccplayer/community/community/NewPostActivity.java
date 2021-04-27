package com.ccplayer.community.community;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ccplayer.community.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class NewPostActivity extends AppCompatActivity {
    private DatabaseReference dbref;
    private EditText title, body;
    private Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        dbref = FirebaseDatabase.getInstance().getReference();

        title = findViewById(R.id.edittext_post_title);
        body = findViewById(R.id.edittext_post_body);
        btnPost = findViewById(R.id.btn_post);

        // Set listeners
        btnPost.setOnClickListener(v -> {
            String strTitle = title.getText().toString(), strBody = body.getText().toString();

            // Check for valid post format
            boolean titleEmpty = strTitle.isEmpty(),
                    bodyEmpty = strBody.isEmpty();
            if (titleEmpty || bodyEmpty) {
                if (titleEmpty) title.setError("빈 제목은 허용되지 않습니다.");
                if (bodyEmpty) body.setError("빈 게시글은 허용되지 않습니다.");
            }
            else {
                // Post to DB
                // Get current user
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                // Create map containing post info
                writeNewPost(dbref, currentUser.getUid(), strTitle, strBody);
            }
        });
    }

    public void writeNewPost (DatabaseReference dbref, String writtenBy, String title, String body) {
        if (dbref == null)
            dbref = FirebaseDatabase.getInstance().getReference();

        // Generate new post key/ID from directory /posts/
        String postID = dbref.child("posts").push().getKey();

        // Prepare to update values
        Map<String, Object> updates = new HashMap<>(); // store directory of stuffs to update
        Post post = new Post(postID, writtenBy, title, body, null, null); // stores post data

        // Update on /posts/$postid
        updates.put("/posts/" + postID, post);
        // Update on /posts/$postid
        updates.put("/user-posts/" + writtenBy + "/" + postID, post);

        // Commit update
        dbref.updateChildren(updates).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "글 작성 성공!", Toast.LENGTH_SHORT);
            // Return to boards
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "글 작성 실패!", Toast.LENGTH_SHORT);
        });
    }
}