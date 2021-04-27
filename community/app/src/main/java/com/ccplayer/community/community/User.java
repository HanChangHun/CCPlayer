package com.ccplayer.community.community;

import java.util.HashMap;
import java.util.Map;

public class User extends DBItem {
    public String uid;
    public String name;
    public String email;
    public String pw;

    public User () {
    }

    public User (String uid, String name, String email, String pw) {
        super();
        this.id = uid;
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.pw = pw;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = (HashMap) super.toMap();
        result.put("id", id);
        result.put("uid", uid);
        result.put("name", name);
        result.put("email", email);
        result.put("pw", pw);
        return result;
    }
}
