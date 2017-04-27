package com.tsi.android.mycoursework;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


public class DescriptionActivity extends AppCompatActivity {

    public static final String TODO_ITEM = "todo";
    private PopupWindow popupWindow;
    private LinearLayout linearLayout;
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        final TodoDbHelper db = new TodoDbHelper(this);

        final TodoItem todoItem = getIntent().getExtras().getParcelable(TODO_ITEM);

        final TextView title = (TextView) findViewById(R.id.title_text);
        title.setText(todoItem.getTitle());

        final TextView description = (TextView) findViewById(R.id.descriptionText);
        description.setText(todoItem.getDescription());

        Button edit = (Button) findViewById(R.id.editButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout = (LinearLayout) findViewById(R.id.activity_description);
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                linearLayout.setAlpha(0.6f);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.activity_popup, null);
                popupWindow = new PopupWindow(
                        container,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        true);
                popupWindow.showAtLocation(linearLayout, Gravity.NO_GRAVITY, 150, 400);

                final EditText editText = (EditText) container.findViewById(R.id.inputDescriptionText);

                editText.setText(todoItem.getDescription());

                final Button addDescriptionButton = (Button) container.findViewById(R.id.addDescriptionText);
                addDescriptionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText.getText().length() != 0) {
                            todoItem.setDescription(editText.getText().toString());
                            description.setText(todoItem.getDescription());
                            linearLayout.setAlpha(1);
                            db.updateTodoItem(todoItem);
                            popupWindow.dismiss();
                        }

                    }

                });
                Button btnDismiss = (Button) container.findViewById(R.id.cancelButton);
                btnDismiss.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        linearLayout.setAlpha(1);
                        popupWindow.dismiss();
                    }
                });
            }
        });

        Button share = (Button) findViewById(R.id.shareButton);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharingToSocialMedia(todoItem.getDescription());
            }
        });

        findViewById(R.id.cameraButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 16);
            }
        });
    }

    public void SharingToSocialMedia(String description) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, description);
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 16 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ImageView imageView = (ImageView) findViewById(R.id.cameraImage);
            imageView.setImageBitmap(photo);
        }
    }
}
