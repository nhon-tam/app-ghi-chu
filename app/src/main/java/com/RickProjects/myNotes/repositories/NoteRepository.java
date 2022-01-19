package com.RickProjects.myNotes.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.RickProjects.myNotes.databases.NoteDatabase;
import com.RickProjects.myNotes.daos.NoteDAO;
import com.RickProjects.myNotes.models.Note;

import java.util.List;

public class NoteRepository {

    private NoteDAO mDAO;
    private LiveData<List<Note>> mNotes;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        mDAO = database.noteDAO();
        mNotes = mDAO.getNotes();
    }

    public void insert(Note note) {
        new InsertTask(mDAO).execute(note);
    }

    public void update(Note note) {
        new UpdateTask(mDAO).execute(note);
    }

    public void delete(Note note) {
        new DeleteTask(mDAO).execute(note);
    }

    public void deleteAll() {
        new DeleteAllTask(mDAO).execute();
    }

    public LiveData<List<Note>> getNotes() {
        return mNotes;
    }

    private static class InsertTask extends AsyncTask<Note, Void, Void> {
        private NoteDAO mDAO;

        public InsertTask(NoteDAO DAO) {
            mDAO = DAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mDAO.insert(notes[0]);
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Note, Void, Void> {
        private NoteDAO mDAO;

        public DeleteTask(NoteDAO DAO) {
            mDAO = DAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mDAO.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void> {
        private NoteDAO mDAO;

        public DeleteAllTask(NoteDAO DAO) {
            mDAO = DAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDAO.deleteAll();
            return null;
        }
    }

    private static class UpdateTask extends AsyncTask<Note, Void, Void> {
        private NoteDAO mDAO;

        public UpdateTask(NoteDAO DAO) {
            mDAO = DAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mDAO.update(notes[0]);
            return null;
        }
    }
}
