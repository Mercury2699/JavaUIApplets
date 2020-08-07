package com.junjie.notepad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Model m;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    Intent noteIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteIntent.setClass(this, EditNoteActivity.class);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Note n = new Note(new Date(System.currentTimeMillis()), "", "");
            noteIntent.putExtra("Note", n);
            noteIntent.setAction(Intent.ACTION_CREATE_DOCUMENT);
            startActivity(noteIntent);
        });

        try{
            FileInputStream fis;
            fis = openFileInput("notes.bin");
            ObjectInputStream is = new ObjectInputStream(fis);
            m = (Model) is.readObject();
            is.close();
            fis.close();
        } catch (Error | IOException | ClassNotFoundException e){
            e.printStackTrace();
            m = new Model();
        }
        adapter = new RecyclerViewAdapter(this, m.notes);
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("notes.bin", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        m.save(fos);
        super.onDestroy();
    }
}