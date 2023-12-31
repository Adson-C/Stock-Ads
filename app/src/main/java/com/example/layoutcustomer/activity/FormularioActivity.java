package com.example.layoutcustomer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.layoutcustomer.model.Produto;
import com.example.layoutcustomer.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class FormularioActivity extends AppCompatActivity {

    private static final int REQUEST_GALERIA = 100;

    EditText edtproduto, edtquanti, edtpreco;
    ImageButton imgvoltar;
    ImageView img_produto;

    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);


        iniciaComponete();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            produto = (Produto) bundle.getSerializable("produto");

            editProduto();
        }

        voltariniciar();
    }

    private void iniciaComponete() {
        edtproduto = findViewById(R.id.edtproduto);
        edtquanti = findViewById(R.id.edtquanti);
        edtpreco = findViewById(R.id.edtpreco);

        imgvoltar = findViewById(R.id.imgVoltar);
        img_produto = findViewById(R.id.imgProduto);
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

    public void carregarImagem(View view){
        verificarPermissaoGaleria();
    }

    private void verificarPermissaoGaleria() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                abrirGaleria();

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FormularioActivity.this, "Permissão Negada.", Toast.LENGTH_SHORT).show();
            }
        };
        showDialogPermissao(permissionListener, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALERIA);
    }
    private void showDialogPermissao(PermissionListener listener, String[] permissoes){
        TedPermission.create()
                .setPermissionListener(listener)
                .setDeniedTitle("Permissões")
                .setDeniedMessage("Você negou a permissão para acessar a galeria do dispositivo, Deseja permitir ?")
                .setDeniedCloseButtonText("NÃO")
                .setGotoSettingButtonText("SIM")
                .setPermissions(permissoes)
                .check();
    }
}