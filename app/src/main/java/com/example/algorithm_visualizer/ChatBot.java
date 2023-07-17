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

import com.example.algorithm_visualizer.Adapters.BotMessageAdapter;
import com.example.algorithm_visualizer.Models.BotMessageModelClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatBot extends AppCompatActivity {

    private static final String BASE_URL = "https://api.openai.com/v1/chat/completions";
    private static final String YOUR_API_KEY = "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";

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    List<BotMessageModelClass> messageList;
    RecyclerView botRecyclerView;
    EditText askBotEditText;
    ImageButton sendButton;
    ImageButton microphone;
    BotMessageAdapter botMessageAdapter;
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
        botMessageAdapter = new BotMessageAdapter(messageList);
        botRecyclerView.setAdapter(botMessageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        botRecyclerView.setLayoutManager(linearLayoutManager);

        sendButton.setOnClickListener(v -> {
            String Question = askBotEditText.getText().toString().trim();
            addToChatList(Question , BotMessageModelClass.SENT_BY_ME);
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
            messageList.add(new BotMessageModelClass(Question, sentBy));
            botMessageAdapter.notifyItemInserted(messageList.size() - 1);
            botRecyclerView.smoothScrollToPosition(messageList.size()-1);
        });
    }

    void addResponse(String response){
        addToChatList(response, BotMessageModelClass.SENT_BY_BOT);
    }

        private void processAI(String Question) {
            JSONObject jsonBody = null;
            try {
                jsonBody = new JSONObject();
                JSONArray messagesArray = new JSONArray();
                JSONObject systemMessage = new JSONObject();
                systemMessage.put("role", "system");
                systemMessage.put("content", "You are a helpful assistant.");
                JSONObject userMessage = new JSONObject();
                userMessage.put("role", "user");
                userMessage.put("content", Question);
                messagesArray.put(systemMessage);
                messagesArray.put(userMessage);
                jsonBody.put("model", "gpt-3.5-turbo-0613");
                jsonBody.put("messages", messagesArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .header("Authorization","Bearer "+YOUR_API_KEY)
                    .post(body)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    if (e instanceof SocketTimeoutException) {
                        addResponse("Request timed out. Please try again later.");
                    } else {
                        addResponse("Failed to load response due to " + e.getMessage());
                    }
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    assert response.body() != null;
                    if (response.isSuccessful()){
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            JSONArray choicesArray = jsonObject.getJSONArray("choices");
                            JSONObject choiceObject = choicesArray.getJSONObject(0);
                            JSONObject messageObject = choiceObject.getJSONObject("message");
                            String assistantReply = messageObject.getString("content");
                            addResponse(assistantReply.trim());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        addResponse("Failed to load response due to " + response.body().string());
                    }
                }
            });
        }
}