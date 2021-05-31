package com.example.layout_test.ui.community.db;
/*
    각 게시글 정보를 담는 모델
    안유빈/202021088
 */

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.layout_test.MainActivity;
import com.example.layout_test.R;
import com.example.layout_test.ui.community.CommunityViewPostActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class PostItem extends DBItem {
    public String writtenBy;
    public String title;
    public String body;
    public Object writtenOn, writtenOnRev, editedOn;

    public PostItem () {
    }

    public PostItem(String id, String writtenBy, String title, String body, Object writtenOn, Object editedOn) {
        super(id);
        this.writtenBy = writtenBy;
        this.title = title;
        this.body = body;

        // 현재 날자를 사용할지 정하기
        this.writtenOn = writtenOn!=null ? writtenOn : ServerValue.TIMESTAMP;
        this.writtenOnRev = editedOn!=null ? editedOn : ServerValue.TIMESTAMP;
        this.editedOn = editedOn!=null ? editedOn : ServerValue.TIMESTAMP;
    }

    public Map<String, Object> toMap () {
        HashMap<String, Object> result = (HashMap) super.toMap();
        result.put("writtenBy", writtenBy);
        result.put("title", title);
        result.put("body", body);
        result.put("writtenOn", writtenOn);
        result.put("writtenOnRev", writtenOnRev);
        result.put("editedOn", editedOn);
        return result;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView title, preview, author, date;
        private PostItem original;

        public PostViewHolder (View view) {
            super(view);
            title = view.findViewById(R.id.post_title);
            preview = view.findViewById(R.id.post_preview);
            author = view.findViewById(R.id.post_author);
            date = view.findViewById(R.id.post_date);

            // 리스너 설정
            view.findViewById(R.id.post_item_parent).setOnClickListener(v -> {
                // 게시글 보기 화면
                Intent intent = new Intent(v.getContext(), CommunityViewPostActivity.class);

                Log.d("POSTITEM", "Click post " + original + " | id: " + original.id);
                intent.putExtra("postID", original.id);
                v.getContext().startActivity(intent);
            });
        }

        // 아앍 그냥 편하게 영문 주석으로 갈게요오오오
        // 이건 그냥 PostItem에서 데이터 읽어서 현재 뷰들에 넘겨주는 메소듭니다
        public void bindPlz (PostItem data) {
            Log.d("POSTITEM", "Bound: " + data.toString());
            original = data;

            // If calculated preview string is shorter than original post then add trailing dots
            String previewStr = data.body.replaceAll("\n", " ").substring(0, Math.min(16, data.body.length()));
            if (previewStr.length() < data.body.length())
                previewStr += "...";

            title.setText(data.title);
            preview.setText(previewStr);

            // Process author
            DatabaseReference dbref = MainActivity.getFireDB().getReference();
            dbref.child("users").child(data.writtenBy).child("/name").get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().getValue() != null)
                    author.setText(task.getResult().getValue().toString());
                else
                    author.setText("<없는 유저>");
            });

            // Process date
            long    writtenOn = (long) data.writtenOn,
                    editedOn = (long) data.editedOn;
            Date writtenDate = new Date(writtenOn);
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            date.setText(sdf.format(writtenDate));
        }
    }
}
