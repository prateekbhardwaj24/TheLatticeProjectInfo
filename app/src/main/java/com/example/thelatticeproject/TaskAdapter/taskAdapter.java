package com.example.thelatticeproject.TaskAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thelatticeproject.DataModal;

import com.example.thelatticeproject.MyDataBaseHelper;
import com.example.thelatticeproject.R;

import java.util.ArrayList;
import java.util.Collections;

public class taskAdapter extends RecyclerView.Adapter<taskAdapter.MyViewHolder> {
    Context context;
    ArrayList<DataModal> list;
    MyDataBaseHelper myDb;


    public taskAdapter(Context context,ArrayList<DataModal> list) {
        this.context = context;
        this.list =list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.taskrecyclerlayout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        myDb = new MyDataBaseHelper(context);
        holder.task_check_box.setTag(position);

        //get current position of checkbox to resolve overlapping problem when scrolling screen
        Integer pos = (Integer) holder.task_check_box.getTag();

        //set checkbox behavior in current elements
        if (list.get(pos).isHold() == true){
            holder.task_title_text.setText(list.get(pos).getTitle());
            list.get(pos).setHold(true);
            holder.task_check_box.setChecked(true);
            holder.task_check_box.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FF03DAC5")));
            holder.task_title_text.setTextColor(Color.parseColor("#808080"));
            holder.task_title_text.setPaintFlags(holder.task_title_text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.task_title_text.setText(list.get(pos).getTitle());
            list.get(pos).setHold(false);
            holder.task_check_box.setChecked(false);
            holder.task_title_text.setTextColor(Color.parseColor("#000000"));
            holder.task_check_box.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#e2e2e2")));
            holder.task_title_text.setPaintFlags( holder.task_title_text.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }

        //applying click event on checkbox for updating checkbox state in DB and setting properties
        holder.task_check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.task_check_box.getTag();

                if (list.get(pos).isHold() == true){
                    list.get(pos).setHold(false);
                    holder.task_check_box.setChecked(false);
                    myDb.updateData(list.get(pos).getId(), "unread");
                    holder.task_check_box.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#e2e2e2")));
                    holder.task_title_text.setTextColor(Color.parseColor("#000000"));
                    holder.task_title_text.setPaintFlags( holder.task_title_text.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }else{
                    list.get(pos).setHold(true);
                    holder.task_check_box.setChecked(true);
                    myDb.updateData(list.get(pos).getId(), "read");
                    holder.task_check_box.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FF03DAC5")));
                    holder.task_title_text.setTextColor(Color.parseColor("#808080"));
                    holder.task_title_text.setPaintFlags(holder.task_title_text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });



    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView task_title_text;
        CheckBox task_check_box;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            task_title_text = itemView.findViewById(R.id.task_title);
            task_check_box = itemView.findViewById(R.id.task_check);
        }
    }

}
