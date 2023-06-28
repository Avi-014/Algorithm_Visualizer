package com.example.algorithm_visualizer;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BinarySearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_binary_search);

        FloatingActionButton fab = findViewById(R.id.copy_button);
        TextView codeSnippetTextView = findViewById(R.id.code_snippet);
        String codeSnippet = "int binarySearch(int[] array, int target) {\n" +
                "    int left = 0;\n" +
                "    int right = array.length - 1;\n" +
                "\n" +
                "    while (left <= right) {\n" +
                "        int mid = (left + right) / 2;\n" +
                "\n" +
                "        if (array[mid] == target) {\n" +
                "            return mid;\n" +
                "        } else if (array[mid] < target) {\n" +
                "            left = mid + 1;\n" +
                "        } else {\n" +
                "            right = mid - 1;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    return -1; // target not found\n" +
                "}" ;
        codeSnippetTextView.setText(codeSnippet);

        // Set an OnClickListener to the floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the text view
                String text = codeSnippetTextView.getText().toString();

                // Copy the text to the clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Code snippet", text);
                clipboardManager.setPrimaryClip(clipData);

                // Show a toast message to indicate that the text has been copied
                Toast.makeText(getApplicationContext(), "Code snippet copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }
}