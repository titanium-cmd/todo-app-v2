package com.example.todo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class BottomSheetDialog extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener{
    private BottomSheetListener mListener;
    private ListView todoList;
    private TextView nullText;
    private String title, description;
    private DatabaseManager databaseManager;
    private TextInputLayout descText;
    private Button addTodoBtn;
    private int hours, minutes;
    private int day, month, year;
    private View view;
    private static final String TODO_TITLE = "task title";
    private static final String TODO_DESC = "task description";
    private EditText dateTimeEditText;
    private String dateSet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        descText = view.findViewById(R.id.descriptionText);
        addTodoBtn = view.findViewById(R.id.addTodoBtn);
        
        databaseManager = new DatabaseManager(getContext());

        addTodoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = getTitleText().getEditText().getText().toString();
                description = descText.getEditText().getText().toString();
                mListener.onTodoAdded(title, description);
                TodoAdapter adapter = new TodoAdapter(getContext(), databaseManager.getTodoDetails(), getTodoList(), getNullText());
                getTodoList().setAdapter(adapter);
                dismiss();
            }
        });

        TextInputLayout pickDateTextInput = view.findViewById(R.id.dateTimeText);
        dateTimeEditText = pickDateTextInput.getEditText();
        dateTimeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(getContext(), BottomSheetDialog.this, year, month, day);
                datePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        showCancelDialog();
                    }
                });
                datePicker.show();
            }
        });
        return view;
    }

    public interface BottomSheetListener{
        void onTodoAdded(String title, String desc);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (BottomSheetListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() +" must implement interface");
        }
    }

    public TextInputLayout getTitleText() { return view.findViewById(R.id.titleText); }
    public TextInputLayout getDescText() { return descText; }
    public Button getAddTodoBtn() { return addTodoBtn; }

    public TextView getNullText() { return nullText; }
    public void setNullText(TextView nullText) { this.nullText = nullText; }

    public ListView getTodoList() { return todoList; }
    public void setTodoList(ListView todoList) { this.todoList = todoList; }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateSet = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String time = timePicker.getCurrentHour() +":"+ timePicker.getCurrentMinute();
                dateSet.concat(time);
                Toast.makeText(getContext(), dateSet, Toast.LENGTH_LONG).show();
                dateTimeEditText.setText(dateSet+"\n"+time);
            }
        }, hour, minute, true);
        timePicker.show();
    }

    private void showCancelDialog(){
        final AlertDialog.Builder cancelAlert = new AlertDialog.Builder(getContext());
        cancelAlert.setMessage("Cancelling the dialog will discard your task. Discard?");
        cancelAlert.setTitle("Cancel Task");
        cancelAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelAlert.create().dismiss();
            }
        });
        cancelAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelAlert.create().dismiss();
            }
        });

        cancelAlert.create().show();
    }
}
