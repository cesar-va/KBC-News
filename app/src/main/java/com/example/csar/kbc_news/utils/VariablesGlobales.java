package com.example.csar.kbc_news.utils;

import com.google.firebase.auth.FirebaseAuth;

public class VariablesGlobales {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private HttpUtils httpUtils = new HttpUtils();

    private static VariablesGlobales instance = null;

    protected VariablesGlobales() {}
    public static VariablesGlobales getInstance() {
        if(instance == null) {instance = new VariablesGlobales(); }
        return instance;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public HttpUtils getHttpUtils() {
        return httpUtils;
    }

    public void setHttpUtils(HttpUtils httpUtils) {
        this.httpUtils = httpUtils;
    }
}// fin de la clase de variables globales