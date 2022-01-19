package com.RickProjects.myNotes.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.RickProjects.myNotes.ItemNavigation;
import com.RickProjects.myNotes.viewmodels.NoteViewModel;
import com.RickProjects.myNotes.R;
import com.RickProjects.myNotes.adapters.ItemAdapter;
import com.RickProjects.myNotes.adapters.NoteListAdapter;
import com.RickProjects.myNotes.models.Note;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static final int CREATE_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    public static final int LINEAR_LAYOUT = 0;
    public static final int GRID_LAYOUT = 1;
    private NoteViewModel mNoteViewModel;
    private RecyclerView recyclerView;
    private NoteListAdapter adapter;
    private int mLayout;
    private SharedPreferences pref;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;


    private ListView list_item;
    private ArrayList arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        drawerLayout = findViewById(R.id.activity_main_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView = findViewById(R.id.nav_view);
        list_item = findViewById(R.id.listView);
        initArrayForListViewDrawer();
        ItemAdapter adapterNav = new ItemAdapter(this, arrayList);
        list_item.setAdapter(adapterNav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                return true;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        pref = getPreferences(this.MODE_PRIVATE);
        int mLayout = pref.getInt("Layout", this.LINEAR_LAYOUT);
        if(mLayout == this.LINEAR_LAYOUT){
            adapter = new NoteListAdapter();
            mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
            recyclerView = findViewById(R.id.recycler_note_list);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            mNoteViewModel.getNotes().observe(this, new Observer<List<Note>>() {
                @Override
                public void onChanged(List<Note> notes) {
                    adapter.submitList(notes);
                    adapter.setInitList(notes);
                }
            });
        }
        else{
            adapter = new NoteListAdapter();
            mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
            recyclerView = findViewById(R.id.recycler_note_list);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            mNoteViewModel.getNotes().observe(this, new Observer<List<Note>>() {
                @Override
                public void onChanged(List<Note> notes) {
                    adapter.submitList(notes);
                    adapter.setInitList(notes);
                }
            });
        }


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(NoteListActivity.this)
                        .setMessage(R.string.delete_msg)
                        .setTitle(R.string.delete)
                        .setPositiveButton(R.string.am_sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mNoteViewModel.delete(adapter.getNoteAt(viewHolder.getLayoutPosition()));
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(NoteListActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClick(
                note -> {
                    Intent intent = new Intent(NoteListActivity.this, CreateEditNoteActivity.class);
                    intent.putExtra(CreateEditNoteActivity.EXTRA_ID, note.getId());
                    intent.putExtra(CreateEditNoteActivity.EXTRA_TITLE, note.getTitle());
                    intent.putExtra(CreateEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                    intent.putExtra(CreateEditNoteActivity.EXTRA_DATE_CREATED, note.getDateCreated());
                    intent.putExtra(CreateEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
                    startActivityForResult(intent, EDIT_NOTE_REQUEST);
                }
        );

        findViewById(R.id.fab_addNote).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NoteListActivity.this, CreateEditNoteActivity.class);
                        intent.putExtra(CreateEditNoteActivity.EXTRA_PRIORITY, Note.LOW_PRIORITY);
                        startActivityForResult(intent, CREATE_NOTE_REQUEST);
                    }
                });

        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1: break;
                    case 0:{
                        Intent i = new Intent(getApplicationContext(), TodoActivity.class);
                        startActivity(i);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_NOTE_REQUEST && resultCode == RESULT_OK) {
            mNoteViewModel.insert(new Note(
                    data.getStringExtra(CreateEditNoteActivity.EXTRA_TITLE),
                    data.getStringExtra(CreateEditNoteActivity.EXTRA_DESCRIPTION),
                    (Date)data.getSerializableExtra(CreateEditNoteActivity.EXTRA_DATE_CREATED),
                    data.getIntExtra(CreateEditNoteActivity.EXTRA_PRIORITY, Note.LOW_PRIORITY)
            ));
            adapter.setInitList(mNoteViewModel.getNotes().getValue());
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(CreateEditNoteActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Note Canceled", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(CreateEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(CreateEditNoteActivity.EXTRA_DESCRIPTION);
            Date date = (Date) data.getSerializableExtra(CreateEditNoteActivity.EXTRA_DATE_CREATED);
            int priority = data.getIntExtra(CreateEditNoteActivity.EXTRA_PRIORITY, Note.LOW_PRIORITY);
            Note note = new Note(title, description, date, priority);
            note.setId(id);
            mNoteViewModel.update(note);
            adapter.setInitList(mNoteViewModel.getNotes().getValue());
            messageFeedback("Note Updated");
        } else {
            Toast.makeText(this, "Not not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_list, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menu_Layout:
                if(mLayout == this.LINEAR_LAYOUT){
                    mLayout = this.GRID_LAYOUT;
                    item.setIcon(R.drawable.linear);
                    item.setTitle("Linear layout");
                    //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                    pref = getPreferences(this.MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putInt("Layout", this.GRID_LAYOUT);
                    edit.commit();
                    return true;
                }
                else{
                    mLayout = this.LINEAR_LAYOUT;
                    item.setIcon(R.drawable.grid);
                    item.setTitle("Grid layout");
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    pref = getPreferences(this.MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putInt("Layout", mLayout);
                    edit.commit();
                    return true;
                }
                case R.id.deleteAll: {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.delete_all)
                            .setMessage("Are you sure you want to Delete All of your Notes")
                            .setPositiveButton(R.string.am_sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mNoteViewModel.deleteAll();
                                    adapter.setInitList(mNoteViewModel.getNotes().getValue());
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();

                    return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initArrayForListViewDrawer() {
        arrayList = new ArrayList();

        ItemNavigation itemNavigation1 = new ItemNavigation(R.drawable.delete, "Todo list");
        arrayList.add(itemNavigation1);

        ItemNavigation itemNavigation2 = new ItemNavigation(R.drawable.delete, "Notes");
        arrayList.add(itemNavigation2);

    }



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void messageFeedback(String msg) {
        Snackbar.make(findViewById(R.id.layout_note_list), msg, Snackbar.LENGTH_SHORT).show();
    }

    private void messageFeedback(String msg, int len) {
        if (len == 1) messageFeedback(msg);
        else Snackbar.make(findViewById(R.id.layout_note_list), msg, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }

}
