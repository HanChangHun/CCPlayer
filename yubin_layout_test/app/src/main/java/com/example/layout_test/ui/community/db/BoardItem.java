package com.example.layout_test.ui.community.db;
/*
    각 게시판 정보를 담는 모델
    안유빈/202021088
 */

import com.example.layout_test.ui.community.db.DBItem;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class BoardItem extends DBItem {
    public String name;
    public HashMap<String, String> posts;

    public BoardItem () {
    }

    public BoardItem(String id, String name, HashMap<String, String> posts) {
        super(id);
        this.name = name;
        this.posts = posts;
    }

    public Map<String, Object> toMap () {
        HashMap<String, Object> result = (HashMap) super.toMap();
        result.put("name", name);
        result.put("posts", posts);
        return result;
    }
}
