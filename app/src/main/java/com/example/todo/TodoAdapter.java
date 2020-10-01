package com.example.todo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TodoAdapter extends ArrayAdapter<TodoDetails>{
    private ListView todoList;
    private TextView nullText;
    private OnTaskChangeListener taskChangeListener;
    private FirebaseOperations operations;

    public interface OnTaskChangeListener {
        void onTaskChange(int taskTotal);
    }

    public TodoAdapter(@NonNull Context context, @NonNull List<TodoDetails> list, ListView todoList, TextView nullText) {
        super(context, 0, list);
        this.todoList = todoList;
        this.nullText = nullText;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_todo_list, parent, false);
        final TodoDetails details = getItem(position);
        attachTaskInterface();
        operations = new FirebaseOperations(getContext());
        checkTaskAvailability();

        TextView task_id = convertView.findViewById(R.id.task_no);
        final TextView title = convertView.findViewById(R.id.title);
        TextView desc = convertView.findViewById(R.id.description);
        TextView timeAdded = convertView.findViewById(R.id.currentTimeText);
        ImageView popupIcon = convertView.findViewById(R.id.popupMenu);
        //popupIcon.setFocusable(true);
        todoList.setFocusable(false);
        TextView isAccomplished = convertView.findViewById(R.id.accomplished);

        if (details != null) {
            taskChangeListener.onTaskChange(operations.getTodoSize());
            timeAdded.setText(details.getCurrentTime());
            task_id.setText(details.getTodoId());
            title.setText(details.getTodoTitle());
            desc.setText(details.getTodoDesc());
            isAccomplished.setText(details.getIsAccomplished());
            if(details.getIsAccomplished().equals("unaccomplished")){
                isAccomplished.setTextColor(Color.parseColor("#97610606"));
            }else{
                isAccomplished.setTextColor(Color.parseColor("#C4055008"));
            }

            final PopupMenu popupMenu = new PopupMenu(getContext(), popupIcon);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                private TextInputLayout descInput;
                private TextInputLayout titleInput;

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.delete_task:
                            operations.deleteTodoItem(details.getTodoId());
                            updateList();
                            checkTaskAvailability();
                            Snackbar.make(((AppCompatActivity) getContext()).findViewById(R.id.mainContainer), "Task deleted successfully", Snackbar.LENGTH_SHORT).show();
                            break;
                        case R.id.edit_task:
                            View view = ((FragmentActivity)getContext()).getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
                            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                            bottomSheetDialog.setContentView(view);

                            titleInput = bottomSheetDialog.getWindow().findViewById(R.id.titleText);
                            descInput = bottomSheetDialog.getWindow().findViewById(R.id.descriptionText);
                            Button updateBtn = bottomSheetDialog.getWindow().findViewById(R.id.addTodoBtn);
                            updateBtn.setText("Update task");
                            TextView dialogTitle = bottomSheetDialog.findViewById(R.id.sheetTitle);
                            dialogTitle.setText("UPDATE TODO ITEM");
                            bottomSheetDialog.show();

                            updateBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String title = titleInput.getEditText().getText().toString();
                                    String desc = descInput.getEditText().getText().toString();

                                    Snackbar.make(((AppCompatActivity) getContext()).findViewById(R.id.mainContainer), "Task edited successfully", Snackbar.LENGTH_SHORT).show();
                                    updateList();
                                    bottomSheetDialog.dismiss();
                                }
                            });
                            break;
                        case R.id.accomplished_task:
                            TodoDetails todoDetails = new TodoDetails(details.getTodoId(), details.getTodoTitle(), details.getTodoDesc(), "Accomplished");
                            Menu menu = popupMenu.getMenu();
                            menu.removeItem(R.id.accomplished_task);
                            Snackbar.make(((AppCompatActivity) getContext()).findViewById(R.id.mainContainer), "Task edited successfully", Snackbar.LENGTH_SHORT).show();
                            updateList();
                            break;
                    }
                    return true;
                }
            });

            popupIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });
        }
        return convertView;
    }

    private void checkTaskAvailability() {
        if(operations.getTodoSize() > 0){
            nullText.setVisibility(View.INVISIBLE);
        }else{
            nullText.setVisibility(View.VISIBLE);
        }
    }

    private void updateList(){
        notifyDataSetChanged();
//        TodoAdapter adapter = new TodoAdapter(getContext(), databaseManager.getTodoDetails(), todoList, nullText);
//        todoList.setAdapter(adapter);
    }

    private void attachTaskInterface(){
        try{
            taskChangeListener = (OnTaskChangeListener) getContext();
        }catch (ClassCastException e){
            throw new ClassCastException(getContext().toString() +" must implement interface");
        }
    }
}
