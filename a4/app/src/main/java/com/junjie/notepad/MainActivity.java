package com.junjie.notepad;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.*;

public class MainActivity extends AppCompatActivity {
    Model m;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> setContentView(R.layout.activity_edit_note));

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