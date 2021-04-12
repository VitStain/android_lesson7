package com.example.makingnotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class NoteBlankFragment extends Fragment {

    static final String ARG_NOTE = "currentNote";
    private Notes note;

    // Фабричный метод создания фрагмента
    // Фрагменты рекомендуется создавать через фабричные методы
    public static NoteBlankFragment newInstance(Notes note) {
        NoteBlankFragment fragment = new NoteBlankFragment();  // создание

        // Передача параметра
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Таким способом можно получить головной элемент из макета
        View view = inflater.inflate(R.layout.fragment_note_blank, container, false);
        // Установить название заметки, описание заметки, дата создания заметки
        TextView titleText = view.findViewById(R.id.note_title);
        TextView noteContentText = view.findViewById(R.id.note_content);
        TextView dateOfCreationText = view.findViewById(R.id.note_date_of_creation);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
        dateOfCreationText.setText(String.format("%s", formatter.format(note.getCreationDate().getTime())));
        titleText.setText(note.getTitle());
        noteContentText.setText(note.getContent());
        return view;
    }
}