package com.example.thelatticeproject.TaskAdapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thelatticeproject.DataModal;
import com.example.thelatticeproject.MyDataBaseHelper;
import com.example.thelatticeproject.R;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    Context context;
    ArrayList<DataModal> list;
    MyDataBaseHelper myDb;

    public ListAdapter(Context context, ArrayList<DataModal> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.taskrecyclerlayout, parent, false);
        }
        myDb = new MyDataBaseHelper(context);
        TextView task_title_text;
        CheckBox task_check_box;
        DataModal dm = list.get(position);
        task_title_text = view.findViewById(R.id.task_title);
        task_check_box = view.findViewById(R.id.task_check);

        task_check_box.setOnCheckedChangeListener(null);
        task_check_box.setChecked(dm.isHold());
        task_title_text.setText(dm.getTitle());


        String checkBoxValue = dm.getReadNread();
        String idAtPosition = dm.getId();
        if (checkBoxValue.equals("read")) {
            task_check_box.setChecked(true);
            task_title_text.setTextColor(Color.parseColor("#808080"));
            task_title_text.setPaintFlags(task_title_text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            // holder.task_check_box.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#1189C3")));

        } else {
            task_check_box.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#e2e2e2")));
        }
        task_check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dm.setHold(isChecked);
                    task_check_box.setChecked(true);

                    myDb.updateData(idAtPosition, "read");

                    task_check_box.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FF03DAC5")));
                    task_title_text.setTextColor(Color.parseColor("#808080"));
                    task_title_text.setPaintFlags(task_title_text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    //filterData(idAtPosition);

                } else {
                    dm.setHold(false);
                    task_check_box.setChecked(false);
                    myDb.updateData(idAtPosition, "unread");

                    task_check_box.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#e2e2e2")));


                    task_title_text.setTextColor(Color.parseColor("#000000"));
                    task_title_text.setPaintFlags(task_title_text.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });

        return view;
    }
}
