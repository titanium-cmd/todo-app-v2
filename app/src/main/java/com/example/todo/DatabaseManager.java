package com.example.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class DatabaseManager extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "todoDetails.db";
    private static final int DATABASE_VERSION = 1;
    private int todoIdPos, todoTitlePos, todoDescPos, taskAccomplishedPos;
    private SQLiteDatabase sqLiteDatabase;
    public static String[] columns;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(todoInfoEntry.TABLECREATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ todoInfoEntry.TABLE_NAME);
        onCreate(db);
    }

    public final class todoInfoEntry {
        public static final String TABLE_NAME = "todo_table";
        public static final String TODO_ID = "_id";
        public static final String TODO_TITLE = "title";
        public static final String TASK_ACCOMPLISHED = "is_accomplished";
        public static final String TODO_DESCRIPTION = "description";

        private static final String TABLECREATION = "CREATE TABLE "+TABLE_NAME+" (" +
                TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                TODO_TITLE +" TEXT NOT NULL,"+
                TASK_ACCOMPLISHED +" TEXT NOT NULL,"+
                TODO_DESCRIPTION +" TEXT NOT NULL)";
    }

    public void deleteTodo(String todoId){
        sqLiteDatabase = getWritableDatabase();
        String clause = todoInfoEntry.TODO_ID + " = ?";
        String[] args = {todoId};
        sqLiteDatabase.delete(todoInfoEntry.TABLE_NAME, clause, args);
    }

    public void updateTodo(TodoDetails todoDetails){
        sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(todoInfoEntry.TODO_TITLE, todoDetails.getTodoTitle());
        values.put(todoInfoEntry.TODO_DESCRIPTION, todoDetails.getTodoDesc());
        values.put(todoInfoEntry.TASK_ACCOMPLISHED, todoDetails.getAccomplished());
        sqLiteDatabase.update(todoInfoEntry.TABLE_NAME, values, todoInfoEntry.TODO_ID +" = ? ", new String[]{todoDetails.getTodoId()});
    }

    public ArrayList<Map<String, String>> getTodoById(String rowid){
        sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(todoInfoEntry.TABLE_NAME, new String[]{todoInfoEntry.TODO_ID, todoInfoEntry.TODO_ID, todoInfoEntry.TODO_TITLE, todoInfoEntry.TODO_DESCRIPTION, todoInfoEntry.TASK_ACCOMPLISHED},
                todoInfoEntry.TODO_ID +" = ?", new String[]{rowid}, null, null, null);

        Map<String, String> todoRecord = new HashMap<>();
        ArrayList<Map<String, String>> todoDetail = new ArrayList<>();
        todoIdPos = cursor.getColumnIndex(todoInfoEntry.TODO_ID);
        todoTitlePos = cursor.getColumnIndex(todoInfoEntry.TODO_TITLE);
        taskAccomplishedPos = cursor.getColumnIndex(todoInfoEntry.TASK_ACCOMPLISHED);
        todoDescPos = cursor.getColumnIndex(todoInfoEntry.TODO_DESCRIPTION);

        if(cursor.moveToFirst()){
            todoRecord.put(todoInfoEntry.TODO_ID, cursor.getString(todoIdPos));
            todoRecord.put(todoInfoEntry.TODO_TITLE, cursor.getString(todoTitlePos));
            todoRecord.put(todoInfoEntry.TODO_DESCRIPTION, cursor.getString(todoDescPos));
            todoRecord.put(todoInfoEntry.TASK_ACCOMPLISHED, cursor.getString(taskAccomplishedPos));
            todoDetail.add(todoRecord);
        }
        cursor.close();
        return todoDetail;
    }

    public ArrayList<TodoDetails> getTodoDetails(){
        sqLiteDatabase = getReadableDatabase(); //We are getting a readable reference to the database
        //Columns we want to get from the database shouls be in a String arary
        columns = new String[]{todoInfoEntry.TODO_ID, todoInfoEntry.TODO_TITLE, todoInfoEntry.TODO_DESCRIPTION, todoInfoEntry.TASK_ACCOMPLISHED};

        //Cursor is used to know the current position of the row in the database...
        Cursor cursor = sqLiteDatabase.query(true, todoInfoEntry.TABLE_NAME, columns, null, null, null, null, null, null);

        //Getting the postions of every column to be able to loop through
        todoIdPos = cursor.getColumnIndex(todoInfoEntry.TODO_ID);
        todoTitlePos = cursor.getColumnIndex(todoInfoEntry.TODO_TITLE);
        todoDescPos = cursor.getColumnIndex(todoInfoEntry.TODO_DESCRIPTION);
        taskAccomplishedPos = cursor.getColumnIndex(todoInfoEntry.TASK_ACCOMPLISHED);

        ArrayList<TodoDetails> todoDetails = new ArrayList<>();

        while(cursor.moveToNext()){
            TodoDetails details = new TodoDetails();
            details.setTodoId(cursor.getString(todoIdPos));
            details.setTodoTitle(cursor.getString(todoTitlePos));
            details.setTodoDesc(cursor.getString(todoDescPos));
            details.setAccomplished(cursor.getString(taskAccomplishedPos));

            todoDetails.add(details);
        }
        cursor.close();
        return todoDetails;
    }

    public void addTodo(TodoDetails studentDetails){
        ContentValues values = new ContentValues();
        values.put(todoInfoEntry.TODO_TITLE, studentDetails.getTodoTitle());
        values.put(todoInfoEntry.TODO_DESCRIPTION, studentDetails.getTodoDesc());
        values.put(todoInfoEntry.TASK_ACCOMPLISHED, studentDetails.getAccomplished());
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(todoInfoEntry.TABLE_NAME, null, values);
    }
}
