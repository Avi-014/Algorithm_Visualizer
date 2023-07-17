package com.example.algorithm_visualizer.Login_Signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.algorithm_visualizer.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUp extends AppCompatActivity {
    EditText enter_email;
    ProgressBar progressBar;
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";
//    private static final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$";

    EditText enter_password;
    EditText confirm_password;
    ImageView register_btn;
    ImageView back_btn;
    ImageView google_btn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        enter_email = findViewById(R.id.enter_email);
        enter_password = findViewById(R.id.enter_password);
        confirm_password = findViewById(R.id.confirm_password);
        register_btn = findViewById(R.id.register_btn);
        progressBar = findViewById(R.id.progressBar);
        back_btn = findViewById(R.id.back_btn);
        google_btn = findViewById(R.id.google_btn);

        mAuth = FirebaseAuth.getInstance();


        back_btn.setOnClickListener(v -> {
            startActivity(new Intent(SignUp.this, LoginActivity.class));
            finish();
        });
        register_btn.setOnClickListener(v -> {
            String email = enter_email.getText().toString();
            String password = enter_password.getText().toString();
            String confirmPassword = confirm_password.getText().toString();

            if (!email.matches(EMAIL_PATTERN) || email.isEmpty()) {
                enter_email.setError("Enter a Valid Email");
                Toast.makeText(SignUp.this, "Enter a Valid Email", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()){
                enter_password.setError("Enter a valid Password");
                Toast.makeText(SignUp.this, "Enter a valid Password", Toast.LENGTH_SHORT).show();
            } else if (!confirmPassword.equals(password)) {
                confirm_password.setError("Password doesn't matches");
                Toast.makeText(SignUp.this, "Password doesn't matches", Toast.LENGTH_SHORT).show();
            } else {
                register(email,password);
            }
        });
    }

    private void register(final String email, final String password) {
        loading(true);
        mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(SignUp.this, "Account Created", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUp.this, LoginActivity.class));
                finish();
            } else {
                loading(false);
                Toast.makeText(SignUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.i("Auth Error" , Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUp.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            register_btn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            register_btn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}