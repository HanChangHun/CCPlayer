package com.example.layout_test.ui.community;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.layout_test.MainActivity;
import com.example.layout_test.R;
import com.example.layout_test.ui.community.db.BoardItem;
import com.example.layout_test.ui.community.db.PostItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class CommunityBoardsFragment extends Fragment {
    private DatabaseReference dbref;
    private FirebaseRecyclerAdapter adapter;

    private RecyclerView rvBoards;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // R.menu.top_menu_calendar로 메뉴 만들기
        inflater.inflate(R.menu.top_menu_community, menu);
        //return true;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.screen_community_boards, container, false);

        rvBoards = root.findViewById(R.id.boardsRV);

        // DB에서 게시판 목록 가져오기
        dbref = MainActivity.getFireDB().getReference();
        Query query = dbref.child("boards");
        FirebaseRecyclerOptions<BoardItem> options = new FirebaseRecyclerOptions.Builder<BoardItem>()
                .setQuery(query, BoardItem.class).build();
        adapter = new FirebaseRecyclerAdapter<BoardItem, BoardViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BoardViewHolder holder, int position, @NonNull BoardItem model) {
                holder.bindPlz(model);
            }

            @NonNull
            @Override
            public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_board_item, parent, false);
                return new BoardViewHolder(view);
            }
        };

        // (이걸 안하면 글이 작성일자 순서로 정렬되긴 하는데 거꾸로 나와요)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvBoards.setLayoutManager(layoutManager);

        // 어댑터 꽂아두기
        rvBoards.setAdapter(adapter);
        return root;
    }
}