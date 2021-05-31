package com.example.layout_test.ui.community;

import android.app.Activity;
import android.content.Context;
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

    private String strBoardTitle;
    private String strBoardID;
    private ArrayList<PostItem> posts;

    private RecyclerView rvPost;
    private TextView textBoardsTitle;
    private TextView textNoPosts;
    private FloatingActionButton btnNewPost;

    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_community);
        mContext = this;

        dbref = MainActivity.getFireDB().getReference();

        rvPost = findViewById(R.id.postsRV);
        textBoardsTitle = findViewById(R.id.textCommunityTitle);
        textNoPosts = findViewById(R.id.textNoPosts);
        btnNewPost = findViewById(R.id.btnNewPost);

        strBoardID = getIntent().getStringExtra("boardID");

        // RecyclerView 어댑터 설정
        posts = new ArrayList<>();
        adapter = new CommunityPostsAdapter(posts);
        rvPost.setAdapter(adapter);

        // (이걸 안하면 글이 작성일자 순서로 정렬되긴 하는데 거꾸로 나와요)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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

        // Intent에서 가져온 ID로 게시판 내용 & 제목 가져오기
        dbref.child("boards").child(strBoardID).child("name").get().addOnCompleteListener(task -> {
            String title = (String) task.getResult().getValue();
            if (task.isSuccessful() && title != null) {
                // 게시판 제목
                strBoardTitle = title;
                onBoardUpdate();
            }
            else {
                Toast.makeText(this, "게시판을 로딩하는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // DB에서 게시글 가져오게끔 리스너 붙이기
        clearPostData();
        dbref.child("boards").child(strBoardID).child("posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                PostItem post = snapshot.getValue(PostItem.class);

                if (previousChildName == null)
                    addPost(post);
                else
                {
                    // Find a place to add
                    int idx = findPostIdx(previousChildName);
                    addPostAt(post, Math.max(idx + 1, 0));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                PostItem post = snapshot.getValue(PostItem.class);
                String postId = post.id;

                // Find post with id
                int idx = findPostIdx(postId);
                if (idx != -1)
                    posts.set(idx, post); // replace
                onBoardUpdate();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                PostItem post = snapshot.getValue(PostItem.class);
                String postId = post.id;

                // Find post with id
                int idx = findPostIdx(postId);
                if (idx != -1)
                    posts.remove(idx); // remove
                onBoardUpdate();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int idx1, idx2 = findPostIdx(previousChildName);
                PostItem    post1 = snapshot.getValue(PostItem.class),
                            post2 = (idx2 == -1) ? null : posts.get(idx2);
                idx1 = findPostIdx(post1.id);
                if (idx1 != -1 && idx2 != -1)
                {
                    // Swap two posts
                    posts.set(idx1, post2);
                    posts.set(idx2, post1);
                }
                onBoardUpdate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onBoardUpdate();
                Toast.makeText(mContext, "게시글을 로딩하는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        //updatePosts(dbref, strBoardID);
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

    // 게시판에 글을 로딩할때마다 실행
    public void onBoardUpdate () {
        // RecyclerView 갱신
        adapter.notifyDataSetChanged();

        // 게시글 제목에 게시글 개수 붙이기
        int num = rvPost.getAdapter().getItemCount();
        textBoardsTitle.setText(strBoardTitle + " : 글 목록 (" + num + ")");

        // "게시글이 없습니다" 텍스트 visible 플래그 설정
        textNoPosts.setVisibility((num>0) ? View.INVISIBLE : View.VISIBLE);
    }

    // 게시판 목록 초기화
    public void clearPostData () {
        posts.clear();
        onBoardUpdate();
    }

    // DB에서 주어진 ID의 게시글 읽어서 게시글 리스트에 추가하기
    public void addPost (String postID) {
        dbref.child("posts").child(postID).get().addOnCompleteListener(postTask -> {
            PostItem postResult = postTask.getResult().getValue(PostItem.class);
            if (postTask.isSuccessful() && postResult != null) {
                Log.d("BOARDPOSTS", postID + ": FETCHED!! > " + postResult);
                posts.add(postResult);
                // RecyclerView & 게시판 제목 갱신
                onBoardUpdate();
            }
            else
                Log.d("BOARDPOSTS", postID + ": FETCH FAILED!!");
        });
    }
    public void addPost (PostItem postItem) {
        posts.add(postItem);
        // RecyclerView & 게시판 제목 갱신
        onBoardUpdate();
    }
    public void addPostAt (PostItem postItem, int idx) {
        posts.add(idx, postItem);
        // RecyclerView & 게시판 제목 갱신
        onBoardUpdate();
    }

    public int findPostIdx (String postId) {
        int idx = -1;
        for (int i=0; i<posts.size(); i++)
        {
            if (posts.get(i).id == postId)
            {
                idx = i;
                break;
            }
        }
        return idx;
    }

    // DB에서 게시판 포스트 읽기
    public void updatePosts () {
        // 1] 현재 게시판 글 목록 읽기
        clearPostData();
        dbref.child("boards").child(strBoardID).child("posts").orderByChild("editedOn").get().addOnCompleteListener(task -> {
            Iterable<DataSnapshot> results = task.getResult().getChildren();
            Log.d("BOARDPOSTS", "results " + results);

            if (task.isSuccessful() && results != null) {
                // 2] 각 글 ID마다.. 글 DB에서 글 검색 & 리스트에 넣기
                results.forEach(postDataSnapshot -> {
                    String postID = postDataSnapshot.getKey();
                    DatabaseReference postDBRef = MainActivity.getFireDB().getReference();

                    Log.d("BOARDPOSTS", "Post " + postID);

                    postDBRef.child("posts").child(postID).get().addOnCompleteListener(postTask -> {
                        PostItem postResult = postTask.getResult().getValue(PostItem.class);
                        if (postTask.isSuccessful() && postResult != null) {
                            Log.d("BOARDPOSTS", postID + ": FETCHED!! > " + postResult);
                            posts.add(postResult);
                            // RecyclerView & 게시판 제목 갱신
                            onBoardUpdate();
                        }
                        else
                            Log.d("BOARDPOSTS", postID + ": FETCH FAILED!!");
                    });
                });
            }
            /*
            else {
                Toast.makeText(this, "게시글을 로딩하는데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
            */
        });
    }
}
