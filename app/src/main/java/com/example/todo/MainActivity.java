package com.example.todo;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener, TodoAdapter.OnTaskChangeListener {
    private ListView todoList;
    private FirebaseOperations operations;
    private TextView noneAvailable;
    private TextView taskSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoList = findViewById(R.id.todoList);
        taskSize = findViewById(R.id.todoSizeText);
        noneAvailable = findViewById(R.id.nullText);
        operations = new FirebaseOperations(this, todoList, noneAvailable);
        operations.loadTodoList();

        if(operations.getTodoSize() > 0){
            noneAvailable.setVisibility(View.INVISIBLE);
        }else{
            noneAvailable.setVisibility(View.VISIBLE);
        }

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

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
    public void onTodoAdded(String title, String desc, String timeToAccomplish, String currentTime, String isAccomplished) {
        UUID uuid = UUID.randomUUID();
        TodoDetails todoDetails = new TodoDetails();
        todoDetails.setTodoId(uuid.toString());
        todoDetails.setTodoDesc(desc);
        todoDetails.setIsAccomplished(isAccomplished);
        todoDetails.setTodoTitle(title);
        todoDetails.setTimeToAccomplish(timeToAccomplish);
        todoDetails.setCurrentTime(currentTime);
        operations.addTodoItem(todoDetails);
        Snackbar.make(findViewById(R.id.mainContainer), "Task added to list", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskChange(int taskTotal) {
        taskSize.setText("Tasks ("+taskTotal+")");
    }

}
