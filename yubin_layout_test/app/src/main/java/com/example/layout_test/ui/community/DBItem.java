package com.example.layout_test.ui.community;
/*
    DB에 담기는 정보들 상위 클래스
    안유빈/202021088
 */

import java.util.HashMap;
import java.util.Map;

public class DBItem {
    public String id;
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
