package com.example.todoapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.graphics.Color;
import android.widget.TextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private ArrayList<String> taskList;
    private ArrayAdapter<String> adapter;
    private String selectedPriority = "Low";
    private EditText taskNameEditText;
    private EditText taskDescEditText;
    private Button chooseDateButton;
    private Button chooseStatusButton;
    private Button choosePriorityButton;
    private TextView dueDateTextView;
    private ListView listView;
    private Calendar calendar;
    private int year, month, dayOfMonth;
    private String selectedDueDate = "Not set";
    private String selectedStatus = "Pending";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskNameEditText = findViewById(R.id.taskNameEditText);
        taskDescEditText = findViewById(R.id.taskDescEditText);
        chooseDateButton = findViewById(R.id.chooseDateButton);
        chooseStatusButton = findViewById(R.id.chooseStatusButton);
        choosePriorityButton = findViewById(R.id.choosePriorityButton);
        dueDateTextView = findViewById(R.id.dueDateTextView);
        listView = findViewById(R.id.listView);

        taskList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, android.R.id.text1, taskList);
        listView.setAdapter(adapter);

        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        chooseStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatusSelectionDialog();
            }
        });
        choosePriorityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrioritySelectionDialog();
            }
        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(Calendar.YEAR, year);
                        selectedCalendar.set(Calendar.MONTH, month);
                        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Get the current date
                        Calendar currentDate = Calendar.getInstance();

                        // Check if the selected date is before the current date
                        if (selectedCalendar.before(currentDate)) {
                            // Display a message indicating an invalid due date
                            Toast.makeText(MainActivity.this, "Invalid due date. Please select a date after today.", Toast.LENGTH_SHORT).show();
                        } else {
                            month += 1; // Month index starts from 0
                            selectedDueDate = dayOfMonth + "/" + month + "/" + year;
                            dueDateTextView.setText("Due Date: " + selectedDueDate);
                        }
                    }
                },
                year, month, dayOfMonth
        );
        datePickerDialog.show();
    }

    private void showStatusSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.status, null);
        builder.setView(dialogView);

        final RadioGroup dialogStatusRadioGroup = dialogView.findViewById(R.id.dialogStatusRadioGroup);
        Button okButton = dialogView.findViewById(R.id.okButton);
        Button closeButton = dialogView.findViewById(R.id.closeButton);

        final AlertDialog dialog = builder.create(); // Move the dialog creation here

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonId = dialogStatusRadioGroup.getCheckedRadioButtonId();

                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = dialogView.findViewById(selectedRadioButtonId);
                    selectedStatus = selectedRadioButton.getText().toString();
                }
                dialog.dismiss();
            }
        });


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show(); // Show the dialog after setting up its listeners
    }




    private void showPrioritySelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.priority, null);
        builder.setView(dialogView);

        final RadioGroup dialogPriorityRadioGroup = dialogView.findViewById(R.id.dialogPriorityRadioGroup);
        Button okButton = dialogView.findViewById(R.id.okButton);
        Button closeButton = dialogView.findViewById(R.id.closeButton);

        final AlertDialog dialog = builder.create(); // Move the dialog creation here

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonId = dialogPriorityRadioGroup.getCheckedRadioButtonId();

                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = dialogView.findViewById(selectedRadioButtonId);
                    selectedPriority = selectedRadioButton.getText().toString();
                }
                dialog.dismiss();
            }
        });


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show(); // Show the dialog after setting up its listeners
    }






    public void onAddTaskButtonClick(View view) {
        String taskName = taskNameEditText.getText().toString().trim();
        String taskDesc = taskDescEditText.getText().toString().trim();




        if (!taskName.isEmpty()) {
            String newTask = taskName + "\n" + taskDesc + "\nDue Date: " + selectedDueDate + "\nStatus: " + selectedStatus +"\nPriority: "+ selectedPriority;

            taskList.add(newTask);
            adapter.notifyDataSetChanged();
            taskNameEditText.getText().clear();
            taskDescEditText.getText().clear();
            selectedDueDate = "Not set";
            dueDateTextView.setVisibility(View.GONE);
        }
    }


}

