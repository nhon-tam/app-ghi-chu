package com.RickProjects.myNotes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.RickProjects.myNotes.ItemNavigation;
import com.RickProjects.myNotes.R;
import com.RickProjects.myNotes.activities.NoteListActivity;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<ItemNavigation> arr;

    public ItemAdapter(Context mContext, ArrayList arr) {
        super();
        this.mContext = mContext;
        this.arr = arr;
        mInflater = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        int count = 0;
        if (arr != null) {
            count = arr.size();
        }
        return count;
    }

    @Override
    public ItemNavigation getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder;

        if (convertView == null) {
            holder = new viewHolder();
            convertView = mInflater
                    .inflate(R.layout.nav_item, parent, false);
            holder.itemIcon = (ImageView) convertView
                    .findViewById(R.id.itemIcon);
            holder.itemName = (TextView) convertView
                    .findViewById(R.id.itemName);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }

        final ItemNavigation itemNavigation = arr.get(position);

        if (itemNavigation != null) {
            holder.itemIcon.setImageResource(itemNavigation.getItemIcon());
            holder.itemName.setText(itemNavigation.getItemName());
        }

        return convertView;
    }

    public class viewHolder {
        public TextView itemName;
        public ImageView itemIcon;
    }

}
