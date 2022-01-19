package com.RickProjects.myNotes.databases;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.RickProjects.myNotes.daos.TodoDAO;
import com.RickProjects.myNotes.daos.NoteDAO;
import com.RickProjects.myNotes.models.Todo;
import com.RickProjects.myNotes.models.Note;

import java.util.Calendar;

@Database(entities = {Note.class, Todo.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;

    public abstract NoteDAO noteDAO();
    public abstract TodoDAO checklistDAO();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(
                            context.getApplicationContext(),
                            NoteDatabase.class,
                            "Note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()
            ;
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBTask(instance).execute();
        }
    };

    private static class PopulateDBTask extends AsyncTask<Note, Void, Void> {
        private NoteDAO mNoteDAO;
        private TodoDAO mTodoDAO;

        public PopulateDBTask(NoteDatabase database) {
            mNoteDAO = database.noteDAO();
            mTodoDAO = database.checklistDAO();
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mNoteDAO.insert(new Note(
                            "Note Title",
                            "Description of the Note",
                            Calendar.getInstance().getTime()
                            ,Note.LOW_PRIORITY
                    )
            );
            mTodoDAO.insert(new Todo(
                            0,
                            "Description of the Note"
                    )
            );
            return null;
        }
    }
}
