package com.ccplayer.community.community;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ccplayer.community.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PostViewHolder extends RecyclerView.ViewHolder {
    private TextView title, preview, author, date;
    public PostViewHolder (View view) {
        super(view);
        title = view.findViewById(R.id.postTitle);
        preview = view.findViewById(R.id.postPreview);
        author = view.findViewById(R.id.postAuthor);
        date = view.findViewById(R.id.postDate);
    }

    public void onBind (Post data) {
        // If calculated preview string is shorter than original post then add trailing dots
        String previewStr = data.body.substring(0, Math.min(16, data.body.length()));
        if (previewStr.length() < data.body.length())
            previewStr += "...";

        title.setText(data.title);
        preview.setText(previewStr);

        // Process author
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
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
