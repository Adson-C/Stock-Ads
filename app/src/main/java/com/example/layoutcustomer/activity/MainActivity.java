package com.example.layoutcustomer.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.layoutcustomer.adapter.AdapterProduto;
import com.example.layoutcustomer.autenticacao.LoginActivity;
import com.example.layoutcustomer.helper.FirebaseHelper;
import com.example.layoutcustomer.model.Produto;
import com.example.layoutcustomer.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterProduto.OnClick {

    private ImageButton imaAdd, imgInfo;

    private List<Produto> produtoList =  new ArrayList<>();
    private SwipeableRecyclerView rvProdutos;
    private AdapterProduto adapterProduto;
    private ProgressBar progressBar;

    private TextView txt_Info;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarComponente();
        ouvinteClique();

        configRecycleView();
    }

    private void iniciarComponente() {
        imaAdd = findViewById(R.id.imaadd);
        imgInfo = findViewById(R.id.imgmore);

        rvProdutos = findViewById(R.id.rvProdutos);
        txt_Info = findViewById(R.id.txtInfo);

        progressBar = findViewById(R.id.progressBar);

    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperarProdutos();

    }
    
    private void recuperarProdutos(){

        DatabaseReference produtoRef = FirebaseHelper.getDatabaseReference()
                .child("produtos")
                .child(FirebaseHelper.getIdFirebase());
        produtoRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtoList.clear();
                for (DataSnapshot snap:
                     snapshot.getChildren()) {
                    Produto produto = snap.getValue(Produto.class);


                    produtoList.add(produto);
                }

                verificaQtdList();

                Collections.reverse(produtoList);
                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    
    private void ouvinteClique(){
        imaAdd.setOnClickListener(view -> {
            startActivity(new Intent(this, FormularioActivity.class));
        });

        imgInfo.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, imgInfo);
            popupMenu.getMenuInflater().inflate(R.menu.toobar_ads, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.menu_sobre){
                    Toast.makeText(this, "Sobre", Toast.LENGTH_SHORT).show();
                }else if (menuItem.getItemId() == R.id.menu_sair){
                    FirebaseHelper.getAuth().signOut();
                    startActivity(new Intent(this, LoginActivity.class));
                }
                return true;
            });

            popupMenu.show();
        });
    }

    private void configRecycleView(){

        rvProdutos.setLayoutManager(new LinearLayoutManager(this));
        rvProdutos.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtoList, this);
        rvProdutos.setAdapter(adapterProduto);

        rvProdutos.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {

            }

            @Override
            public void onSwipedRight(int position) {

                Produto produto = produtoList.get(position);

                produtoList.remove(produto);
                produto.deletarProduto();
                adapterProduto.notifyItemRemoved(position);

                verificaQtdList();


            }
        });
    }

    private void verificaQtdList(){
        if (produtoList.size() == 0){

            txt_Info.setText("Nenhum produto cadastrado.");
            txt_Info.setVisibility(View.VISIBLE);
        }else {
            txt_Info.setVisibility(View.GONE);
        }

        progressBar.setVisibility(View.GONE);
    }
    @Override
    public void onClickListener(Produto produto) {
        Intent intent = new Intent(this, FormularioActivity.class);
        intent.putExtra("produto", produto);
        startActivity(intent);
    }

}