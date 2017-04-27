package com.tsi.android.mycoursework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private PopupWindow popupWindow;
    private LinearLayout linearLayout;
    private LayoutInflater layoutInflater;
    final TodoDbHelper db = new TodoDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<TodoItem> list = db.getAllTodoItems();
        final ListViewAdapter adapter = new ListViewAdapter(list, this);

        final EditText editText = (EditText) findViewById(R.id.inputText);
        Button button = (Button) findViewById(R.id.add_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() != 0) {
                    TodoItem newItem = new TodoItem(editText.getText().toString(), new Date(), false);
                    adapter.addItem(newItem);
                    db.addTodoItem(newItem);
                    editText.setText("");
                }
            }
        });

        final ListView lView = (ListView) findViewById(R.id.todoList);
        lView.setAdapter(adapter);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                linearLayout = (LinearLayout) findViewById(R.id.activity_main);
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                linearLayout.setAlpha(0.6f);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.activity_popup, null);
                popupWindow = new PopupWindow(
                        container,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        true);

                popupWindow.showAtLocation(linearLayout, Gravity.NO_GRAVITY, 150, 400);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        linearLayout.setAlpha(1);
                    }
                });
                Button btnDismiss = (Button) container.findViewById(R.id.cancelButton);
                btnDismiss.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TodoItem item = adapter.getItem(position);
                        popupWindow.dismiss();
                        final Intent intent = new Intent(MainActivity.this, DescriptionActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(DescriptionActivity.TODO_ITEM, item);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });


                final Button addDescriptionText = (Button) container.findViewById(R.id.addDescriptionText);
                final EditText inputDescriptionText = (EditText) container.findViewById(R.id.inputDescriptionText);

                addDescriptionText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (inputDescriptionText.getText().length() != 0) {
                            TodoItem item = adapter.getItem(position);
                            popupWindow.dismiss();
                            item.setDescription(inputDescriptionText.getText().toString());
                            final Intent intent = new Intent(MainActivity.this, DescriptionActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(DescriptionActivity.TODO_ITEM, item);
                            intent.putExtras(bundle);
                            dbTodoItemUpdate(list.get(position));
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    public void dbTodoItemUpdate(TodoItem todoItem) {
        db.updateTodoItem(todoItem);
    }
}
