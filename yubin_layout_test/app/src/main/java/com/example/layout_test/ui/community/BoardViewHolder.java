package com.example.layout_test.ui.community;
/*
    RecyclerView에 쓰일 게시판 디스플레이용 홀더
    안유빈/202021088
 */

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.layout_test.MainActivity;
import com.example.layout_test.R;
import com.example.layout_test.ui.community.db.BoardItem;
import com.example.layout_test.ui.community.db.PostItem;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class BoardViewHolder extends RecyclerView.ViewHolder {
    private TextView title;
    private BoardItem original;

    public BoardViewHolder(View view) {
        super(view);
        title = view.findViewById(R.id.board_title);

        // 리스너 설정
        view.findViewById(R.id.board_item_parent).setOnClickListener(v -> {
            Intent intent;

            // 로그인 체크
            if (MainActivity.getFireAuth().getCurrentUser() == null) {
                // 회원가입 화면
                intent = new Intent(v.getContext(), LoginActivity.class);
                intent.putExtra("boardID", original.id);
                intent.putExtra("destIntent", "CommunityActivity");
            }
            else {
                // 게시판 화면
                intent = new Intent(v.getContext(), CommunityActivity.class);
                intent.putExtra("boardID", original.id);
            }

            Log.d("BOARDITEM", "Click board " + original + " | id: " + original.id);
            v.getContext().startActivity(intent);
        });
    }

    // BoardItem에서 데이터 읽어서 현재 뷰들에 넘겨주는 메소듭니다
    public void bindPlz (BoardItem data) {
        Log.d("BOARDITEM", "Bound: " + data.toString());
        original = data;

        title.setText(data.name);
    }
}
