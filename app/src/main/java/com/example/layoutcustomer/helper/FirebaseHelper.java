package com.example.layoutcustomer.helper;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseHelper {

    // Essa Class server para qualquer App que use Firebase
    // metodos para salvar arquivos
    private static FirebaseAuth auth;
    public static FirebaseAuth getAuth(){
        if (auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static boolean getAutenticao(){
        return getAuth().getCurrentUser() != null;
    }
}
