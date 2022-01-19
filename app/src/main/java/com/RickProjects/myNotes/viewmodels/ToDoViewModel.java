package com.RickProjects.myNotes.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.RickProjects.myNotes.models.Note;
import com.RickProjects.myNotes.models.Todo;
import com.RickProjects.myNotes.repositories.NoteRepository;
import com.RickProjects.myNotes.repositories.ToDoRepository;

import java.util.List;

public class ToDoViewModel extends AndroidViewModel {
    private final ToDoRepository mRepository;
    private LiveData<List<Todo>> mTodos;

    public ToDoViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ToDoRepository(application);
        mTodos = mRepository.getNotes();
    }

    public void insert(Todo todo) {
        mRepository.insert(todo);
    }

    public void delete(Todo todo) {
        mRepository.delete(todo);
    }

    public void update(Todo todo) {
        mRepository.update(todo);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public LiveData<List<Todo>> getTodos() {
        return mTodos;
    }
}
