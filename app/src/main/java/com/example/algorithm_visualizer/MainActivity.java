package com.example.algorithm_visualizer;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.algorithm_visualizer.Adapters.AlgoAdapter;
import com.example.algorithm_visualizer.Login_Signup.LoginActivity;
import com.example.algorithm_visualizer.Models.AlgorithmsModelClass;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    Button logout_btn;

    LinearLayoutManager linearLayoutManager;
    List<AlgorithmsModelClass> AlgoList;
    AlgoAdapter algoAdapter;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.RecyclerView);
        logout_btn = findViewById(R.id.logout_btn);
        SearchView searchView = findViewById(R.id.SearchView);
        searchView.clearFocus();

        sharedPreferences = getSharedPreferences("LoginFile",MODE_PRIVATE);
        editor = sharedPreferences.edit();


        logout_btn.setOnClickListener(v -> {
            Dialog dialog = new Dialog(MainActivity.this, R.style.Dialog);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_layout);
            dialog.show();

            Button YesBtn, NoBtn;
            YesBtn = dialog.findViewById(R.id.YesBtn);
            NoBtn = dialog.findViewById(R.id.NoBtn);

            YesBtn.setOnClickListener(v1 -> {
                editor.putString("isLoggedIn","false");
                editor.commit();
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finishAffinity();
                dialog.dismiss();
            });
            NoBtn.setOnClickListener(v1 -> dialog.dismiss());
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                fileList(newText);
                return false;
            }

            private void fileList(String text) {
                List<AlgorithmsModelClass> filteredList = new ArrayList<>();
                for (AlgorithmsModelClass algorithmsModelClass : AlgoList){
                    if (algorithmsModelClass.getAlgoName().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add(algorithmsModelClass);
                    }
                }
                algoAdapter.setFilteredList(filteredList);
            }
        });

        initData();
        initRecyclerView();
        codes();


    }

    private void initData() {
        AlgoList = new ArrayList<>();
        AlgoList.add(new AlgorithmsModelClass("Converse With Assistant"));
        AlgoList.add(new AlgorithmsModelClass("Binary Search"));
        AlgoList.add(new AlgorithmsModelClass("Linear Search"));
        AlgoList.add(new AlgorithmsModelClass("Depth First Search"));
        AlgoList.add(new AlgorithmsModelClass("Breadth First Search"));
        AlgoList.add(new AlgorithmsModelClass("Fibonacci Series"));
        AlgoList.add(new AlgorithmsModelClass("Insertion Sort"));
        AlgoList.add(new AlgorithmsModelClass("Selection Sort"));
        AlgoList.add(new AlgorithmsModelClass("Bubble Sort"));
        AlgoList.add(new AlgorithmsModelClass("Merge Sort"));
        AlgoList.add(new AlgorithmsModelClass("2-Way Merge Sort"));
        AlgoList.add(new AlgorithmsModelClass("3-Way Merge Sort"));
        AlgoList.add(new AlgorithmsModelClass("Quick Sort"));
        AlgoList.add(new AlgorithmsModelClass("Dijkstra's Algorithm"));
        AlgoList.add(new AlgorithmsModelClass("Bellman Ford Algorithm"));
        AlgoList.add(new AlgorithmsModelClass("Kruskal's Algorithm"));
        AlgoList.add(new AlgorithmsModelClass("Prim's Algorithm"));
        AlgoList.add(new AlgorithmsModelClass("Fractional Knapsack"));
        AlgoList.add(new AlgorithmsModelClass("0/1 Knapsack"));
        AlgoList.add(new AlgorithmsModelClass("N-Queens Problem"));
        AlgoList.add(new AlgorithmsModelClass("All Pair Shortest Path"));
        AlgoList.add(new AlgorithmsModelClass("N-Queens Problem"));
        AlgoList.add(new AlgorithmsModelClass("Matrix Chain Multiplication"));
        AlgoList.add(new AlgorithmsModelClass("Recursive Maze Problem"));
        AlgoList.add(new AlgorithmsModelClass("Hamiltonian Circuit Problem"));
        AlgoList.add(new AlgorithmsModelClass("Subset Sum Problem"));

    }

    private void codes(){
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            final GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    if (position == 0) {
                        Intent intent = new Intent(MainActivity.this, ChatBot.class);
                        startActivity(intent);
                        return true;
                    } else if (position == 1) { // Check if it's the second item
                        String headerText = "Binary Search";
                        String codeSnippet = getString(R.string.binary_search_java);
                        Intent intent = new Intent(MainActivity.this, Code_Snippet.class);
                        intent.putExtra("headerText", headerText);
                        intent.putExtra("codeSnippetResId", codeSnippet);
                        startActivity(intent);
                        return true;
                    } else if (position == 2) { // Check if it's the second item
                        String headerText = "Linear Search";
                        String codeSnippet = getString(R.string.linear_search_java);
                        Intent intent = new Intent(MainActivity.this, Code_Snippet.class);
                        intent.putExtra("headerText", headerText);
                        intent.putExtra("codeSnippetResId", codeSnippet);
                        startActivity(intent);
                        return true;
                    } else if (position == 3) { // Check if it's the second item
                        String headerText = "Depth First Search";
                        String codeSnippet = getString(R.string.dfs_code);
                        Intent intent = new Intent(MainActivity.this, Code_Snippet.class);
                        intent.putExtra("headerText", headerText);
                        intent.putExtra("codeSnippetResId", codeSnippet);
                        startActivity(intent);
                        return true;
                    } else if (position == 4){
                        String headerText = "Breadth First Search";
                        String codeSnippet = getString(R.string.bfs_code);
                        Intent intent = new Intent(MainActivity.this, Code_Snippet.class);
                        intent.putExtra("headerText", headerText);
                        intent.putExtra("codeSnippetResId", codeSnippet);
                        startActivity(intent);
                        return true;
                    } else if (position == 5){
                        String headerText = "Fibonacci Series";
                        String codeSnippet = getString(R.string.fibonacci_series_code);
                        Intent intent = new Intent(MainActivity.this, Code_Snippet.class);
                        intent.putExtra("headerText", headerText);
                        intent.putExtra("codeSnippetResId", codeSnippet);
                        startActivity(intent);
                        return true;
                    } else if (position == 6){
                        String headerText = "Insertion Sort";
                        String codeSnippet = getString(R.string.insertion_sort_code);
                        Intent intent = new Intent(MainActivity.this, Code_Snippet.class);
                        intent.putExtra("headerText", headerText);
                        intent.putExtra("codeSnippetResId", codeSnippet);
                        startActivity(intent);
                        return true;
                    } else if (position == 7){
                        String headerText = "Selection Sort";
                        String codeSnippet = getString(R.string.selection_sort_code);
                        Intent intent = new Intent(MainActivity.this, Code_Snippet.class);
                        intent.putExtra("headerText", headerText);
                        intent.putExtra("codeSnippetResId", codeSnippet);
                        startActivity(intent);
                        return true;
                    } else if (position == 8){
                        String headerText = "Bubble Sort";
                        String codeSnippet = getString(R.string.bubble_sort_code);
                        Intent intent = new Intent(MainActivity.this, Code_Snippet.class);
                        intent.putExtra("headerText", headerText);
                        intent.putExtra("codeSnippetResId", codeSnippet);
                        startActivity(intent);
                        return true;
                    } else if (position == 9){
                        String headerText = "Merge Sort";
                        String codeSnippet = getString(R.string.merge_sort_code);
                        Intent intent = new Intent(MainActivity.this, Code_Snippet.class);
                        intent.putExtra("headerText", headerText);
                        intent.putExtra("codeSnippetResId", codeSnippet);
                        startActivity(intent);
                        return true;
                    } else {
                        String headerText = "N/A";
                        String codeSnippet = "We are continuously working on it\nand it will be updated soon";
                        Intent intent = new Intent(MainActivity.this, Code_Snippet.class);
                        intent.putExtra("headerText", headerText);
                        intent.putExtra("codeSnippetResId", codeSnippet);
                        startActivity(intent);
                    }
                }
                return false;
            }
            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        algoAdapter = new AlgoAdapter(AlgoList, getApplicationContext());
        recyclerView.setAdapter(algoAdapter);
        algoAdapter.notifyDataSetChanged();
    }
}