package com.example.algorithm_visualizer;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.algorithm_visualizer.MainActivity;
import com.example.algorithm_visualizer.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Code_Snippet extends AppCompatActivity {

    TextView codeSnippetTextView;
    TextView textHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_code_snippet);

        FloatingActionButton fab = findViewById(R.id.copy_button);
        codeSnippetTextView = findViewById(R.id.code_snippet);
        textHeader = findViewById(R.id.textHeader);

        // Launch ActivityB
        Intent intent = getIntent();
        if (intent != null) {
            String headerText = intent.getStringExtra("headerText");
            String codeSnippet = intent.getStringExtra("codeSnippetResId");

            textHeader.setText(headerText);
            codeSnippetTextView.setText(codeSnippet);
        }

        fab.setOnClickListener(v -> {
            // Get the text from the text view
            String text = codeSnippetTextView.getText().toString();

            // Copy the text to the clipboard
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Code snippet", text);
            clipboardManager.setPrimaryClip(clipData);

            // Show a toast message to indicate that the text has been copied
            Toast.makeText(getApplicationContext(), "Code snippet copied to clipboard", Toast.LENGTH_SHORT).show();
        });
    }
}