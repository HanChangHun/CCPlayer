package com.ccplayer.community;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ccplayer.community.community.DBItem;
import com.ccplayer.community.community.NewPostActivity;
import com.ccplayer.community.community.Post;
import com.ccplayer.community.community.PostViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BoardsActivity extends AppCompatActivity {
    private DatabaseReference dbref = null;
    private FirebaseRecyclerAdapter adapter = null;

    private RecyclerView postList;
    private TextView boardsTitle;
    private FloatingActionButton btnNewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boards);

        postList = findViewById(R.id.post_list);
        boardsTitle = findViewById(R.id.text_board_title);
        btnNewPost = findViewById(R.id.btn_new_post);

        // Set actionbar title
        ActionBar bar = getSupportActionBar();
        bar.setTitle("게시판");

        // Get database
        dbref = FirebaseDatabase.getInstance().getReference();

        // Setup Firebaserecycler
        Query postsQuery = dbref.child("posts").orderByChild("editedOn"); //child("posts");
        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                                                            .setQuery(postsQuery, Post.class).build();
        adapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(PostViewHolder holder, int position, Post model) {
                onPostsUpdate();
                holder.onBind(model);
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
                return new PostViewHolder(view);
            }
        };

        // Plug in the adapter to recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);

        postList.setLayoutManager(layoutManager);
        postList.setAdapter(adapter);

        // Make it so that every time there is new post or something then it updates the title bar
        dbref.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                onPostsUpdate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onPostsUpdate();
            }
        });

        // Add listeners
        btnNewPost.setOnClickListener(v -> {
            // Check if logged in
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                // Move to log in/sign up activity
                //finish();
                startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
            else {
                // Move to writing posts activity
                startActivity(new Intent(this, NewPostActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_boards, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.app_bar_write_random:
                // Write random posts
                writeRandomPost(5);
                break;

            case R.id.app_bar_logout:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();

                    // Move to log in/sign up activity
                    startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void onPostsUpdate () {
        int num = postList.getAdapter().getItemCount();
        boardsTitle.setText("글 목록 (" + num + ")");
        Log.d("COMMUNITY", "POSTS UPDATED");
    }

    public void writeRandomPost (int amount) {
        final String[] titleList1 = new String[]{"안녕하세요. ", "다들 ", "여러분 ", "와우 "};
        final String[] titleList2 = new String[]{"오랜만입니다.", "반가워요.", "안녕!", "잘있어요 다시만나요"};
        final String[] objects = new String[]{"여러분", "콩순이", "밥 아저씨", "우리집 강아지"};
        final String[] prefixes = new String[]{"백만볼트", "위협적인", "매력적인", "반가운"};
        final String[] postfix = new String[]{", 아시나요?", ", 원하십니까?", ", 존재하지 않습니다.", ", 위험합니다."};
        final String[] random = new String[]{"뜻", "번역", "자막", "비디오"};

        final String[][] table = new String[][]{prefixes, objects, postfix, random, titleList1, titleList2};

        Random rand = new Random();
        while (amount-- > 0)
        {
            int length = rand.nextInt() % 10 + 3;
            String body = pickFrom(rand, prefixes) + pickFrom(rand, prefixes) + pickFrom(rand, postfix) + "\n";
            while (length-- > 0)
            {
                body += pickFrom(rand, pickFrom(rand, table));
            }
            writeNewPost(dbref, String.valueOf(rand.nextInt() % 5 + 1), pickFrom(rand, titleList1) + pickFrom(rand, titleList2), body, false);
        }
    }

    public void writeNewPost (DatabaseReference dbref, String writtenBy, String title, String body, boolean returnAfterWrite) {
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

            if (returnAfterWrite)
            {
                // Return to boards
                finish();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "글 작성 실패!", Toast.LENGTH_SHORT);
        });
    }

    public <T> T pickFrom (Random rng, T[] stuffs)
    {
        return stuffs[ rng.nextInt(stuffs.length) ];
    }
}