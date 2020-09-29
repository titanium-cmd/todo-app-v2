package com.example.todo;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener {
    private DatabaseManager databaseManager;
    private ListView todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseManager = new DatabaseManager(this);
        todoList = findViewById(R.id.todoList);

        final TextView noneAvailable = findViewById(R.id.nullText);
        if(databaseManager.getTodoDetails().size() > 0){
            noneAvailable.setVisibility(View.INVISIBLE);
        }else{
            noneAvailable.setVisibility(View.VISIBLE);
        }

        TodoAdapter adapter = new TodoAdapter(this, databaseManager.getTodoDetails(), todoList, noneAvailable);
        todoList.setAdapter(adapter);
        FloatingActionButton addTodoFab = findViewById(R.id.addTodoFAB);
        addTodoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheet = new BottomSheetDialog();
                bottomSheet.setTodoList(todoList);
                bottomSheet.setNullText(noneAvailable);
                bottomSheet.show(getSupportFragmentManager(), "todoSheet");
            }
        });
    }

    @Override
    public void onTodoAdded(String title, String desc) {
        TodoDetails todoDetails = new TodoDetails();
        todoDetails.setTodoDesc(desc);
        todoDetails.setAccomplished("Unaccomplished");
        todoDetails.setTodoTitle(title);
        databaseManager.addTodo(todoDetails);
        Snackbar.make(findViewById(R.id.mainContainer), "Task added to list", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseManager.close();
    }


}
