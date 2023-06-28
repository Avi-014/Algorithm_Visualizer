package com.example.algorithm_visualizer;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
public class ChatBot extends AppCompatActivity {

    List<MessageModelClass> messagelist;
    RecyclerView botRecyclerView;
    EditText askbotEditText;
    ImageButton sendbtn;
    ImageView microphone;
    MessageAdapter messageAdapter;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private final String BASE_URL = "https://api.openai.com/v1/chat/completions";
    String accessToken = "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";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chat_bot);

        botRecyclerView = findViewById(R.id.botRecyclerView);
        botRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        askbotEditText = findViewById(R.id.askbotEditText);
        sendbtn = findViewById(R.id.sendbtn);
        microphone = findViewById(R.id.microphone);


        messagelist = new ArrayList<>();
        messageAdapter = new MessageAdapter(messagelist);
        botRecyclerView.setAdapter(messageAdapter);

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAI();
            }
        });

        microphone.setOnClickListener(v -> {
            Intent intent
                    = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                    Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
            }
            catch (Exception e) {
                Toast.makeText(ChatBot.this, " " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                                         RecognizerIntent.EXTRA_RESULTS);
                askbotEditText.setText(Objects.requireNonNull(result).get(0));
            }
        }
    }
    private void processAI() {
        String text = askbotEditText.getText().toString();
        messagelist.add(new MessageModelClass(text,true));
        messageAdapter.notifyItemInserted(messagelist.size()-1);
        botRecyclerView.scrollToPosition(messagelist.size()-1);
        askbotEditText.getText().clear();


        JSONObject requestBody = new JSONObject();
        try {
            JSONArray messagesArray = new JSONArray();

            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", text);
            messagesArray.put(systemMessage);

            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", messagesArray);

        } catch (Exception e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL, requestBody,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray choices = response.getJSONArray("choices");
                    JSONObject choice = choices.getJSONObject(0);
                    JSONObject message = choice.getJSONObject("message");
                    String text = message.getString("text");
                    messagelist.add(new MessageModelClass(text.replaceFirst("\n","").
                            replaceFirst("\n", ""), false));
                    messageAdapter.notifyItemInserted(messagelist.size()-1);
                    botRecyclerView.scrollToPosition(messagelist.size()-1);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 429) {
                    // Handle "Too Many Requests" error
                    // You can implement logic to retry the request after a certain time or show a user-friendly message
                    messagelist.add(new MessageModelClass("Too many requests. Please try again later.", false));
                } else {
                    // Handle other errors
                    messagelist.add(new MessageModelClass("Error: " + error.getMessage(), false));
                }
                messageAdapter.notifyItemInserted(messagelist.size() - 1);
                botRecyclerView.scrollToPosition(messagelist.size() - 1);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put( "Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
            @Override
            protected Response< JSONObject > parseNetworkResponse(NetworkResponse response){
                return super.parseNetworkResponse(response);
            }
        };

        int timeoutMs = 1000;
        int maxNumRetries = 3;
        float backoffMultiplier = 2.0f;
        RetryPolicy policy = new DefaultRetryPolicy(timeoutMs , maxNumRetries, backoffMultiplier);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(ChatBot.this).addToRequestQueue(request);
    }
}