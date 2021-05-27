package com.example.layout_test.ui.community.db;

import java.util.HashMap;
import java.util.Map;

public class UserItem extends DBItem {
    public String uid;
    public String name;
    public String email;
    public String pw;

    public UserItem () {
    }

    public UserItem (String uid, String name, String email, String pw) {
        super(uid);
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.pw = pw;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = (HashMap) super.toMap();
        result.put("uid", uid);
        result.put("name", name);
        result.put("email", email);
        result.put("pw", pw);
        return result;
    }
}
