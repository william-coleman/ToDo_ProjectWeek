package com.example.williamcoleman.teky_todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by williamcoleman on 10/26/16.
 */

public class ToDo_Details extends AppCompatActivity {

    private EditText toDoTitle;
    private EditText toDoText;
    private EditText toDoCategory;
    private EditText toDoDueDate;
    private Button saveButton;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_details);

        toDoTitle = (EditText)findViewById(R.id.toDo_title);
        toDoText = (EditText)findViewById(R.id.toDo_text);
        toDoCategory = (EditText)findViewById(R.id.toDo_category);
        toDoDueDate = (EditText)findViewById(R.id.toDo_dueDate);
        saveButton = (Button)findViewById(R.id.save_button);

        Intent intent = getIntent();

        toDoTitle.setText(intent.getStringExtra("Title"));
        toDoText.setText(intent.getStringExtra("Text"));
        toDoCategory.setText(intent.getStringExtra("Category"));
        toDoDueDate.setText(intent.getStringExtra("DueDate"));
        index =  intent.getIntExtra("Index", -1);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("Title", toDoTitle.getText().toString());
                intent.putExtra("Text", toDoText.getText().toString());
                intent.putExtra("Category", toDoCategory.getText().toString());
                intent.putExtra("DueDate", toDoDueDate.getText().toString());
                intent.putExtra("Index", index);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
