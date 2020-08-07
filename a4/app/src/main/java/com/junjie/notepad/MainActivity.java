package com.junjie.notepad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

import java.io.*;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.NoteEventListener {
    Model m;
    Note editing;
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
            editing = new Note(new Date(System.currentTimeMillis()), "", "");
            noteIntent.putExtra("Note", editing);
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
        LinearLayoutManager l =new LinearLayoutManager(this);
        l.setReverseLayout(true);
        l.setStackFromEnd(true);
        l.scrollToPosition(2147483647);
        recyclerView.setLayoutManager(l);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Note edited = null;
                if (data != null) {
                    edited = (Note) data.getExtras().getSerializable("Note");
                }
                if (edited != null) {
                    System.err.println("Receiving edited note...");
                    System.err.println(edited.title);
                    System.err.println(edited.text);
                    if (edited.isEmpty()){
                        System.err.println("Empty note returned ...");
                        Toast t = Toast.makeText(this, "This note is empty, it is deleted." ,Toast.LENGTH_SHORT);
                        t.show();
                        m.notes.remove(editing);
                    } else {
                        int i = m.notes.indexOf(editing);
                        System.err.println("Note returned ...");
                        if (i != -1) {
                            System.err.println("Modifying existing notes ...");
                            m.notes.get(i).title = edited.title;
                            m.notes.get(i).text = edited.text;
                        } else {
                            System.err.println("Adding new notes ...");
                            m.notes.add(edited);
                        }
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        try (FileOutputStream fos = openFileOutput("notes.bin", Context.MODE_PRIVATE)) {
            m.save(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println("Saving notes to file...");
        System.err.println("Notes Array Size: " + m.notes.size());
    }

    @Override
    public void onNoteClick(Note note) {
        editing = note;
        noteIntent.putExtra("Note", note);
        noteIntent.setAction(Intent.ACTION_EDIT);
        startActivityForResult(noteIntent, 0);
        System.err.println("Editing notes...");
        System.err.println("Notes Array Size: " + m.notes.size());
    }

    @Override
    public void onDeleteClick(Note note) {
        m.notes.remove(note);
        adapter.notifyDataSetChanged();
    }
}