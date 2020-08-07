package com.junjie.notepad;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditNoteActivity extends AppCompatActivity {
    private Note editing;
    private Button saveButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> setContentView(R.layout.activity_main));
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setContentView(R.layout.activity_main);
    }
}
