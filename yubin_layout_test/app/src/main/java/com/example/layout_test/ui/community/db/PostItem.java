package com.example.layout_test.ui.community.db;
/*
    각 게시글 정보를 담는 모델
    안유빈/202021088
 */

import com.example.layout_test.ui.community.db.DBItem;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

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
}
