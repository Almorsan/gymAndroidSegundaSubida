package com.example.gimnasio;

import org.json.JSONObject;


    public interface VolleyCallback {
        void onSuccess(JSONObject result);
        void onError(String error);
    }


