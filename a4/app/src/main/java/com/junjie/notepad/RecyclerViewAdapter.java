package com.junjie.notepad;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.NoteHolder> {
    private final Context context;
    private final ArrayList<Note> notes;
    private NoteEventListener listener;

    public RecyclerViewAdapter(Context c, ArrayList<Note> n) {
        context = c;
        notes = n;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note note = notes.get(position);
        if (note != null) {
            String title = note.title;
            String txt = note.text;
            if(title.isEmpty()) {
                title = txt;
                txt = "";
            }
            if(title.length() > 10) title = title.substring(0,9)+"...";
            if(txt.length() > 15) txt = txt.substring(0,14)+"...";
            holder.noteTitle.setText(title);
            holder.noteText.setText(txt);
            holder.itemView.findViewById(R.id.linearLayout).setOnClickListener(view -> listener.onNoteClick(note));
            holder.itemView.findViewById(R.id.deleteIcon).setOnClickListener(view -> listener.onDeleteClick(note));
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteText;

        public NoteHolder(View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteText = itemView.findViewById(R.id.note_text);
        }
    }

    public void setListener(NoteEventListener l) {
        listener = l;
    }

    public interface NoteEventListener {
        void onNoteClick(Note note);
        void onDeleteClick(Note note);
    }
}