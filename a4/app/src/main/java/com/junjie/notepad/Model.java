package com.junjie.notepad;

import java.io.*;
import java.util.ArrayList;

public class Model implements Serializable {
    ArrayList<Note> notes;

    public void add(Note n){
        notes.add(n);
    }

    public void save(FileOutputStream fo) {
        notes.removeIf(Note::isEmpty);
        ObjectOutputStream os;
        try {
            os = new ObjectOutputStream(fo);
            os.writeObject(this);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
