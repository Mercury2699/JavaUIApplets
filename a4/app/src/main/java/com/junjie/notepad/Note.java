package com.junjie.notepad;

import java.util.Date;

public class Note implements Comparable<Date> {
    Date creationTime;
    String title;
    String text;

    Note(Date d, String titl, String txt){
        creationTime = d;
        title = titl;
        text = txt;
    }

    public boolean isEmpty(){
        return title.isEmpty() && text.isEmpty();
    }

    @Override
    public int compareTo(Date d) {
        return creationTime.compareTo(d);
    }
}
