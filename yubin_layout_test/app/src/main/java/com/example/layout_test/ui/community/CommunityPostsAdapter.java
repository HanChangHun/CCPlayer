package com.example.layout_test.ui.community;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.layout_test.R;
import com.example.layout_test.ui.community.db.PostItem;

import java.util.ArrayList;

public class CommunityPostsAdapter extends RecyclerView.Adapter<PostItem.PostViewHolder> {
    private ArrayList<PostItem> postsList;

    public CommunityPostsAdapter(ArrayList<PostItem> postsList) {
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public PostItem.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("POST", "POST VIEWHOLDER CREATE");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_post_item, parent, false);
        return new PostItem.PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostItem.PostViewHolder holder, int position) {
        Log.d("POST", "POST VIEWHOLDER BOUND " + position);
        holder.bindPlz(postsList.get(position));
    }

    @Override
    public int getItemCount() {
        return postsList!=null?postsList.size():0;
    }
}
