package com.example.algorithm_visualizer;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatBot extends AppCompatActivity {

    private static final String BASE_URL = "https://api.openai.com/v1/completions";
    private static final String YOUR_API_KEY = "Paste_Your_Api_Key_Here";

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    List<MessageModelClass> messageList;
    RecyclerView botRecyclerView;
    EditText askBotEditText;
    ImageButton sendButton;
    ImageButton microphone;
    MessageAdapter messageAdapter;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        botRecyclerView = findViewById(R.id.botRecyclerView);
        botRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        askBotEditText = findViewById(R.id.askbotEditText);
        sendButton = findViewById(R.id.sendbtn);
        microphone = findViewById(R.id.microphone);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        botRecyclerView.setAdapter(messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        botRecyclerView.setLayoutManager(linearLayoutManager);

        sendButton.setOnClickListener(v -> {
            String Question = askBotEditText.getText().toString().trim();
            addToChatList(Question , MessageModelClass.SENT_BY_ME);
            askBotEditText.setText("");
            processAI(Question);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT){
            if (resultCode == RESULT_OK && data!=null){
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                askBotEditText.setText(result.get(0));
            }
        }
    }

    private void addToChatList(String Question , String sentBy) {
        runOnUiThread(() -> {
            messageList.add(new MessageModelClass(Question, sentBy));
            messageAdapter.notifyDataSetChanged();
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            botRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }

    void addResponse(String response){
        addToChatList(response,MessageModelClass.SENT_BY_BOT);
    }

        private void processAI(String Question) {
            JSONObject jsonBody = null;
            try {
                jsonBody = new JSONObject();
                jsonBody.put("model", "text-davinci-003");
                jsonBody.put("prompt", Question);
                jsonBody.put("max_tokens", 4000);
                jsonBody.put("temperature", 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .header("Authorization","Bearer "+YOUR_API_KEY)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    addResponse("Failed to load response due to "+e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    assert response.body() != null;
                    if (response.isSuccessful()){
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            JSONArray jsonArray = jsonObject.getJSONArray("choices");
                            String result = jsonArray.getJSONObject(0).getString("text");
                            addResponse(result.trim());
                        } catch (JSONException e) {
                           e.printStackTrace();
                        }
                    } else {
                        addResponse("Failed to load response due to "+response.body().string());
                    }
                }
            });

//        int timeoutMs = 1000;
//        int maxNumRetries = 3;
//        float backoffMultiplier = 2.0f;
//        RetryPolicy policy = new DefaultRetryPolicy(timeoutMs , maxNumRetries, backoffMultiplier);
//        request.setRetryPolicy(policy);
//        MySingleton.getInstance(ChatBot.this).addToRequestQueue();
        }
}