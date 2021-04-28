package com.example.layout_test.ui.community;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.layout_test.MainActivity;
import com.example.layout_test.R;
import com.example.layout_test.ui.videos.VideoFolderFragment;
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

import java.util.HashMap;
import java.util.Map;

public class CommunityFragment extends Fragment {
    private DatabaseReference dbref;
    private FirebaseRecyclerAdapter adapter;

    private RecyclerView rvPost;
    private TextView textBoardsTitle;
    private FloatingActionButton btnNewPost;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening(); // 게시글 리스트 어댑터가 서버를 계속 지켜보도록 하도록 활성화
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening(); // 어댑터 비활성화
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.screen_community, container, false);

        rvPost = root.findViewById(R.id.postsRV);
        textBoardsTitle = root.findViewById(R.id.textCommunityTitle);
        btnNewPost = root.findViewById(R.id.btnNewPost);

        // DB 리스너 설정해서 새 게시글 등록할때마다 뷰 갱신
        dbref = MainActivity.getFireDB().getReference();
        Query postsQuery = dbref.child("posts").orderByChild("editedOn");
        FirebaseRecyclerOptions<PostItem> options = new FirebaseRecyclerOptions.Builder<PostItem>()
                                                                                .setQuery(postsQuery, PostItem.class).build();
        adapter = new FirebaseRecyclerAdapter<PostItem, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull PostItem model) {
                updateBoardTitle();
                holder.bindPlz(model);
            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_post_item, parent, false);
                return new PostViewHolder(view);
            }
        };

        // 게시판 제목 갱신
        dbref.child("posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateBoardTitle();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateBoardTitle();
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                updateBoardTitle();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateBoardTitle();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                updateBoardTitle();
            }
        });

        // (이걸 안하면 글이 작성일자 순서로 정렬되긴 하는데 거꾸로 나와요)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
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
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
            else {
                // 글쓰기 화면
                startActivity(new Intent(getContext(), CommunityWritePostActivity.class));
            }
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // R.menu.top_menu_calendar로 메뉴 만들기
        inflater.inflate(R.menu.top_menu_community, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_community1) {
            // 로그아웃
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateBoardTitle () {
        int num = rvPost.getAdapter().getItemCount();
        textBoardsTitle.setText("글 목록 (" + num + ")");
    }
}
