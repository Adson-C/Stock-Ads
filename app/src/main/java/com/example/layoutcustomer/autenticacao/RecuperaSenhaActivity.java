package com.example.layoutcustomer.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.layoutcustomer.R;
import com.example.layoutcustomer.helper.FirebaseHelper;

public class RecuperaSenhaActivity extends AppCompatActivity {

    EditText edt_email;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recupera_senha);
        
        iniciarComponentes();
    }

    public void recuperaSenha(View view) {

        String email = edt_email.getText().toString().trim();

        if (!email.isEmpty()){
            progressBar.setVisibility(View.VISIBLE);
            enviarEmail(email);

        }else {
            edt_email.requestFocus();
            edt_email.setError("Infrome seu E-mail.");
        }

    }

    private void enviarEmail(String email) {
        FirebaseHelper.getAuth().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(this, "Email envido com Sucesso", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }else {
                    String error = task.getException().getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void iniciarComponentes() {
        edt_email = findViewById(R.id.edtemail);
        progressBar = findViewById(R.id.progressBar);
    }
}