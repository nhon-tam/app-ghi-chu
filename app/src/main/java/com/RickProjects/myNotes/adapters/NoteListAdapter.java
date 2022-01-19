package com.RickProjects.myNotes.adapters;

import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.RickProjects.myNotes.models.Note;
import com.RickProjects.myNotes.R;

import java.util.ArrayList;
import java.util.List;

public class NoteListAdapter extends ListAdapter<Note, NoteListAdapter.NoteListViewHolder> implements Filterable {

    private OnClickListener mListener;
    private List<Note> mNoteListInit;


    public void setInitList(List<Note> noteList) {
        mNoteListInit = new ArrayList<>(noteList);
    }

    @Override
    public Filter getFilter() {
        return filterList;
    }

    private Filter filterList = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Note> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mNoteListInit);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Note note : mNoteListInit) {
                    if (note.getTitle().toLowerCase().contains(filterPattern)||note.getDescription().toLowerCase().contains(filterPattern)) {
                        filteredList.add(note);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            submitList((List) results.values);
        }
    };

    public interface OnClickListener {
        void onClick(Note note);
    }

    public void setOnItemClick(OnClickListener listener) {
        mListener = listener;
    }

    public NoteListAdapter() {
        super(DIFF_CALL_BACK);
    }


    private static final DiffUtil.ItemCallback<Note> DIFF_CALL_BACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getDateCreated().equals(newItem.getDateCreated())
                    && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getTitle().equals(newItem.getTitle())
                    && oldItem.getPriority() == newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public NoteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteListViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_note_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListViewHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewDate.setText(currentNote.getDateCreated().toString());
        if(currentNote.getPriority()==Note.HIGH_PRIORITY){
            holder.imageView.setImageResource(R.drawable.redd);
        }
        if(currentNote.getPriority()==Note.MEDIUM_PRIORITY){
            holder.imageView.setImageResource(R.drawable.vang);
        }
        if(currentNote.getPriority()==Note.LOW_PRIORITY){
            holder.imageView.setImageResource(R.drawable.green);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(getNoteAt(holder.getAdapterPosition()));
            }
        });
    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }



    class NoteListViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle, textViewDescription, textViewDate;
        private CardView cardView;
        private ImageView imageView;

        public NoteListViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textView_noteTitle);
            textViewDescription = itemView.findViewById(R.id.textView_description);
            textViewDate = itemView.findViewById(R.id.textView_date);
            cardView = itemView.findViewById(R.id.cardView);
            imageView = itemView.findViewById(R.id.priority);
        }

    }
}
