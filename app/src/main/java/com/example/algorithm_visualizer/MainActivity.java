package com.example.algorithm_visualizer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<ModelClass> Algolist;
    Adapter adapter;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.RecyclerView);
        searchView = findViewById(R.id.SearchView);
        searchView.clearFocus();
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
                for (ModelClass modelClass : Algolist){
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
        Algolist = new ArrayList<>();
        Algolist.add(new ModelClass("Converse With Assistant"));
        Algolist.add(new ModelClass("Binary Search"));
        Algolist.add(new ModelClass("Insertion Sort"));


    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Adapter(Algolist, getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}