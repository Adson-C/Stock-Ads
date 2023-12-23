package com.example.layoutcustomer.autenticacao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.layoutcustomer.R;
import com.example.layoutcustomer.activity.MainActivity;
import com.example.layoutcustomer.helper.FirebaseHelper;
import com.example.layoutcustomer.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.Objects;

public class CriarContaActivity extends AppCompatActivity {

    private EditText edt_nome, edt_email, edt_senha;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);

        iniciaComponentes();

        configClique();

    }

    private void configClique(){
        findViewById(R.id.imgVoltar).setOnClickListener(view -> finish());
    }

    public void validaDados(View view) {

        String nome = edt_nome.getText().toString();
        String email = edt_email.getText().toString();
        String senha = edt_senha.getText().toString();

        if (!nome.isEmpty()) {
            if (!email.isEmpty()) {
                if (!senha.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);

                  // FirebaseAuth
                    Usuario usuario = new Usuario();
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setSenha(senha);

                    salvarCadastro(usuario);

                } else {
                    edt_senha.requestFocus();
                    edt_senha.setError("Informe sua senha.");
                }

            } else {
                edt_email.requestFocus();
                edt_email.setError("Informe seu E-mail.");
            }

        } else {
            edt_nome.requestFocus();
            edt_nome.setError("Informe seu nome.");
        }

    }

    private void salvarCadastro(Usuario usuario){

        FirebaseHelper.getAuth().createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                Toast.makeText(this, "task.isSuccessful", Toast.LENGTH_SHORT).show();
                
                String id = Objects.requireNonNull(task.getResult().getUser().getUid());
                usuario.setId(id);

                finish();

                startActivity(new Intent(this, MainActivity.class));
            } else {
                Toast.makeText(this, "Ocorreu erro no Task", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void iniciaComponentes() {

        edt_nome = findViewById(R.id.edtNome);
        edt_email = findViewById(R.id.edtemail);
        edt_senha = findViewById(R.id.edtsenha);
        progressBar = findViewById(R.id.progressBar);

        TextView text_titulo = findViewById(R.id.txt_titulo);
        text_titulo.setText("Criar Conta");

    }
}