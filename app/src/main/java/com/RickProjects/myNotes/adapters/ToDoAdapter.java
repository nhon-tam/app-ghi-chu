package com.RickProjects.myNotes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.RickProjects.myNotes.R;
import com.RickProjects.myNotes.models.Note;
import com.RickProjects.myNotes.models.Todo;

import java.util.List;

public class ToDoAdapter extends ListAdapter<Todo, ToDoAdapter.ToDoViewHolder>{

    private OnCheckedChangeListener mListener;

    public ToDoAdapter() {
        super(DIFF_CALL_BACK);
    }

    public interface OnCheckedChangeListener {
        void onCheckedChangeListener(Todo todo, boolean isChecked);
    }

    private static final DiffUtil.ItemCallback<Todo> DIFF_CALL_BACK = new DiffUtil.ItemCallback<Todo>() {
        @Override
        public boolean areItemsTheSame(@NonNull Todo oldItem, @NonNull Todo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Todo oldItem, @NonNull Todo newItem) {
            return oldItem.getStatus() == newItem.getStatus()
                    && oldItem.getTask().equals(newItem.getTask());
        }
    };


    public Todo getTodoAt(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ToDoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        Todo item = getItem(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListener.onCheckedChangeListener(getTodoAt(holder.getAdapterPosition()), isChecked);
            }
        });
    }

    public void setOnCheckedItem(OnCheckedChangeListener listener) {
        mListener = listener;
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        ToDoViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }

}
