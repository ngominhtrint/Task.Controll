package com.thiendn.coderschool.taskcontrol.dialog;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.thiendn.coderschool.taskcontrol.R;
import com.thiendn.coderschool.taskcontrol.entity.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by thiendn on 09/02/2017.
 */

public class UpdateTaskDialog extends DialogFragment {
    private EditText txtTitle;
    private TextView txtDay;
    private Spinner spinnerPriority;
    private Date date;
    private Button btnOk;
    private Button btnCancel;
    private Button btnPickDay;
    private Task task;

    Calendar myCalendar = Calendar.getInstance();

    public interface UpdateTaskDialogListener {
        void onFinishEditTaskDialog(Task task);
    }

    public UpdateTaskDialog() {

    }

    public static UpdateTaskDialog newInstance(int num, Task task) {
        UpdateTaskDialog f = new UpdateTaskDialog();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        f.setTask(task);
        return f;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    //    public AddTaskDialog(Task task){
//
//    }
    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container, Bundle savedInstanceState) {

        //Tạo giao diện Dialog từ tập tin xml
        View view = inflater.inflate
                (R.layout.fragment_edit_layout, container);
        btnOk = (Button)
                view.findViewById(R.id.btnUpdate);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnPickDay = (Button) view.findViewById(R.id.btnPickday);
        txtTitle = (EditText) view.findViewById(R.id.txt_title);


        //txtPriority = (EditText) view.findViewById(R.id.txt_priority);
        txtDay = (TextView) view.findViewById(R.id.txt_day);
        spinnerPriority = (Spinner) view.findViewById(R.id.spinnerPriority);
        getDialog().setTitle("Update Task");

        List<String> list = new ArrayList<String>();
        list.add("HIGH");
        list.add("MED");
        list.add("LOW");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(dataAdapter);

        txtTitle.setText(task.getTitle());
        txtDay.setText(task.getDeadline());
        int currentSelection = dataAdapter.getPosition(task.getPriority());
        spinnerPriority.setSelection(currentSelection);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTaskDialog.UpdateTaskDialogListener activity = (UpdateTaskDialog.UpdateTaskDialogListener) getActivity();
                String title = txtTitle.getText().toString();
                String priority = spinnerPriority.getSelectedItem().toString();
                String day = txtDay.getText().toString();
                Task task = new Task(title, priority, day, false);
                activity.onFinishEditTaskDialog
                        (task);
                getDialog().dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        btnPickDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return view;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtDay.setText(sdf.format(myCalendar.getTime()));
    }
}
