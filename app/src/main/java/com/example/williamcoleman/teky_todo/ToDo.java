package com.example.williamcoleman.teky_todo;

import java.util.Date;

/**
 * Created by williamcoleman on 10/26/16.
 */

public class ToDo implements Comparable<ToDo>{

    private String name;
    private String text;
    private Date dateModified;
    private String category;
    private Date dueDate;


        public ToDo (String name, String text, Date dateModified, String category, Date dueDate){
            this.name = name;
            this.text = text;
            this.dateModified = dateModified;
            this.category = category;
            this.dueDate = dueDate;

        }



    public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Date getDateModified() {
            return dateModified;
        }

        public void setDateModified(Date dateModified) {
            this.dateModified = dateModified;
        }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



        @Override
        public int compareTo(ToDo another) {
            //return another.getDateModified().compareTo(getDateModified());
            return getCategory().compareTo(another.getCategory());
        }
    }
