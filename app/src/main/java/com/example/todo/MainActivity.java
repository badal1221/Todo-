package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todo.Utils.Mydbhandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListner{
    private  RecyclerView task;
    private  List<Model> arr=new ArrayList<>();
    private  Recadapter adapter;
    private Mydbhandler db;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Objects.requireNonNull(getSupportActionBar()).hide();
        db=new Mydbhandler(this);
        task=findViewById(R.id.task);
        fab=findViewById(R.id.fab);
        task.setLayoutManager(new LinearLayoutManager(this));
        adapter=new Recadapter(this,db);
        task.setAdapter(adapter);
        ItemTouchHelper i=new ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        i.attachToRecyclerView(task);
        arr=db.getAllTasks();
        Collections.reverse(arr);
        adapter.updateData(arr);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Addnewtask.newInstance().show(getSupportFragmentManager(),Addnewtask.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        arr=db.getAllTasks();
        Collections.reverse(arr);
        adapter.updateData(arr);
    }
}