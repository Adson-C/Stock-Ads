package com.example.layoutcustomer.autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.layoutcustomer.R;
import com.example.layoutcustomer.activity.MainActivity;
import com.example.layoutcustomer.helper.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {
    private TextView textCriar;
    private EditText edt_email, edt_senha;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniciarComponent();
        configClique();
    }

    public void logar(View view){
        String email = edt_email.getText().toString().trim();
        String senha = edt_senha.getText().toString();

        if (!email.isEmpty()){
            if (!senha.isEmpty()){

                progressBar.setVisibility(View.VISIBLE);
                
                validaLogin(email, senha);
                
            }else {
                edt_senha.requestFocus();
                edt_senha.setError("Informe sua senha.");
            }

        }else {
            edt_email.requestFocus();
            edt_email.setError("Informe seu E-mail.");
        }
    }

    private void validaLogin(String email, String senha) {

        FirebaseHelper.getAuth().signInWithEmailAndPassword(
            email, senha
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                finish();
                startActivity(new Intent(this, MainActivity.class));

            }else {
                String error = task.getException().getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Ocorreu erro no Task", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void configClique(){

        textCriar.setOnClickListener(view -> startActivity(new Intent(this, CriarContaActivity.class)));
    }

    private void iniciarComponent(){
        edt_email = findViewById(R.id.edtemail);
        edt_senha = findViewById(R.id.edtsenha);

        textCriar = findViewById(R.id.txtcriar_conta);

        progressBar = findViewById(R.id.progressBar);
    }
}