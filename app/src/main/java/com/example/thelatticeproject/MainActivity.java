package com.example.thelatticeproject;

import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.thelatticeproject.TaskAdapter.ListAdapter;
import com.example.thelatticeproject.TaskAdapter.taskAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;

import static android.graphics.Color.*;
import static android.widget.Toast.LENGTH_SHORT;
import static com.example.thelatticeproject.R.color.design_default_color_primary_dark;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton AddFloatBtn;
    ColorStateList csl;
    RecyclerView recyclerView;
    ArrayList<String> mainRvArrayList, task_id, task_title, task_status;
    TextInputLayout addTaskTextField;
    Button addTaskButton;
    LinearLayout addTaskLayout;
    Boolean taskLayout = false;
    String userInput, readUnread;
    taskAdapter taskAdapter;
    // dataBaseHelper myDb;
    // dbdb db;
    //  ListAdapter listAdapter;
    ArrayList<DataModal> courseModalArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        //getting all buttons ids
        AddFloatBtn = findViewById(R.id.addFloatingBtn);
        recyclerView = findViewById(R.id.mainRv);
        addTaskTextField = findViewById(R.id.addTaskField);
        addTaskButton = findViewById(R.id.addTaskBtn);
        addTaskLayout = findViewById(R.id.topLayout);


        //setting plus icon color to white
        csl = AppCompatResources.getColorStateList(this, R.color.white);
        ImageViewCompat.setImageTintList(AddFloatBtn, csl);

        // initializing arrayList for recyclerview
        mainRvArrayList = new ArrayList<>();
        task_id = new ArrayList<>();
        task_title = new ArrayList<>();
        task_status = new ArrayList<>();
        courseModalArrayList = new ArrayList<>();


        //when click action is performed on addFloatingBtn to add list item
        AddFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskLayout == true) {
                    addTaskLayout.setVisibility(View.GONE);
                    taskLayout = false;
                } else {
                    addTaskLayout.setVisibility(View.VISIBLE);
                    taskLayout = true;
                }

            }
        });

        //when click action is performed on Done btn
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid() == true) {
                    //fetch data from userInput in addTaskTextField
                    InsertDataToDb();
                    hideKeybaord(v);
                    addTaskTextField.getEditText().setText("");
                }

            }
        });
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(MainActivity.this, "on Move", LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(MainActivity.this);

                //Remove swiped item from list and notify the RecyclerView

                int position = viewHolder.getAdapterPosition();
                DataModal dataModal = courseModalArrayList.get(position);
                myDataBaseHelper.deleteData(dataModal.getId());

                courseModalArrayList.remove(position);
//                courseModalArrayList.remove(dataModal);
                taskAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                Drawable background = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_delete);

                Paint p = new Paint();
                if (dX > 0) {
                    /* Set your color for positive displacement */
                    // Draw Rect with varying right side, equal to displacement dX

                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                            (float) itemView.getBottom(), p);
                } else {
                    /* Set your color for negative displacement */
                    // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX

                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), p);
                }

                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicHeight = background.getIntrinsicHeight();
                int iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int iconMargin = (itemHeight - intrinsicHeight) / 2;
                int iconLeft = itemView.getRight() - iconMargin - background.getIntrinsicWidth();
                int iconRight = itemView.getRight() - iconMargin;
                int iconBottom = iconTop + intrinsicHeight;
                background.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                background.draw(c);
                // Fade out the view as it is swiped out of the parent's bounds
                final float alpha = 1.0f - Math.abs(dX) / (float) itemView.getWidth();
                itemView.setAlpha(alpha);
                itemView.setTranslationX(dX);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // get all data from SQLite db
        displayData();
    }

    private void InsertDataToDb() {
        //dataBaseHelper dataBaseHelper = new dataBaseHelper(this);
        MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(MainActivity.this);

        String luserInput = addTaskTextField.getEditText().getText().toString();
        String lreadUnread = "Unread";

        boolean result = myDataBaseHelper.adddata(luserInput, lreadUnread);
        if (result == true) {
            addTaskLayout.setVisibility(View.GONE);

            DataModal dataModal = new DataModal();
            dataModal.setHold(false);
            dataModal.setTitle(luserInput);
            dataModal.setReadNread(lreadUnread);
            dataModal.setHold(false);


            courseModalArrayList.add(0, dataModal);
            taskAdapter.notifyItemInserted(0);

        }

    }


    public void displayData() {
        MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(MainActivity.this);
        Cursor cursor = myDataBaseHelper.getAllData();


        if (cursor == null) {
            Toast.makeText(this, "Please add a list item", LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                //creating object of DataModal class to set data in ArrayList<DataModal>

                DataModal dataModal = new DataModal();
                dataModal.setId(cursor.getString(0));
                dataModal.setTitle(cursor.getString(1));
                dataModal.setReadNread(cursor.getString(2));

                if (cursor.getString(2).equals("read")) {
                    dataModal.setHold(true);
                } else {
                    dataModal.setHold(false);
                }
                courseModalArrayList.add(dataModal);

                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                taskAdapter = new taskAdapter(this, courseModalArrayList);
                Collections.reverse(courseModalArrayList);
                recyclerView.setAdapter(taskAdapter);

            }

        }
    }

    public boolean isValid() {
        Boolean result = true;
        if (addTaskTextField.getEditText().getText().toString().isEmpty()) {
            addTaskTextField.setError("Please add task");
            result = false;

        }
        return result;
    }
    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}