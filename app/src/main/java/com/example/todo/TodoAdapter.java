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
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class TodoAdapter extends ArrayAdapter<TodoDetails>{
    private ListView todoList;
    private TextView nullText;
    private OnTaskChangeListener taskChangeListener;
    private FirebaseOperations operations;
    private TextInputLayout descInput, titleInput, timeToAccomplish;
    private Button updateBtn;

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
        operations = new FirebaseOperations(getContext(), todoList, nullText);
        checkTaskAvailability();

        TextView task_id = convertView.findViewById(R.id.task_no);
        final TextView title = convertView.findViewById(R.id.title);
        final TextView desc = convertView.findViewById(R.id.description);
        TextView timeAdded = convertView.findViewById(R.id.currentTimeText);
        ImageView popupIcon = convertView.findViewById(R.id.popupMenu);
        //popupIcon.setFocusable(true);
        todoList.setFocusable(false);
        TextView isAccomplished = convertView.findViewById(R.id.accomplished);

        if (details != null) {
            taskChangeListener.onTaskChange(getCount());
            Log.v("onTask (adapter)", getCount()+"");
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

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.delete_task:
                            operations.deleteTodoItem(details.getTodoId());
                            checkTaskAvailability();
                            Snackbar.make(((AppCompatActivity) getContext()).findViewById(R.id.mainContainer), "Task deleted successfully", Snackbar.LENGTH_SHORT).show();
                            break;
                        case R.id.edit_task:
                            View view = ((FragmentActivity)getContext()).getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
                            final BottomSheetDialog bottomSheetDialog;
                            bottomSheetDialog = new BottomSheetDialog(getContext());
                            bottomSheetDialog.setContentView(view);
                            bottomSheetDialog.show();

                            titleInput = bottomSheetDialog.getWindow().findViewById(R.id.titleText);
                            descInput = bottomSheetDialog.getWindow().findViewById(R.id.descriptionText);
                            timeToAccomplish = bottomSheetDialog.getWindow().findViewById(R.id.dateTimeText);
                            operations.getTodoRef().child(details.getTodoId()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    TodoDetails todoDetails = dataSnapshot.getValue(TodoDetails.class);
                                    if (todoDetails != null){
                                        titleInput.getEditText().setText(todoDetails.getTodoTitle());
                                        descInput.getEditText().setText(todoDetails.getTodoDesc());
                                        timeToAccomplish.getEditText().setText(todoDetails.getTimeToAccomplish());

                                        updateBtn = bottomSheetDialog.getWindow().findViewById(R.id.addTodoBtn);
                                        updateBtn.setText("Update task");
                                        TextView dialogTitle = bottomSheetDialog.findViewById(R.id.sheetTitle);
                                        dialogTitle.setText("UPDATE TODO ITEM");

                                        updateBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String title = titleInput.getEditText().getText().toString();
                                                String desc = descInput.getEditText().getText().toString();
                                                String dateTime = timeToAccomplish.getEditText().getText().toString();

                                                TodoDetails todo = new TodoDetails(details.getTodoId(), title, desc, details.getIsAccomplished(), dateTime, getCurrentDate());
                                                operations.getTodoRef().child(details.getTodoId()).setValue(todo);
                                                bottomSheetDialog.dismiss();
                                                Snackbar.make(((AppCompatActivity) getContext()).findViewById(R.id.mainContainer), "Task edited successfully", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            break;
                        case R.id.accomplished_task:
                            TodoDetails todoDetails = new TodoDetails(details.getTodoId(), details.getTodoTitle(), details.getTodoDesc(), "accomplished", details.getTimeToAccomplish(), getCurrentDate());
                            operations.getTodoRef().child(details.getTodoId()).setValue(todoDetails);
                            Menu menu = popupMenu.getMenu();
                            menu.removeItem(R.id.accomplished_task);
                            Snackbar.make(((AppCompatActivity) getContext()).findViewById(R.id.mainContainer), "Task edited successfully", Snackbar.LENGTH_SHORT).show();
                            break;
                        case R.id.share_task:

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

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }

    private void checkTaskAvailability() {
        if(getCount() > 0){
            nullText.setVisibility(View.INVISIBLE);
        }else{
            nullText.setVisibility(View.VISIBLE);
        }
    }

    private void attachTaskInterface(){
        try{
            taskChangeListener = (OnTaskChangeListener) getContext();
        }catch (ClassCastException e){
            throw new ClassCastException(getContext().toString() +" must implement interface");
        }
    }
}
