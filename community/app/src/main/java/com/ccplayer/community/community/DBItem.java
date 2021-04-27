package com.ccplayer.community.community;

import java.util.HashMap;
import java.util.Map;

public class DBItem {
    String id;
    public DBItem () {
    }

    public DBItem (String id) {
        this.id = id;
    }

    public Map<String, Object> toMap () {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        return map;
    };
}
