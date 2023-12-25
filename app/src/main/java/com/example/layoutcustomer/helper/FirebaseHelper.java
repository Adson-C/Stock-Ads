package com.example.layoutcustomer.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    // Essa Class server para qualquer App que use Firebase
    // metodos para salvar arquivos
    private static FirebaseAuth auth;
    private static DatabaseReference databaseReference;

    private static DatabaseReference getDatabaseReference(){
        if (databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }


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
