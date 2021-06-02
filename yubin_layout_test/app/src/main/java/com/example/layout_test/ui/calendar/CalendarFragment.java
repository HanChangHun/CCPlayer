package com.example.layout_test.ui.calendar;
//package rebuild.com.sharedpreferences;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.layout_test.R;
import com.example.layout_test.ui.calendar.KeyManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class CalendarFragment extends Fragment {
    private int selectedYear = 0;
    private int selectedMon = 0;
    private int selectedDay = 0;
    private String keyOfData = "";
    private Context mContext;
    private String data;
    KeyManager keyManager = new KeyManager();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    //layout - screen calender 레이아웃을 객체화시킴
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.screen_calendar, container, false);
        ListView listview = root.findViewById(R.id.todoList);
        TextView textView = root.findViewById(R.id.textView);
        CalendarView cal = root.findViewById(R.id.calendarView);
        mContext = container.getContext();

        final ArrayList<String> todo = new ArrayList<String>() ;
        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice,
                todo);
        //listview.setAdapter(adapter);
        //listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //Initial Key is current date
        keyOfData = keyManager.initialKey();
        Log.i("Initial key", keyOfData);
        rebuild.com.sharedpreferences.SharedPreference.setString(mContext, keyOfData, "0");
        data = rebuild.com.sharedpreferences.SharedPreference.getString(mContext, keyOfData);
        if(data != "")
        {
            todo.add(data);
            adapter.notifyDataSetChanged();
        }
        int index = keyManager.endOfDateIndex(Integer.toString(selectedYear), Integer.toString(selectedMon), Integer.toString(selectedDay));
        if(index > 0)
        {
            for(int i = 1 ; i < index + 1 ; i++)
            {
                data = rebuild.com.sharedpreferences.SharedPreference.getString(mContext,Integer.toString(selectedYear)+
                        Integer.toString(selectedMon)+ Integer.toString(selectedDay) + "_" + Integer.toString(i));
                todo.add(data);
            }
            adapter.notifyDataSetChanged();
        }
        //If users click the calendar, key data change.
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                todo.clear();
                adapter.notifyDataSetChanged();
                selectedYear = year;
                selectedMon = month + 1;
                selectedDay = dayOfMonth;

                //선택된 날짜의 key로 현재 key를 갱신
                keyOfData = keyManager.newKey(Integer.toString(selectedYear), Integer.toString(selectedMon), Integer.toString(selectedDay));
                rebuild.com.sharedpreferences.SharedPreference.setString(mContext, keyOfData, "0");
                Log.i("After key", keyOfData);
                data = rebuild.com.sharedpreferences.SharedPreference.getString(mContext, keyOfData);
                todo.add(data);
                int index = keyManager.endOfDateIndex(Integer.toString(selectedYear), Integer.toString(selectedMon), Integer.toString(selectedDay));
                if(index > 0)
                {
                    for(int i = 1 ; i < index + 1 ; i++)
                    {
                        data = rebuild.com.sharedpreferences.SharedPreference.getString(mContext,Integer.toString(selectedYear)+
                                Integer.toString(selectedMon)+ Integer.toString(selectedDay) + "_" + Integer.toString(i));
                        todo.add(data);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        //add button
        Button addButton = (Button)root.findViewById(R.id.add) ;
        addButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //edittext에서 값 받아오기
                EditText ed = (EditText)root.findViewById(R.id.newitem);
                String text = ed.getText().toString();
                if (!text.isEmpty()) {
                    todo.add(text);
                    //새로운 key 생성
                    keyOfData = keyManager.addKey(Integer.toString(selectedYear), Integer.toString(selectedMon), Integer.toString(selectedDay));
                    rebuild.com.sharedpreferences.SharedPreference.setString(mContext, keyOfData, text);
                    String test = rebuild.com.sharedpreferences.SharedPreference.getString(mContext, keyOfData);
                    Log.i("error", Integer.toString(selectedYear));
                    ed.setText("");
                    adapter.notifyDataSetChanged();
                    Log.i("add", keyOfData);
                }

            }
        });
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        return root;
    }
    //menu - top_menu_calendar 레이아웃 개체화
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_menu_calendar, menu);
    }

}
