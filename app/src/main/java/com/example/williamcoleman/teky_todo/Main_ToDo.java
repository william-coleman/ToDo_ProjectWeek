package com.example.williamcoleman.teky_todo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Main_ToDo extends AppCompatActivity {


    // creates a variable named toDolist that is a type known as a list-view
    private ListView toDoList;
    // this establishes the notes array adapter and other variables
    private ToDo_ArrayAdapter todosArrayAdapter;
    private ArrayList<ToDo> todosArray;
    private SharedPreferences todosPrefs;

    // this is the on create method that tells the device what to do when the activity is started.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this line sets the inital screen to the resource file named activity notes in the R class this is like going to get our template for the screen.
        setContentView(R.layout.activity_main__to_do);
        // this is part of the sorting feature that uses the preferences
        todosPrefs = getPreferences(Context.MODE_PRIVATE);
        // this calls the method setupNotes
        setupNotes();
        //this sorts the notes after the notes have been set up. this line is also used elsewhere and sorts the notes before this line happens. may may remove
        Collections.sort(todosArray);
        // this line ties the variable to the actual view on the screen (Activity)
        toDoList = (ListView) findViewById(R.id.listView);

        //  this will update the contents of the view named notes_list_item from the notesArray
        todosArrayAdapter = new ToDo_ArrayAdapter(this, R.layout.activity_todo_list, todosArray);
        // this connects the adapter to the variable notesList (notesArrayAdapter) is what it is set to.
        toDoList.setAdapter(todosArrayAdapter);
        // this turns the view into a clickable element
        toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // if the element is clicked create the intent
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //this selects the note from the array at the position established by the variable named position.
                ToDo toDo = todosArray.get(position);
                //create a new intent named intent  from NotesActivity to NoteDetailActivity. this opens the wormhole between the two.
                Intent intent = new Intent(Main_ToDo.this, ToDo_Details.class);
                //this sends the information of the title through the wormhole. we tell the other side that the next bit of information is labeled Title and we GET the actual value
                intent.putExtra("Title", toDo.getName());
                //this sends the information of the text through the wormhole. we tell the other side that the next bit of information is labeled text and we GET the actual value
                intent.putExtra("Text", toDo.getText());
                intent.putExtra("Category", toDo.getCategory());
                intent.putExtra("DueDate", toDo.getDueDate());
                // we also send the index position. we will use this later.
                intent.putExtra("Index", position);


                //this actually fires the intent causing the wormhole to open and push the info
                startActivityForResult(intent, 1);

            }
        });

        //this is for the delete method. it sets the option for a long click and what to do if a long click happens.
        toDoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            //what to do if the view gets a long click
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //builds an alert box
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Main_ToDo.this);
                // gives this box a title & message. the buttons are also established.
                alertBuilder.setTitle("Delete");
                alertBuilder.setMessage("You sure?");
                // if user clicks cancel then do nothing
                alertBuilder.setNegativeButton("Cancel", null);
                //if user clicks the delete then do some stuff
                alertBuilder.setPositiveButton("Delete",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToDo toDo = todosArray.get(position);
                        deleteFile("##" + toDo.getName());
                        todosArray.remove(position);
                        todosArrayAdapter.updateAdapter(todosArray);
                    }
                });
                alertBuilder.create().show();
                return true;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            int index = data.getIntExtra("Index", -1);
//TODO Please come back and enter date not hard coded
            ToDo toDo = new ToDo(data.getStringExtra("Title"),
                    data.getStringExtra("Text"), new Date(), data.getStringExtra("Category"), new Date());
            if (index == -1){
                todosArray.add(toDo);


            }else {
                ToDo oldtoDo = todosArray.get(index);
                todosArray.set(index, toDo);
                //  writeFile(toDo);
                if (!oldtoDo.getName().equals(toDo.getName())){
                    File oldFile = new File(this.getFilesDir(), "##" + oldtoDo.getName());
                    File newFile = new File(this.getFilesDir(), "##" + toDo.getName());
                    oldFile.renameTo(newFile);
                }

            }
            Collections.sort(todosArray);
            todosArrayAdapter.updateAdapter(todosArray);
        }


    }



    private void setupNotes() {
        todosArray = new ArrayList<>();
        if (todosPrefs.getBoolean("firstRun", true)) {
            SharedPreferences.Editor editor = todosPrefs.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();

            ToDo note1 = new ToDo("Note 1", "This is a note", new Date(), "", new Date());
            todosArray.add(note1);
            todosArray.add(new ToDo("Note 2", "This is another note", new Date(), "", new Date()));
            todosArray.add(new ToDo("Note 3", "This is another note", new Date(), "", new Date()));

            for (ToDo toDo : todosArray) {

                writeFile(toDo);
            }
        } else {
            File[] filesDir = this.getFilesDir().listFiles();
            for (File file : filesDir) {
                FileInputStream inputStream = null;
                String title = file.getName();
                if (!title.startsWith("##")){
                    continue;
                }else {
                    title = title.substring(2,title.length());
                }
                Date date = new Date(file.lastModified());
                String text = "";
                try {
                    inputStream = openFileInput("##" + title);
                    byte[] input = new byte[inputStream.available()];
                    while (inputStream.read(input) != -1) {
                    }
                    text += new String(input);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {

                        inputStream.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                todosArray.add(new ToDo(title, text, date, "", date));
            }
        }
    }

    private void writeFile(ToDo ToDo) {
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput("##" + ToDo.getName(), Context.MODE_PRIVATE);
            outputStream.write(ToDo.getText().getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException ioe) {
            } catch (NullPointerException npe) {
            } catch (Exception e) {
            }
        }
    }


    //menu goes here
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Main_ToDo.this, ToDo_Details.class);
            intent.putExtra("Title", "");
            intent.putExtra("Text", "");
                startActivityForResult(intent, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // This example shows an Activity, but you would use the same approach if
// you were subclassing a View.
    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d("-----","Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.d("-----","Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP) :
                Intent intent = new Intent(Main_ToDo.this, ToDo_Details.class);
                intent.putExtra("Title", "");
                intent.putExtra("Text", "");
                startActivityForResult(intent, 1);
                Log.d("-----","Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                Log.d("-----","Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.d("-----","Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }


}

