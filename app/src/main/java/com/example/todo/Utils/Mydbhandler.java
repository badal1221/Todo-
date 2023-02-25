package com.example.todo.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.todo.Model;
import java.util.ArrayList;
import java.util.List;

public class Mydbhandler extends SQLiteOpenHelper {
    private static final int VERSION =1;
    private static final String NAME="todoListDatabase";
    private static final String TODO_TABLE="todo";
    private static final String ID="id";
    private static final String TASK="task";
    private static final String STATUS="status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";
    public Mydbhandler(Context context){
        super(context,NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE IF EXISTS "+TODO_TABLE);
        onCreate(db);
    }
//    public void openDatabase() {
//        db = this.getWritableDatabase();
//    }
    public void insertTask(Model m){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(TASK,m.getTask());
        cv.put(STATUS,0);
        db.insert(TODO_TABLE,null,cv);
    }
    public List<Model> getAllTasks(){
        SQLiteDatabase db=this.getReadableDatabase();
        List<Model> m1=new ArrayList<>();
        Cursor cur=null;
        db.beginTransaction();
        try{
            cur=db.query(TODO_TABLE,null,null,null,null,null,null);
            if(cur!=null){
                if(cur.moveToFirst()){
                    do{
                        Model task = new Model();
                        task.setId(cur.getInt(cur.getColumnIndexOrThrow(ID)));
                        task.setTask(cur.getString(cur.getColumnIndexOrThrow(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndexOrThrow(STATUS)));
                        m1.add(task);
                    }while(cur.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            assert cur!=null;
            cur.close();
        }
        return m1;
    }
    public void updatestatus(int id,int status){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(STATUS,status);
        db.update(TODO_TABLE,cv,ID+"= ?",new String[]{String.valueOf(id)});
    }
    public void updatetask(int id,String task){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(TASK,task);
        db.update(TODO_TABLE,cv,ID+"= ?",new String[]{String.valueOf(id)});
    }
    public void deletetask(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TODO_TABLE,ID+"= ?",new String[]{String.valueOf(id)});
    }
}
