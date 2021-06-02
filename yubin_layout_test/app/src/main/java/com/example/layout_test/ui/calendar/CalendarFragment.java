package com.example.layout_test.ui.calendar;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.layout_test.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class CalendarFragment extends Fragment {
    private int selectedYear = 0;
    private int selectedMon = 0;
    private int selectedDay = 0;
    private String keyOfData = "";
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
        CalendarView cal = root.findViewById(R.id.calendarView);

        //Initial Key is current date
        keyOfData = keyManager.initialKey();
        Log.i("Initial key", keyOfData);
        //If users click the calendar, key data change.
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedYear = year;
                selectedMon = month + 1;
                selectedDay = dayOfMonth;
                //선택된 날짜의 key로 현재 key를 갱신
                keyOfData = keyManager.newKey(Integer.toString(selectedYear), Integer.toString(selectedMon), Integer.toString(selectedDay));
                Log.i("After key", keyOfData);
            }
        });

        final ArrayList<String> todo = new ArrayList<String>() ;
        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice,
                todo);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

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
                    ed.setText("");
                    adapter.notifyDataSetChanged();
                    Log.i("add", keyOfData);
                }

            }
        });
        return root;
    }
    //menu - top_menu_calendar 레이아웃 개체화
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_menu_calendar, menu);
    }

}
