package com.junjie.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.AppCompatImageButton;

public class EditNoteActivity extends AppCompatActivity {
    private Note editing;
    private EditText titleBar;
    private EditText noteField;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        editing = (Note) extras.getSerializable("Note");
        setContentView(R.layout.activity_edit_note);
        titleBar = findViewById(R.id.titleField);
        noteField = findViewById(R.id.noteField);
        Toolbar editToolbar = findViewById(R.id.editToolbar);
        setSupportActionBar(editToolbar);
        AppCompatImageButton saveButton = findViewById(R.id.saveButton);
        titleBar.setText(editing.title);
        noteField.setText(editing.text);
        saveButton.setOnClickListener(view -> returnNoteIntent());
    }

    @Override
    protected void onStop() {
        super.onStop();
        returnNoteIntent();
    }

    private void returnNoteIntent() {
        editing.title = titleBar.getText().toString();
        editing.text = noteField.getText().toString();
        System.err.println("Transferring note back...");
        System.err.println(editing.title);
        System.err.println(editing.text);
        Intent intent = new Intent();
        intent.putExtra("Note", editing);
        setResult(RESULT_OK, intent);
        finish();
    }
}
