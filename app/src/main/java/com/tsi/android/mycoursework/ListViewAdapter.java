package com.tsi.android.mycoursework;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

class ListViewAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<TodoItem> list = new ArrayList<>();
    private Context context;

    ListViewAdapter(ArrayList<TodoItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_todolist, null);
        }

        final TextView listItemText = (TextView) view.findViewById(R.id.list_element);
        listItemText.setText(list.get(position).getTitle());


        ImageButton deleteBtn = (ImageButton) view.findViewById(R.id.delete_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Message")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
        String formattedDate = df.format(list.get(position).getDate());

        final TextView date = (TextView) view.findViewById(R.id.date);
        date.setText(formattedDate);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.element_check);
        checkBox.setChecked(list.get(position).isCheckbox());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    list.get(position).setCheckbox(true);
                    listItemText.setPaintFlags(date.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    date.setPaintFlags(date.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    list.get(position).setCheckbox(false);
                    listItemText.setPaintFlags(0);
                    date.setPaintFlags(0);
                }
                ((MainActivity) context).dbTodoItemUpdate(list.get(position));
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public TodoItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(TodoItem item) {
        list.add(item);
        notifyDataSetChanged();
    }
}