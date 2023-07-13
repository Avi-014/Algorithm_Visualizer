package com.example.algorithm_visualizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button logout_btn;

    LinearLayoutManager linearLayoutManager;
    List<ModelClass> AlgoList;
    Adapter adapter;

    private SearchView searchView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.RecyclerView);
        logout_btn = findViewById(R.id.logout_btn);
        searchView = findViewById(R.id.SearchView);
        searchView.clearFocus();

        sharedPreferences = getSharedPreferences("LoginFile",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        logout_btn.setOnClickListener(v -> {
            editor.putString("isLoggedIn","false");
            editor.commit();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finishAffinity();
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
                List<ModelClass> filteredList = new ArrayList<>();
                for (ModelClass modelClass : AlgoList){
                    if (modelClass.getAlgoName().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add(modelClass);
                    }
                }
                adapter.setFilteredList(filteredList);
            }
        });

        initData();
        initRecyclerView();


    }

    private void initData() {
        AlgoList = new ArrayList<>();
        AlgoList.add(new ModelClass("Converse With Assistant"));
        AlgoList.add(new ModelClass("Binary Search"));
        AlgoList.add(new ModelClass("Linear Search"));
        AlgoList.add(new ModelClass("Depth First Search"));
        AlgoList.add(new ModelClass("Breadth First Search"));
        AlgoList.add(new ModelClass("Fibonacci Series"));
        AlgoList.add(new ModelClass("Insertion Sort"));
        AlgoList.add(new ModelClass("Selection Sort"));
        AlgoList.add(new ModelClass("Bubble Sort"));
        AlgoList.add(new ModelClass("Merge Sort"));
        AlgoList.add(new ModelClass("2-Way Merge Sort"));
        AlgoList.add(new ModelClass("3-Way Merge Sort"));
        AlgoList.add(new ModelClass("Quick Sort"));
        AlgoList.add(new ModelClass("Dijkstra's Algorithm"));
        AlgoList.add(new ModelClass("Bellman Ford Algorithm"));
        AlgoList.add(new ModelClass("Kruskal's Algorithm"));
        AlgoList.add(new ModelClass("Prim's Algorithm"));
        AlgoList.add(new ModelClass("Fractional Knapsack"));
        AlgoList.add(new ModelClass("0/1 Knapsack"));
        AlgoList.add(new ModelClass("N-Queens Problem"));
        AlgoList.add(new ModelClass("All Pair Shortest Path"));
        AlgoList.add(new ModelClass("N-Queens Problem"));
        AlgoList.add(new ModelClass("Matrix Chain Multiplication"));
        AlgoList.add(new ModelClass("Recursive Maze Problem"));
        AlgoList.add(new ModelClass("Hamiltonian Circuit Problem"));
        AlgoList.add(new ModelClass("Subset Sum Problem"));

    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Adapter(AlgoList, getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}