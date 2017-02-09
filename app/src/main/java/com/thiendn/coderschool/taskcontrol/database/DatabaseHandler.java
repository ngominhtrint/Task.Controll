package com.thiendn.coderschool.taskcontrol.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thiendn.coderschool.taskcontrol.entity.Task;

import java.util.ArrayList;

/**
 * Created by thiendn on 09/02/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tasksManager";

    // Contacts table name
    private static final String TABLE_TASKS = "tasks";

    // Contacts Table Columns names
    private static final String TITLE = "title";
    private static final String PRIORITY = "priority";
    private static final String DEADLINE = "deadline";
    private static final String STATUS = "status";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + TITLE + " TEXT," + PRIORITY + " TEXT,"
                + DEADLINE + " TEXT," + STATUS + " INTEGER" + ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

        // Create tables again
        onCreate(db);
    }

    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE, task.getTitle());
        values.put(PRIORITY, task.getPriority());
        values.put(DEADLINE, task.getDeadline());
        int flag = (task.isStatus())? 1 : 0;
        values.put(STATUS, flag);

        // Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }

    // Getting All tasks
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> taskList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setTitle(cursor.getString(0));
                task.setPriority(cursor.getString(1));
                task.setDeadline(cursor.getString(2));
                task.setStatus(cursor.getInt(3) > 0);
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        // return contact list
        return taskList;
    }

    // Updating single task
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, task.getTitle());
        values.put(PRIORITY, task.getPriority());
        values.put(DEADLINE, task.getDeadline());
        values.put(STATUS, (task.isStatus())? 1 : 0);
        // updating row
        return db.update(TABLE_TASKS, values, TITLE + " = ?",
                new String[] { String.valueOf(task.getTitle()) });
    }

    // Deleting single task
    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, TITLE + " = ?",
                new String[] { String.valueOf(task.getTitle()) });
        db.close();
    }

}
