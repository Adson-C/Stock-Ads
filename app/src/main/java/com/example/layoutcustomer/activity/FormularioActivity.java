package com.example.layoutcustomer.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.layoutcustomer.helper.FirebaseHelper;
import com.example.layoutcustomer.model.Produto;
import com.example.layoutcustomer.R;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.util.List;

public class FormularioActivity extends AppCompatActivity {

    private static final int REQUEST_GALERIA = 100;
    private String caminhoImagem;
    private Bitmap imgBitmap;

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

                            // metodo para criar um produto com imagem
                            if (caminhoImagem == null){
                                Toast.makeText(this, "Selecione uma imagem.", Toast.LENGTH_SHORT).show();
                            }else {
                                salvarImgProduto();
                            }

//                            produto.salvarProduto();

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

    // metodos para slavar as imagens
    private void salvarImgProduto(){

        StorageReference reference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("produtos")
                .child(FirebaseHelper.getIdFirebase())
                .child(produto.getId() + ".jpeg");




        // pegando a Uri da imagem
        UploadTask uploadTask = reference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnCompleteListener(task -> {

            produto.setUrlImagem(task.getResult().toString());
            produto.salvarProduto();

            finish();

        })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    public void verificarPermissaoGaleria(View view) {
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

    // metodo resposavél para tratar o resultado
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_GALERIA){

                Uri localImagemSelecionada = data.getData();
                caminhoImagem =localImagemSelecionada.toString();

                if (Build.VERSION.SDK_INT < 28){
                    try {
                        imgBitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), localImagemSelecionada);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    ImageDecoder.Source source = ImageDecoder.createSource(getBaseContext().getContentResolver(), localImagemSelecionada);
                    try {
                        imgBitmap = ImageDecoder.decodeBitmap(source);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                    img_produto.setImageBitmap(imgBitmap);
            }
        }
    }
}