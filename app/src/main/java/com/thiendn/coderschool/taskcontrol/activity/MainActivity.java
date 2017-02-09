package com.thiendn.coderschool.taskcontrol.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.thiendn.coderschool.taskcontrol.R;
import com.thiendn.coderschool.taskcontrol.adapter.TaskAdapter;
import com.thiendn.coderschool.taskcontrol.database.DatabaseHandler;
import com.thiendn.coderschool.taskcontrol.dialog.AddTaskDialog;
import com.thiendn.coderschool.taskcontrol.dialog.UpdateTaskDialog;
import com.thiendn.coderschool.taskcontrol.entity.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddTaskDialog.AddTaskDialogListener, TaskAdapter.TaskCallback,UpdateTaskDialog.UpdateTaskDialogListener {

    ArrayList<Task> taskArrayList;
    TaskAdapter taskAdapter;
    ListView lvTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        DatabaseHandler db = new DatabaseHandler(this);

        lvTasks = (ListView) findViewById(R.id.lvTasks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItemDialog();
            }
        });

        taskArrayList = db.getAllTasks();
        updateListView();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void showAddItemDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AddTaskDialog addTaskDialog = new AddTaskDialog();
        addTaskDialog.show(fm, "fragment_add_task");
    }

    private void showUpdateItemDialog(Task task){
        FragmentManager fm = getSupportFragmentManager();
        UpdateTaskDialog updateTaskDialog = UpdateTaskDialog.newInstance(1, task);
        updateTaskDialog.show(fm, "fragment_edit_task");
    }


    @Override
    public void onFinishAddTaskDialog(Task task) {
        DatabaseHandler db = new DatabaseHandler(this);
        db.addTask(task);
        taskArrayList = db.getAllTasks();
        Toast.makeText(getBaseContext(), task.getTitle() + " has been added!", Toast.LENGTH_SHORT).show();
        updateListView();
    }

    private void updateListView(){
        taskAdapter = new TaskAdapter(this, taskArrayList, this);
        lvTasks.setAdapter(taskAdapter);
    }

    @Override
    public void onCheckboxChecked(Task task, int position) {
        DatabaseHandler db = new DatabaseHandler(this);
        db.updateTask(task);
        //taskArrayList = db.getAllTasks();
        taskArrayList.set(position, task);
        taskAdapter.notifyDataSetChanged();
        //updateListView();
    }

    @Override
    public void onClickUpdateButton(Task task) {
        showUpdateItemDialog(task);
    }

    @Override
    public void onClickDeleteButton(Task task) {
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteTask(task);
        taskArrayList = db.getAllTasks();
        Toast.makeText(getBaseContext(), task.getTitle() + " has been deleted!", Toast.LENGTH_SHORT).show();
        updateListView();
    }

    @Override
    public void onItemClick(Task task) {
        Toast.makeText(getBaseContext(), "ItemClicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishEditTaskDialog(Task task) {
        DatabaseHandler db = new DatabaseHandler(this);
        db.updateTask(task);
        Toast.makeText(getBaseContext(), task.getTitle() + " has been updated!", Toast.LENGTH_SHORT).show();
        taskArrayList = db.getAllTasks();
        updateListView();
    }
}
