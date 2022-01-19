package com.RickProjects.myNotes.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.RickProjects.myNotes.service.DateConverter;

import java.util.Date;


@Entity(tableName = "Note_table")
public class Note {

    @Ignore
    public final static int LOW_PRIORITY = 0;
    @Ignore
    public final static int MEDIUM_PRIORITY = 1;
    @Ignore
    public final static int HIGH_PRIORITY = 2;

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "note_title")
    private String title;

    @NonNull
    @ColumnInfo(name = "note_description")
    private String description;

    @ColumnInfo(name = "note_date")
    @TypeConverters({DateConverter.class})
    private Date dateCreated;

    @ColumnInfo(name = "note_priority", defaultValue = ""+Note.LOW_PRIORITY)
    private int priority;


   public Note(@NonNull String title, @NonNull String description, Date dateCreated, int priority){
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

   public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
