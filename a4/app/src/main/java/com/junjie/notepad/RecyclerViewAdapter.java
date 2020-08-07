package com.junjie.notepad;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.NoteHolder> {
    private final Context context;
    private final ArrayList<Note> notes;
    private NoteEventListener listener;

    public RecyclerViewAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        Note note = notes.get(position);
        if (note != null) {
            holder.noteTitle.setText(note.title);
            holder.noteText.setText(note.text);
            holder.itemView.setOnClickListener(view -> listener.onNoteClick(note));
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

    public void setListener(NoteEventListener listener) {
        this.listener = listener;
    }

    public interface NoteEventListener {
        void onNoteClick(Note note);
    }
}