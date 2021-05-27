package com.example.layout_test.ui.community;

import android.app.Activity;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.layout_test.MainActivity;
import com.example.layout_test.R;
import com.example.layout_test.ui.community.db.PostItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class CommunityActivity extends AppCompatActivity {
    private DatabaseReference dbref;
    private CommunityPostsAdapter adapter;
    //private FirebaseRecyclerAdapter adapter;

    private String strBoardTitle;
    private String strBoardID;
    private ArrayList<PostItem> posts;

    private RecyclerView rvPost;
    private TextView textBoardsTitle;
    private FloatingActionButton btnNewPost;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_community);

        dbref = MainActivity.getFireDB().getReference();

        rvPost = findViewById(R.id.postsRV);
        textBoardsTitle = findViewById(R.id.textCommunityTitle);
        btnNewPost = findViewById(R.id.btnNewPost);

        strBoardID = getIntent().getStringExtra("boardID");

        // RecyclerView 어댑터 설정
        posts = new ArrayList<>();
        adapter = new CommunityPostsAdapter(posts);
        rvPost.setAdapter(adapter);

        // Intent에서 가져온 ID로 게시판 내용 & 제목 가져오기
        dbref.child("boards").child(strBoardID).child("name").get().addOnCompleteListener(task -> {
            String title = (String) task.getResult().getValue();
            if (task.isSuccessful() && title != null) {
                // 게시판 제목
                strBoardTitle = title;
                updateBoardTitle();
            }
            else {
                Toast.makeText(this, "게시판을 로딩하는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // DB에서 게시글 가져오기
        fetchPosts(dbref, strBoardID);

        // (이걸 안하면 글이 작성일자 순서로 정렬되긴 하는데 거꾸로 나와요)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvPost.setLayoutManager(layoutManager);

        // 어댑터 꽂아두기
        rvPost.setAdapter(adapter);

        // 글쓰기 버튼 리스너
        btnNewPost.setOnClickListener(v -> {
            // 로그인 체크
            if (MainActivity.getFireAuth().getCurrentUser() == null) {
                // 회원가입 화면
                startActivity(new Intent(this, LoginActivity.class));
            }
            else {
                // 글쓰기 화면
                Intent intent = new Intent(this, CommunityWritePostActivity.class);
                intent.putExtra("boardID", strBoardID);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu_community, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_community1) {
            // 로그아웃
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateBoardTitle () {
        int num = rvPost.getAdapter().getItemCount();
        textBoardsTitle.setText(strBoardTitle + " : 글 목록 (" + num + ")");
    }

    // DB에서 게시판 포스트 읽기
    public void fetchPosts (DatabaseReference dbref, String board) {
        // 1] 현재 게시판 글 목록 읽기
        posts.clear();

        dbref.child("boards").child(board).child("posts").get().addOnCompleteListener(task -> {
            Iterable<DataSnapshot> results = task.getResult().getChildren();
            Log.d("BOARDPOSTS", "results " + results);

            if (task.isSuccessful() && results != null) {
                // 2] 각 글 ID마다.. 글 DB에서 글 검색 & 리스트에 넣기
                results.forEach(postDataSnapshot -> {
                    String postID = postDataSnapshot.getKey();
                    DatabaseReference postDBRef = MainActivity.getFireDB().getReference();

                    Log.d("BOARDPOSTS", "Post " + postID);

                    //postDBRef.child("posts").child(postID).orderByChild("editedOn").get().addOnCompleteListener(postTask -> {
                    postDBRef.child("posts").child(postID).get().addOnCompleteListener(postTask -> {
                        PostItem postResult = postTask.getResult().getValue(PostItem.class);
                        if (postTask.isSuccessful() && postResult != null) {
                            Log.d("BOARDPOSTS", postID + ": FETCHED!! > " + postResult);
                            posts.add(postResult);

                            // 3] RecyclerView 갱신
                            adapter.notifyDataSetChanged();

                            // 4] 게시판 제목 갱신
                            updateBoardTitle();
                            //Toast.makeText(this, "게시글을 로딩! > " + posts.toString(), Toast.LENGTH_SHORT).show();
                        }
                        else
                            Log.d("BOARDPOSTS", postID + ": FETCH FAILED!!");
                    });
                });
            }
            else {
                Toast.makeText(this, "게시글을 로딩하는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
