package com.RickProjects.myNotes.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.RickProjects.myNotes.AddNewTask;
import com.RickProjects.myNotes.RecyclerItemTouchHelper;
import com.RickProjects.myNotes.adapters.ToDoAdapter;
import com.RickProjects.myNotes.interfaces.DialogCloseListener;
import com.RickProjects.myNotes.models.Todo;
import com.RickProjects.myNotes.viewmodels.NoteViewModel;
import com.RickProjects.myNotes.viewmodels.ToDoViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


import com.RickProjects.myNotes.R;

import java.util.List;
import java.util.Objects;

public class TodoActivity extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;
    private ToDoViewModel mTodoViewModel;
    private List<Todo> taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter();
        tasksRecyclerView.setAdapter(tasksAdapter);

        mTodoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        mTodoViewModel.getTodos().observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todos) {
                tasksAdapter.submitList(todos);
            }
        });

        tasksAdapter.setOnCheckedItem(
                (todo, isChecked) -> {
                    if (isChecked) {
                        todo.setStatus(1);
                        mTodoViewModel.update(todo);
                    } else {
                        todo.setStatus(0);
                        mTodoViewModel.update(todo);
                    }
                }
        );

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask ant = new AddNewTask(mTodoViewModel);
                ant.show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter, mTodoViewModel, TodoActivity.this));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

    }


    @Override
    public void handleDialogClose(DialogInterface dialog) {

    }
}