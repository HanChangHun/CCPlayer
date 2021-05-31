package com.example.layout_test.ui.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.layout_test.MainActivity;
import com.example.layout_test.R;
import com.example.layout_test.ui.community.db.BoardItem;
import com.example.layout_test.ui.community.db.CommentItem;
import com.example.layout_test.ui.community.db.PostItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class CommunityViewPostActivity extends AppCompatActivity {
    private String boardID, postID;
    private TextView mPostTitle, mPostBody;
    private EditText textComment;
    private Button btnCommentSubmit;

    private RecyclerView rvPost;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_view_post);

        mPostTitle = findViewById(R.id.textPostTitle);
        mPostBody = findViewById(R.id.textPostBody);
        textComment = findViewById(R.id.textPostComment);
        btnCommentSubmit = findViewById(R.id.btnPostCommentSubmit);
        rvPost = findViewById(R.id.rvComments);

        btnCommentSubmit.setEnabled(false);
        textComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btnCommentSubmit.setEnabled((s.toString().trim().length() != 0));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnCommentSubmit.setEnabled((s.toString().trim().length() != 0));
            }

            @Override
            public void afterTextChanged(Editable s) {
                btnCommentSubmit.setEnabled((s.toString().trim().length() != 0));
            }
        });

        DatabaseReference dbref = MainActivity.getFireDB().getReference();

        // Intent에서 가져온 ID로 게시글 내용 가져오기
        postID = getIntent().getStringExtra("postID");
        boardID = getIntent().getStringExtra("boardID");

        Log.d("VIEWITEM", "Fetch post id: " + postID);
        dbref.child("posts").child(postID).get().addOnCompleteListener(task -> {
            PostItem result = task.getResult().getValue(PostItem.class);
            if (task.isSuccessful() && result != null) {
                // 글 내용 보여주기
                mPostTitle.setText(result.title);
                mPostBody.setText(result.body);
            }
            else {
                Toast.makeText(this, "게시글을 로딩하는데 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });

        // 댓글 가져오기
        Query query = dbref.child("posts").child(postID).child("comments").orderByChild("writtenOn");
        FirebaseRecyclerOptions<CommentItem> options = new FirebaseRecyclerOptions.Builder<CommentItem>()
                                                            .setQuery(query, CommentItem.class).build();
        adapter = new FirebaseRecyclerAdapter<CommentItem, CommentItem.CommentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentItem.CommentViewHolder holder, int position, @NonNull CommentItem model) {
                holder.bindPlz(model);
            }

            @NonNull
            @Override
            public CommentItem.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_comment_item, parent, false);
                return new CommentItem.CommentViewHolder(view);
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);
        rvPost.setLayoutManager(layoutManager);
        rvPost.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening(); // 어댑터가 서버를 계속 지켜보도록 하도록 활성화
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening(); // 어댑터 비활성화
    }

    // 댓글 전송
    public void onSubmit (View view)
    {
        String body = textComment.getText().toString();

        // Get current user
        FirebaseUser currentUser = MainActivity.getFireAuth().getCurrentUser();
        String  userID = currentUser.getUid();

        if (body.trim() != "") {
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

            // Generate new post key/ID from directory /posts/$postid/comments/
            String commentID = dbref.child("posts").child(postID).child("comments").push().getKey();

            // Prepare to update values
            Map<String, Object> updates = new HashMap<>(); // store directory of stuffs to update
            CommentItem comment = new CommentItem(commentID, userID, postID, body, null); // stores comment data

            // Update on /posts/$postid/comments/$commentid
            updates.put("/posts/" + postID + "/comments/" + commentID, comment);

            // Update on /user-comments/$userid/$commentid
            updates.put("/user-comments/" + userID + "/" + commentID, comment);

            // Commit update
            dbref.updateChildren(updates).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "댓글 작성 성공!", Toast.LENGTH_SHORT);
                // Reset
                textComment.setText("");
                btnCommentSubmit.setActivated(false);
                //finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "댓글 작성 실패!", Toast.LENGTH_SHORT);
            });
        }
        else {
            Toast.makeText(this, "올바른 형식의 댓글을 작성해 주세요", Toast.LENGTH_SHORT).show();
        }
    }
}