package com.example.peterzhao.dysreader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peterzhao.dysreader.pdf_utils.PDFReader;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    File selectedFile;
    private static int PICK_PDF = 1212;
    boolean b1 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addSelectButtonListener();
        addToDyslexiaFontButtonListener();
        addDeleteFileButtonListener();
    }

    private void addSelectButtonListener() {
        Button selectButton = findViewById(R.id.select_button);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo: navigate to select picture from gallery, select pdf from internal storage
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, PICK_PDF);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && requestCode == PICK_PDF && resultCode == RESULT_OK) {

                Uri pdfUri = data.getData();
                String uriString = pdfUri.toString();
                Toast.makeText(getApplicationContext(), uriString, Toast.LENGTH_LONG).show();
                File file = new File(uriString);
                TextView textView = (TextView) findViewById(R.id.file_text_view);
                //Toast.makeText(getApplicationContext(), uriString, Toast.LENGTH_LONG).show();
                String displayName = null;
                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = this.getContentResolver().query(pdfUri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    displayName = file.getName();
                }
                textView.setText(displayName);
                String pathName = "/data/media/0/Download/" + displayName;
                selectedFile = new File(pathName);
                b1 = true;

                //String[] split = uriString.split("/");
                //Toast.makeText(getApplicationContext(), displayName, Toast.LENGTH_LONG).show();
                //textView.setText(split[split.length - 1]);

        }
    }

    private void addToDyslexiaFontButtonListener() {
        Button toDyslexiaFontButton = findViewById(R.id.to_dyslexia_font_button);
        toDyslexiaFontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent textViewPage = new Intent(MainActivity.this,
                        TextScrollingActivity.class);
                startActivity(textViewPage);
                boolean isRead = PDFReader.ReadPDF(getApplicationContext(), selectedFile);
                /*if (b1) {
                    Intent textViewPage = new Intent(MainActivity.this,
                            TextScrollingActivity.class);
                    startActivity(textViewPage);
                } else {
                    Intent textViewPage = new Intent(MainActivity.this,
                            EmptyActivity.class);
                    startActivity(textViewPage);
                }*/
            }
        });
    }

    private void addDeleteFileButtonListener() {
        ImageButton deleteFileButton = findViewById(R.id.DeleteFileButton);
        deleteFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFile = null;
                b1 = false;
                TextView textView = (TextView) findViewById(R.id.file_text_view);
                textView.setText("NoSelectedFile");
            }
        });
    }
}
