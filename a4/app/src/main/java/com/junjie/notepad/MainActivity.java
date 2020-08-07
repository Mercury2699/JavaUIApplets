package com.junjie.notepad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.NoteEventListener {
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
            m.add(n);
            noteIntent.putExtra("Note", n);
            noteIntent.setAction(Intent.ACTION_CREATE_DOCUMENT);
            startActivityForResult(noteIntent, 0);
        });

        try{
            FileInputStream fis;
            fis = openFileInput("notes.bin");
            ObjectInputStream is = new ObjectInputStream(fis);
            m = (Model) is.readObject();
            System.err.println("Loading notes from file...");
            System.err.println("Notes Array Size: " + m.notes.size());
            is.close();
            fis.close();
        } catch (Error | IOException | ClassNotFoundException e){
            e.printStackTrace();
            m = new Model();
            System.err.println("Created new notes data structure...");
        }
        adapter = new RecyclerViewAdapter(this, m.notes);
        adapter.setListener(this);
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Note edited = (Note) data.getExtras().getSerializable("Note");
                if (edited.isEmpty()){
                    System.err.println("Empty note returned ...");
                    Toast t = Toast.makeText(this, "This note is empty, it is deleted." ,Toast.LENGTH_SHORT);
                    t.show();
                    m.notes.remove(edited);
                } else {
                    int i = m.notes.indexOf(edited);
                    System.err.println("Note returned ...");
                    if (i != -1) {
                        m.notes.get(i).title = edited.title;
                        m.notes.get(i).text = edited.text;
                    } else {
                        m.notes.add(edited);
                    }

                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try (FileOutputStream fos = this.openFileOutput("notes.bin", Context.MODE_PRIVATE)) {
            m.save(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println("Saving notes to file...");
        System.err.println("Notes Array Size: " + m.notes.size());
    }

    @Override
    public void onNoteClick(Note note) {
        noteIntent.putExtra("Note", note);
        noteIntent.setAction(Intent.ACTION_EDIT);
        startActivity(noteIntent);
        System.err.println("Editing notes...");
        System.err.println("Notes Array Size: " + m.notes.size());
    }

    @Override
    public void onDeleteClick(Note note) {
        m.notes.remove(note);
        adapter.notifyDataSetChanged();
    }
}