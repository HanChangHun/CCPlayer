package com.ccplayer.community.community;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class Post extends DBItem {
    public String writtenBy;
    public String title;
    public String body;
    public Object writtenOn, writtenOnRev, editedOn;

    public Post () {
    }

    public Post(String id, String writtenBy, String title, String body, Object writtenOn, Object editedOn) {
        super(id);
        this.writtenBy = writtenBy;
        this.title = title;
        this.body = body;

        // Handle date
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
