package com.example.algorithm_visualizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    ImageView login_btn;
    ImageView signup_btn;
    EditText email_or_username;
    EditText password;
    private static final String url = "http://192.168.29.222/APIs/LOGIN.php";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        login_btn = findViewById(R.id.login_btn);
        signup_btn = findViewById(R.id.signup_btn);
        email_or_username = findViewById(R.id.email_or_username);
        password = findViewById(R.id.password);

        sharedPreferences = getSharedPreferences("LoginFile",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getString("isLoggedIn","").equals("true")) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finishAffinity();
        }


        login_btn.setOnClickListener(v -> {
           String EmailOrUsername = email_or_username.getText().toString();
           String Password = password.getText().toString();

           if (EmailOrUsername.isEmpty()){
               email_or_username.setError("Enter Email or UserName");
           } else if (Password.isEmpty()) {
               password.setError("Enter a Valid Password");
           } else {
               login(EmailOrUsername,Password);
           }
        });


        signup_btn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this,SignUp.class);
            startActivity(intent);
            finish();
        });
    }

    private void login(String emailOrUsername, String password) {
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            if (response.equals("Logged In")){
                editor.putString("isLoggedIn","true");
                editor.commit();
                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finishAffinity();
            } else {
                Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            if (error.toString().equals("com.android.volley.NoConnectionError: java.net.ConnectException: Failed to connect to /192.168.29.222:80")) {
                Toast.makeText(LoginActivity.this, "No Internet", Toast.LENGTH_LONG).show();
                Log.i("Error", error.toString());
            } else {
                Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        }){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("username",emailOrUsername);
                map.put("email",emailOrUsername);
                map.put("password",password);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}