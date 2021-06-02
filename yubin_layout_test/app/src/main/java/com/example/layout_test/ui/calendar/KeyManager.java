package com.example.layout_test.ui.calendar;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class KeyManager {
    public ArrayList<String> keyList = new ArrayList<>();
    private String findKey;
    private String currentKey;


    public String initialKey()
    {
        Calendar c = Calendar.getInstance();
        currentKey = Integer.toString(c.get(Calendar.YEAR)) + Integer.toString(c.get(Calendar.MONTH) + 1)
                + Integer.toString(c.get(Calendar.DATE)) + "_Time";
        keyList.add(currentKey);
        return currentKey;
    }

    public String newKey(String year, String month, String date)
    {
        currentKey = year + month + date + "_Time";
        if(!keyList.contains(currentKey))
        {
            keyList.add(currentKey);
        }
        return currentKey;
    }

    public String addKey(String year, String month, String date)
    {
        findKey = year + month + date;
        int cnt = 1;
        String newKey = "";
        if(keyList.contains(currentKey))
        {
            int index = keyList.indexOf(currentKey);
            for(int i = index; i < keyList.size(); i++)
            {
                if(!keyList.get(i).contains(findKey) || i == keyList.size() - 1)
                {
                    newKey = findKey + "_" + Integer.toString(cnt);
                    if(!keyList.contains(newKey))
                    {
                        keyList.add(i + 1, newKey);
                    }
                    break;
                }
                cnt ++;
            }
        }
        return newKey;
    }
}
