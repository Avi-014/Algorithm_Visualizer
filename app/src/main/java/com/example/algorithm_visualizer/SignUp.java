package com.example.algorithm_visualizer;

import android.content.Intent;
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

public class SignUp extends AppCompatActivity {

    EditText enter_username;
    EditText enter_email;
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";
    private static final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$";

    EditText enter_password;
    EditText confirm_password;
    ImageView register_btn;
    ImageView back_btn;
    ImageView google_btn;
    private static final String url = "http://192.168.29.222/APIs/REGISTER.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        enter_username = findViewById(R.id.enter_username);
        enter_email = findViewById(R.id.enter_email);
        enter_password = findViewById(R.id.enter_password);
        confirm_password = findViewById(R.id.confirm_password);
//    TextInputLayout confirmPassInLayout; PassInLayout = findViewById(R.id.PassInLayout);
//        confirmPassInLayout = findViewById(R.id.confirmPassInLayout);
        register_btn = findViewById(R.id.register_btn);
        back_btn = findViewById(R.id.back_btn);
        google_btn = findViewById(R.id.google_btn);


        back_btn.setOnClickListener(v -> {
            startActivity(new Intent(SignUp.this,LoginActivity.class));
            finish();
        });
        register_btn.setOnClickListener(v -> {
            String username = enter_username.getText().toString();
            String email = enter_email.getText().toString();
            String password = enter_password.getText().toString();
            String confirmPassword = confirm_password.getText().toString();

            if (username.isEmpty()){
                enter_username.setError("Enter Username");
                Toast.makeText(SignUp.this, "Enter a valid Username", Toast.LENGTH_SHORT).show();
            }else if (!email.matches(EMAIL_PATTERN) || email.isEmpty()) {
                enter_email.setError("Enter a Valid Email");
                Toast.makeText(SignUp.this, "Enter a Valid Email", Toast.LENGTH_SHORT).show();
            } else if (password.length() <8){
                enter_password.setError("Password must be at least 8 characters long");
                Toast.makeText(SignUp.this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
            } else if (!password.matches(PASSWORD_PATTERN)){
                enter_password.setError("Password must contain at least\none uppercase letter,\none lowercase letter,\none special character,\nand one number");
                Toast.makeText(SignUp.this, "Enter a Valid Password", Toast.LENGTH_SHORT).show();
            } else if (!confirmPassword.equals(password)) {
                confirm_password.setError("Password doesn't matches");
                Toast.makeText(SignUp.this, "Password doesn't matches", Toast.LENGTH_SHORT).show();
            } else {
                register(username,email,password);
            }
        });
    }

    private void register(final String username, final String email, final String password) {
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            enter_email.setText("");
            enter_password.setText("");
            enter_username.setText("");
            confirm_password.setText("");
            Toast.makeText(SignUp.this, response, Toast.LENGTH_LONG).show();
            Log.i("Response",response);
        }, error -> {
            enter_email.setText("");
            enter_password.setText("");
            enter_username.setText("");
            confirm_password.setText("");
            Toast.makeText(SignUp.this, error.toString(), Toast.LENGTH_LONG).show();
            Log.i("Error",error.toString());
        }){
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("username",username);
                map.put("email",email);
                map.put("password",password);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

//        int TIMEOUT_MS=10000;        //10 seconds
//
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                TIMEOUT_MS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUp.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}