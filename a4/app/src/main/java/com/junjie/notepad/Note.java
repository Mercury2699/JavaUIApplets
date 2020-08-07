package com.junjie.notepad;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable ,Comparable<Date> {
    Date creationTime;
    String title;
    String text;

    Note(Date d, String t, String txt){
        creationTime = d;
        title = t;
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
