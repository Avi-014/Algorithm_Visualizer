package com.example.algorithm_visualizer;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton instance;
    public RequestQueue requestQueue;
    private static Context context;

    public MySingleton(Context context) {
        MySingleton.context = context;
        requestQueue = getRequestQueue();
    }
    public static synchronized MySingleton getInstance(Context context){
        if (instance == null){
            instance = new MySingleton(context);
        }
        return instance;
    }
    private RequestQueue getRequestQueue() {
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
