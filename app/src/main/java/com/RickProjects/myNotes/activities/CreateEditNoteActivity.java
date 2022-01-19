package com.RickProjects.myNotes.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.RickProjects.myNotes.R;
import com.RickProjects.myNotes.models.Note;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.RickProjects.myNotes.EXTRA_TITLE";
    public static final String EXTRA_ID = "com.RickProjects.myNotes.EXTRA_ID";
    public static final String EXTRA_DESCRIPTION = "com.RickProjects.myNotes.EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE_CREATED = "com.RickProjects.myNotes.EXTRA_DATE_CREATED";
    public static final String EXTRA_PRIORITY = "com.RickProjects.myNotes.EXTRA_PRIORITY";
    int priority = -1;
    private TextInputEditText editTextTitle, editTextDescription;
    private Button buttonLow, buttonMedium, buttonHigh;
//    private CoordinatorLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        editTextTitle = findViewById(R.id.editText_title);
        editTextDescription = findViewById(R.id.editText_description);
//        mLayout = findViewById(R.id.layout_createNotes);
        buttonLow = findViewById(R.id.buttonLow);
        buttonMedium = findViewById(R.id.buttonMedium);
        buttonHigh = findViewById(R.id.buttonHigh);

        buttonLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonLow.setSelected(true);
                buttonHigh.setSelected(false);
                buttonMedium.setSelected(false);
            }
        });

        buttonHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHigh.setSelected(true);
                buttonLow.setSelected(false);
                buttonMedium.setSelected(false);
            }
        });

        buttonMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonMedium.setSelected(true);
                buttonLow.setSelected(false);
                buttonHigh.setSelected(false);
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle(getString(R.string.edit_note));
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            priority = intent.getIntExtra(EXTRA_PRIORITY, Note.LOW_PRIORITY);
            if(priority==Note.LOW_PRIORITY){
                buttonLow.setSelected(true);
            }
            if(priority==Note.MEDIUM_PRIORITY){
                buttonMedium.setSelected(true);
            }
            if(priority==Note.HIGH_PRIORITY){
                buttonHigh.setSelected(true);
            }
        } else {
            setTitle(getString(R.string.create_note));
            buttonLow.setSelected(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_edit_notes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveNote();
                return true;
            case R.id.action_share:
                shareNote();
                return true;
            case R.id.action_erase:
                erase();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if (!title.trim().isEmpty()) {
            if (!description.trim().isEmpty()) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                shareIntent.putExtra(Intent.EXTRA_TEXT, title + "\n\n" + description);
                shareIntent.setType("text/plain"); //Will Only Open email clients.
                startActivity(Intent.createChooser(shareIntent, "Share"));
            } else {
                Toast.makeText(this, "Note Description is required", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Note Title is required!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        Date date_created = Calendar.getInstance().getTime();
        if(buttonMedium.isSelected()){
            priority = Note.MEDIUM_PRIORITY;
        }
        if(buttonHigh.isSelected()){
            priority = Note.HIGH_PRIORITY;
        }
        if(buttonLow.isSelected()){
            priority = Note.LOW_PRIORITY;
        }

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "CANNOT SAVE EMPTY NOTE!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent data = new Intent();
            data.putExtra(EXTRA_TITLE, title);
            data.putExtra(EXTRA_DESCRIPTION, description);
            data.putExtra(EXTRA_DATE_CREATED, date_created);
            data.putExtra(EXTRA_PRIORITY, priority);
            int id = getIntent().getIntExtra(EXTRA_ID, -1);
            if (id != -1) {
                data.putExtra(EXTRA_ID, id);
            }
            setResult(RESULT_OK, data);
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void erase() {
        editTextTitle.setText(null);
        editTextDescription.setText(null);
        editTextTitle.requestFocus();
    }
}