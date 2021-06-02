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
                    //newKey = findKey + "_" + Integer.toString(cnt);
                    if(i == keyList.size() - 1) {
                        if (!keyList.contains(newKey)) {
                            newKey = findKey + "_" + Integer.toString(cnt);
                            keyList.add(i + 1, newKey);
                            break;
                        }
                    }
                    else
                    {
                        if(!keyList.contains(newKey))
                        {
                            newKey = findKey + "_" + Integer.toString(cnt - 1);
                            keyList.add(i, newKey);
                            break;
                        }
                    }
                }
                cnt ++;
            }
            for(int i = 0; i < keyList.size(); i++)
            {
                Log.i("key is : ", keyList.get(i));
            }
        }
        return newKey;
    }

    public int endOfDateIndex(String year, String month, String date)
    {
        findKey = year + month + date + "_Time";
        String findDate = year + month + date;
        int index = 0;
        int cnt = 0;
        if(keyList.contains(findKey)) {
            index = keyList.indexOf(findKey);
            for (int i = index + 1; i < keyList.size(); i++) {
                if (keyList.get(i).contains("_Time") || i == keyList.size() - 1) {
                    break;
                }
                cnt++;
            }
        }
        return cnt;
    }
}
