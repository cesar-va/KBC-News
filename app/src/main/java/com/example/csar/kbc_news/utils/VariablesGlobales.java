package com.example.csar.kbc_news.utils;

import com.google.firebase.auth.FirebaseAuth;

public class VariablesGlobales {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
}// fin de la clase de variables globales