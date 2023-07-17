package com.example.algorithm_visualizer.Login_Signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.algorithm_visualizer.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPassword extends AppCompatActivity {

    ImageButton btnSendLink;
    EditText email;
    ProgressBar progressBar;
    TextView textViewLinkSend;
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.email);
        btnSendLink = findViewById(R.id.btnSendLink);
        progressBar = findViewById(R.id.progressBar);
        textViewLinkSend = findViewById(R.id.textViewLinkSend);
        mAuth = FirebaseAuth.getInstance();

        btnSendLink.setOnClickListener(v -> {
            String Email = email.getText().toString();
            if (!Email.matches(EMAIL_PATTERN) || Email.isEmpty()) {
                email.setError("Enter a Valid Email");
                Toast.makeText(ForgotPassword.this, "Enter a Valid Email", Toast.LENGTH_SHORT).show();
            } else {
                resetPass(Email);
            }
        });

    }

    private void resetPass(String Email) {
        progressBar.setVisibility(View.VISIBLE);
        btnSendLink.setVisibility(View.INVISIBLE);

        mAuth.sendPasswordResetEmail(Email).addOnSuccessListener(unused -> {
            btnSendLink.setVisibility(View.VISIBLE);
            textViewLinkSend.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            btnSendLink.setEnabled(false);
        }).addOnFailureListener(e -> {
            if (Objects.equals(e.getMessage(), "There is no user record corresponding to this identifier. The user may have been deleted.")){
                Toast.makeText(this, "Email not registered\nCreate an account first", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.i("Auth Error" , e.getMessage());
            }
            Log.i("Auth Error" , e.getMessage());
            textViewLinkSend.setVisibility(View.INVISIBLE);
            btnSendLink.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
            btnSendLink.setVisibility(View.VISIBLE);
        });
    }
}