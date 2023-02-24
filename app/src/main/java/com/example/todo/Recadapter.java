package com.example.todo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Utils.Mydbhandler;

import java.util.ArrayList;
import java.util.List;

public class Recadapter extends RecyclerView.Adapter<Recadapter.viewholder>{
    private MainActivity activity;
    private List<Model> arr;
    private Mydbhandler db;
    public Recadapter(MainActivity activity, Mydbhandler db) {
        this.activity=activity;
        this.db = db;
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.taskrec,parent,false);
        return new viewholder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, int position) {
           db.openDatabase();
           final Model m=arr.get(position);
           holder.task.setText(m.getTask());
           if(m.getStatus()==1)
               holder.task.setChecked(true);
           else
               holder.task.setChecked(false);
           holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                   if(isChecked)
                       db.updatestatus(m.getId(),1);
                   else
                       db.updatestatus(m.getId(),0);
               }
           });
    }
    public void updateData(List<Model> arr){
        this.arr=arr;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() { return arr.size(); }
    public void setTasks(List<Model> arr){
        this.arr=arr;
    }
    public class viewholder extends RecyclerView.ViewHolder{
        CheckBox task;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            task=itemView.findViewById(R.id.todocheckbox);
        }
    }
    public void deleteItem(int position){
        Model m=arr.get(position);
        db.deletetask(m.getId());
        arr.remove(position);
        notifyItemRemoved(position);
    }
    public Context getcontext(){
        return activity;
    }
    public void editItem(int position){
         Model m=arr.get(position);
         Bundle bundle=new Bundle();
         bundle.putInt("id",m.getId());
         bundle.putString("task",m.getTask());
         Addnewtask fragment=new Addnewtask();
         fragment.setArguments(bundle);
         fragment.show(activity.getSupportFragmentManager(),Addnewtask.TAG);
    }
}
