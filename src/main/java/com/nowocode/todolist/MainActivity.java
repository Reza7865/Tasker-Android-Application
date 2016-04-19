package com.nowocode.todolist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CreateTaskDialogInterface{
    //the RecyclerView will hold the Cards that contain each Task
    static final String DB_NAME = "TaskDB";
    static final String DB_TABLE_NAME ="Tasks";
    public SQLiteDatabase db;

    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Task> mTasks;
    public static ArrayList<Task> tasksToRemove;
    private CreateTaskDialog mTaskDialog;
    public static FloatingActionButton removeButton;
    static int tasksChecked = 0;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDatabase();

        //Building our Dialog to create new Tasks
        mTaskDialog = new CreateTaskDialog();
        tasksToRemove = new ArrayList<>();

        //Setting up the Content and populating it with Tasks
        mRecyclerView = (RecyclerView) findViewById(R.id.task_card_container);
        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager.offsetChildrenVertical(50);
//        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mTasks = new ArrayList<>();
        mAdapter = new TaskAdapter(mTasks);
        mRecyclerView.setAdapter(mAdapter);

        getTaskList();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTaskDialog.show(getSupportFragmentManager(), "Create your new Task");
            }
        });

        removeButton = (FloatingActionButton) findViewById(R.id.deletFab);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTasks.removeAll(tasksToRemove);
                for(Task t : tasksToRemove){
                    String query = "delete from " + DB_TABLE_NAME + " where taskname='" + t.getTaskName()+"'";
                    db.execSQL(query);
                }
                mAdapter.notifyDataSetChanged();
                removeButton.hide();
                tasksChecked = 0;
            }
        });
        removeButton.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void createTask(String s, int priority) {
        try {
            Task newTask = new Task();
            newTask.setTaskName(s);
            newTask.setPriority(priority);
            saveTaskToDb(newTask);
            getTaskList();
        } catch(Exception e){
            Toast.makeText(MainActivity.this, "Error with creating the Task.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void saveTaskToDb(Task newTask){
        String query = "INSERT INTO " + DB_TABLE_NAME +" VALUES('"+ newTask.getTaskName() + "',"
                + newTask.getPriority() + ")";
        db.execSQL(query);
    }

    private ArrayList<Task> getTaskList() {
        ArrayList<Task> results = new ArrayList<>();
        cursor = db.rawQuery("select * from " + MainActivity.DB_TABLE_NAME + " order by priority", null);
        cursor.moveToFirst();
        String taskname;
        int priority;

        while (!cursor.isAfterLast()) {
            taskname = cursor.getString(0);
            priority = cursor.getInt(1);
            Task t = new Task();
            t.setTaskName(taskname);
            t.setPriority(priority);
            results.add(t);
            cursor.moveToNext();
        }

        mTasks.clear();
        mTasks.addAll(results);
        mAdapter.notifyDataSetChanged();
        return results;
    }


    private void initDatabase(){
        db = openOrCreateDatabase(DB_NAME,MODE_PRIVATE,null);
        db.execSQL("create Table if not exists " + DB_TABLE_NAME + "(taskname varchar," +
                "priority integer)");
    }

}
