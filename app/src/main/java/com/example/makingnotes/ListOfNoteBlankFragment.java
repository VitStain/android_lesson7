package com.example.makingnotes;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class ListOfNoteBlankFragment extends Fragment {

    private boolean isLandscape;
    private Notes[] notes;
    private Notes currentNote;

    // При создании фрагмента укажем его макет
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_note_blank, container, false);
    }

    // вызывается после создания макета фрагмента, здесь мы проинициализируем список
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }
    // создаем список заметок на экране из массива в ресурсах
    private void initList(View view) {
        notes = new Notes[]{
                new Notes(getString(R.string.one_note_title), getString(R.string.one_note_content), Calendar.getInstance()),
                new Notes(getString(R.string.two_note_title), getString(R.string.two_note_content), Calendar.getInstance()),
                new Notes(getString(R.string.three_note_title), getString(R.string.three_note_content), Calendar.getInstance()),
        };

        // В этом цикле создаем элемент TextView,
        // заполняем его значениями,
        // и добавляем на экран.
        // Кроме того, создаем обработку касания на элемент
        for (Notes note : notes) {
            Context context = getContext();
            if (context != null) {
                LinearLayout linearView = (LinearLayout) view;
                TextView firstTextView = new TextView(context);
                TextView secondTextView = new TextView(context);
                firstTextView.setText(note.getTitle());
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
                secondTextView.setText(formatter.format(note.getCreationDate().getTime()));
                linearView.addView(firstTextView);
                firstTextView.setTextSize(25);
                secondTextView.setTextSize(15);
                firstTextView.setTextColor(Color.WHITE);
                secondTextView.setTextColor(Color.WHITE);
                linearView.addView(secondTextView);
                firstTextView.setPadding(10, 55, 0, 0);
                secondTextView.setPadding(10, 5, 0, 0);
                firstTextView.setOnClickListener(v -> initCurrentNote(note));
                secondTextView.setOnClickListener(v -> initCurrentNote(note));
            }
        }
    }

    // Сохраним текущую позицию (вызывается перед выходом из фрагмента)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(NoteBlankFragment.ARG_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }
    private void initCurrentNote(Notes note) {
        currentNote = note;
        showNote(note);
    }
    // activity создана, можно к ней обращаться. Выполним начальные действия
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Определение, можно ли будет расположить заметку рядом в другом фрагменте
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        // Если это не первое создание, то восстановим текущую позицию
        if (savedInstanceState != null) {
            // Восстановление текущей позиции.
            currentNote = savedInstanceState.getParcelable(NoteBlankFragment.ARG_NOTE);
        } else {
            // Если воccтановить не удалось, то сделаем объект с первым индексом
            currentNote = notes[0];
        }

        // Если можно нарисовать рядом, то сделаем это
        if (isLandscape) {
            showLandNote(currentNote);
        }
    }

    private void showNote(Notes currentNote) {
        if (isLandscape) {
            showLandNote(currentNote);
        } else {
            showPortNote(currentNote);
        }
    }

    // Показать заметку в ландшафтной ориентации
    private void showLandNote(Notes currentNote) {
        // Создаем новый фрагмент с текущей позицией для вывода заметки
        NoteBlankFragment fragment = NoteBlankFragment.newInstance(currentNote);

        // Выполняем транзакцию по замене фрагмента
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.note_layout, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
    }


    private void showPortNote(Notes currentNote) {
        NoteBlankFragment fragment = NoteBlankFragment.newInstance(currentNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("list_fragment");
        fragmentTransaction.replace(R.id.list_of_notes_fragment_container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}