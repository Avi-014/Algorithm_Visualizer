package com.example.algorithm_visualizer;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatBot extends AppCompatActivity {

    List<MessageModelClass> messagelist;
    RecyclerView botRecyclerView;
    EditText askbotEditText;
    ImageButton sendbtn;
    ImageView microphone;
    MessageAdapter messageAdapter;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private final String apiurl = "https://api.openai.com/v1/chat/completions/";
    String accessToken = "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";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                askbotEditText.setText(Objects.requireNonNull(result).get(0));
            }
        }
    }
    private void processAI() {
        String Text = askbotEditText.getText().toString();
        messagelist.add(new MessageModelClass(Text,true));
        messageAdapter.notifyItemInserted(messagelist.size()-1);
        botRecyclerView.scrollToPosition(messagelist.size()-1);
        askbotEditText.getText().clear();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(25, TimeUnit.SECONDS)
                .readTimeout(25, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiurl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        JsonObject requestBody = new JsonObject();

        requestBody.addProperty("model", "gpt-4");
        requestBody.addProperty("prompt", Text);
        requestBody.addProperty("max_tokens", 100);
        requestBody.addProperty("temperature", 1);
        requestBody.addProperty("top_p", 1);
        requestBody.addProperty("frequency_penalty", 0.0);
        requestBody.addProperty("presence_penalty", 0.0);

        Call<JsonObject> call = apiService.sendRequest("https://api.openai.com/v1/chat/completions/","Bearer " + accessToken, "application/json",requestBody);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonResponse = response.body();
                    try {
                        assert jsonResponse != null;
                        JsonArray choicesArray = jsonResponse.getAsJsonArray("choices");
                        if (choicesArray != null && choicesArray.size() > 0) {
                            JsonObject choiceObject = choicesArray.get(0).getAsJsonObject();
                            String text = choiceObject.get("text").getAsString();
                            messagelist.add(new MessageModelClass(text.replaceFirst("\n", "").replaceFirst("\n", ""), false));
                            messageAdapter.notifyItemInserted(messagelist.size() - 1);
                            botRecyclerView.scrollToPosition(messagelist.size() - 1);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Log.e("API Error", response.toString());
                    messagelist.add(new MessageModelClass(response.toString().replaceFirst("\n","").replaceFirst("\n",""),false));
                    messageAdapter.notifyItemInserted(messagelist.size() - 1);
                    botRecyclerView.scrollToPosition(messagelist.size() - 1);
                }
            }

            final int maxRetries = 3;
            int retryCount = 0;
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                if (retryCount < maxRetries) {
                    retryCount++;
                    call.clone().enqueue(this);
                } else {
                    Log.e("API Error", t.toString());
                    messagelist.add(new MessageModelClass(t.toString().replaceFirst("\n", "").replaceFirst("\n", ""), false));
                    messageAdapter.notifyItemInserted(messagelist.size() - 1);
                    botRecyclerView.scrollToPosition(messagelist.size() - 1);
                }
            }
        });
    }
}