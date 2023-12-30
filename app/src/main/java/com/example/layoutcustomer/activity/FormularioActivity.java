package com.example.layoutcustomer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.layoutcustomer.model.Produto;
import com.example.layoutcustomer.R;

public class FormularioActivity extends AppCompatActivity {

    EditText edtproduto, edtquanti, edtpreco;
    ImageButton imgvoltar;

    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);


        edtproduto = findViewById(R.id.edtproduto);
        edtquanti = findViewById(R.id.edtquanti);
        edtpreco = findViewById(R.id.edtpreco);

        imgvoltar = findViewById(R.id.imgVoltar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            produto = (Produto) bundle.getSerializable("produto");

            editProduto();
        }

        voltariniciar();
    }

    private void editProduto(){
        edtproduto.setText(produto.getNome());
        edtquanti.setText(String.valueOf(produto.getEstoque()));
        edtpreco.setText(String.valueOf(produto.getPreco()));
    }
    private void voltariniciar(){

        imgvoltar.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });

    }

    public void salvarPrtoduto(View view){

        String nome = edtproduto.getText().toString();
        String quantidade = edtquanti.getText().toString();
        String preco = edtpreco.getText().toString();

        if (!nome.isEmpty()){
            if (!quantidade.isEmpty()){

                int qtd = Integer.parseInt(quantidade);

                if (qtd >= 1){

                    if (!preco.isEmpty()){

                        double valorProduto = Double.parseDouble(preco);

                        if (valorProduto >  0){

                            if (produto == null) produto = new Produto();
                            produto.setNome(nome);
                            produto.setEstoque(qtd);
                            produto.setPreco(valorProduto);

                            produto.salvarProduto();

                            finish();

                        }else {
                            edtpreco.requestFocus();
                            edtpreco.setError("Informe o preço maior que 0.");
                        }

                    }else {
                        edtpreco.requestFocus();
                        edtpreco.setError("Informe o preço do produto.");
                    }

                }else {
                    edtquanti.requestFocus();
                    edtquanti.setError("Informe a quantidade maior que 0.");
                }

            }else {
                edtquanti.requestFocus();
                edtquanti.setError("Informe a quantidade do produto.");
            }

        }else {
            edtproduto.requestFocus();
            edtproduto.setError("Informe o nome do produto.");
        }

    }
}