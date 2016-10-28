package com.example.williamcoleman.teky_todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by williamcoleman on 10/26/16.
 */

public class ToDo_ArrayAdapter extends ArrayAdapter<ToDo> {

        private int resource;
        private ArrayList<ToDo> todos;
        private LayoutInflater inflater;
        private SimpleDateFormat formatter;

        public ToDo_ArrayAdapter(Context context, int resource, ArrayList<ToDo> objects)
        {
            super(context, resource, objects);
            this.resource = resource;
            this.todos = objects;

            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            formatter = new SimpleDateFormat("MM/dd/yyyy");

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View toDoRow = inflater.inflate(resource, parent, false);

            TextView toDoTitle = (TextView)toDoRow.findViewById(R.id.toDo_title);
            TextView toDoText = (TextView)toDoRow.findViewById(R.id.toDo_text);
            TextView toDoDate = (TextView)toDoRow.findViewById(R.id.toDo_date);

            ToDo toDo = todos.get(position);

            toDoTitle.setText(toDo.getName());
            toDoText.setText(toDo.getText());
            toDoDate.setText(formatter.format(toDo.getDateModified()));

            return toDoRow;
        }
        public void updateAdapter(ArrayList<ToDo> todos){
            this.todos = todos;
            super.notifyDataSetChanged();
        }
    }
