package com.example.layoutcustomer.helper;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseHelper {

    // metodos para salvar arquivos

    private static FirebaseAuth auth;
    public static FirebaseAuth getAuth(){
        if (auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

}
