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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.layout_test.R;

import java.util.ArrayList;

public class CalendarFragment extends Fragment {

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
        final ArrayList<String> todo = new ArrayList<String>() ;
        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_multiple_choice,
                todo);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //add button
        Button addButton = (Button)root.findViewById(R.id.add) ;
        addButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                /*
                int count, checked;
                count = adapter.getCount();
                todo.add("일정을 추가하세요");
                adapter.notifyDataSetChanged();
                 */
                //edittext에서 값 받아오기
                EditText ed = (EditText)root.findViewById(R.id.newitem);
                String text = ed.getText().toString();
                if (!text.isEmpty()) {
                    todo.add(text);
                    ed.setText("");
                    adapter.notifyDataSetChanged();
                }

            }
        });
        /*
        Button modifyButton = (Button)root.findViewById(R.id.modify) ;
        modifyButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int count, checked ;
                count = adapter.getCount() ;

                if (count > 0) {
                    // 현재 선택된 아이템의 position 획득.
                    checked = listview.getCheckedItemPositions();
                    Log.i("log", "checked : " + checked);
                    Log.i("log2", "checked : " + count);
                    if (checked > -1 && checked < count) {
                        // 아이템 수정
                        todo.set(checked - 1, Integer.toString(checked+1) + "번 아이템 수정") ;

                        // listview 갱신
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }) ;
        */
        return root;
    }
    //menu - top_menu_calendar 레이아웃 개체화
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_menu_calendar, menu);
    }

}
