package com.RickProjects.myNotes.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.RickProjects.myNotes.daos.TodoDAO;
import com.RickProjects.myNotes.databases.NoteDatabase;
import com.RickProjects.myNotes.models.Todo;

import java.util.List;

public class ToDoRepository {

    private TodoDAO mDAO;
    private LiveData<List<Todo>> mNotes;

    public ToDoRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        mDAO = database.checklistDAO();
        mNotes = mDAO.getChecklists();
    }

    public void insert(Todo todo) {
        new ToDoRepository.InsertTask(mDAO).execute(todo);
    }

    public void update(Todo todo) {
        new ToDoRepository.UpdateTask(mDAO).execute(todo);
    }

    public void delete(Todo todo) {
        new ToDoRepository.DeleteTask(mDAO).execute(todo);
    }

    public void deleteAll() {
        new ToDoRepository.DeleteAllTask(mDAO).execute();
    }

    public LiveData<List<Todo>> getNotes() {
        return mNotes;
    }

    private static class InsertTask extends AsyncTask<Todo, Void, Void> {
        private TodoDAO mDAO;

        public InsertTask(TodoDAO DAO) {
            mDAO = DAO;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            mDAO.insert(todos[0]);
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Todo, Void, Void> {
        private TodoDAO mDAO;

        public DeleteTask(TodoDAO DAO) {
            mDAO = DAO;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            mDAO.delete(todos[0]);
            return null;
        }
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void> {
        private TodoDAO mDAO;

        public DeleteAllTask(TodoDAO DAO) {
            mDAO = DAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDAO.deleteAll();
            return null;
        }
    }

    private static class UpdateTask extends AsyncTask<Todo, Void, Void> {
        private TodoDAO mDAO;

        public UpdateTask(TodoDAO DAO) {
            mDAO = DAO;
        }

        @Override
        protected Void doInBackground(Todo... todos) {
            mDAO.update(todos[0]);
            return null;
        }
    }
}
