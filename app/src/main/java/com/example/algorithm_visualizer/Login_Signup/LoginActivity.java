package com.example.algorithm_visualizer.Login_Signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.algorithm_visualizer.MainActivity;
import com.example.algorithm_visualizer.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";
//    private static final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$";
    ImageView login_btn;
    ImageView signup_btn;
    EditText email;
    EditText password;
    TextView ForgotPassword;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_btn = findViewById(R.id.login_btn);
        signup_btn = findViewById(R.id.signup_btn);
        email = findViewById(R.id.email_or_username);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        ForgotPassword = findViewById(R.id.ForgotPassword);
        mAuth = FirebaseAuth.getInstance();


        sharedPreferences = getSharedPreferences("LoginFile",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getString("isLoggedIn","").equals("true")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finishAffinity();
        }


        login_btn.setOnClickListener(v -> {
           String Email = email.getText().toString();
           String Password = password.getText().toString();
            if (!Email.matches(EMAIL_PATTERN) || Email.isEmpty()) {
                email.setError("Enter a Valid Email");
                Toast.makeText(LoginActivity.this, "Enter a Valid Email", Toast.LENGTH_SHORT).show();
            } else if (Password.isEmpty()){
                password.setError("Enter a valid Password");
                Toast.makeText(LoginActivity.this, "Enter a valid Password", Toast.LENGTH_SHORT).show();
           } else {
               login(Email,Password);
           }
        });


        ForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
            startActivity(intent);
        });


        signup_btn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUp.class);
            startActivity(intent);
            finish();
        });
    }

    private void login(String email, String password) {
        loading(true);
        mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                editor.putString("isLoggedIn", "true");
                editor.commit();
                Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                UpdateUI();
            }
        }).addOnFailureListener(e -> {
            loading(false);
            if (Objects.equals(e.getMessage(), "The password is invalid or the user does not have a password.")) {
                Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
            } else if (Objects.equals(e.getMessage(), "There is no user record corresponding to this identifier. The user may have been deleted.")){
                Toast.makeText(this, "Email not registered\nCreate an account first", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.i("Auth Error" , e.getMessage());
            }
        });
    }

    private void UpdateUI(){
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finishAffinity();
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            login_btn.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            login_btn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}