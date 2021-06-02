package com.example.layout_test.ui.community.db;
/*
    각 댓글 정보를 담는 모델
    안유빈/202021088
 */

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.layout_test.MainActivity;
import com.example.layout_test.R;
import com.example.layout_test.ui.community.CommunityActivity;
import com.example.layout_test.ui.community.LoginActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class CommentItem extends DBItem {
    public String writtenBy;
    public String body;
    public String writtenPost;
    public Object writtenOn;

    public CommentItem() {
    }

    public CommentItem(String id, String writtenBy, String writtenPost, String body, Object writtenOn) {
        super(id);
        this.writtenBy = writtenBy;
        this.writtenPost = writtenPost;
        this.body = body;
        this.writtenOn = writtenOn != null ? writtenOn : ServerValue.TIMESTAMP;
    }

    public Map<String, Object> toMap () {
        HashMap<String, Object> result = (HashMap) super.toMap();
        result.put("writtenBy", writtenBy);
        result.put("writtenPost", writtenPost);
        result.put("body", body);
        result.put("writtenOn", writtenOn);
        return result;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView author, date, body;
        private CommentItem original;

        public CommentViewHolder(View view) {
            super(view);
            author = view.findViewById(R.id.comment_author);
            date = view.findViewById(R.id.comment_time);
            body = view.findViewById(R.id.comment_body);
        }

        // commentItem에서 데이터 읽어서 현재 뷰들에 넘겨주는 메소듭니다
        public void bindPlz (CommentItem data) {
            Log.d("COMMENTITEM", "Bound: " + data.toString());
            original = data;

            // Author
            DatabaseReference dbref = MainActivity.getFireDB().getReference();
            dbref.child("users").child(data.writtenBy).child("/name").get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().getValue() != null)
                    author.setText(task.getResult().getValue().toString());
                else
                    author.setText("<없는 유저>");
            });

            // Date
            long    writtenOn = (long) data.writtenOn;
            Date writtenDate = new Date(writtenOn);
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            date.setText(sdf.format(writtenDate));

            // Body
            body.setText(data.body);
        }
    }
}
