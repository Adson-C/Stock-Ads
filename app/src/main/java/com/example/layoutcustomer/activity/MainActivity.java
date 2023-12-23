package com.example.layoutcustomer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.layoutcustomer.adapter.AdapterProduto;
import com.example.layoutcustomer.autenticacao.LoginActivity;
import com.example.layoutcustomer.helper.FirebaseHelper;
import com.example.layoutcustomer.model.Produto;
import com.example.layoutcustomer.ProdutoDAO;
import com.example.layoutcustomer.R;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterProduto.OnClick {

    private ImageButton imaAdd, imgInfo;

    private List<Produto> produtoList =  new ArrayList<>();
    private SwipeableRecyclerView rvProdutos;
    private AdapterProduto adapterProduto;

    private TextView txt_Info;

    private ProdutoDAO produtoDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        produtoDAO = new ProdutoDAO(this);

        // parou aqui
        produtoList = produtoDAO.getProdutoList();

        imaAdd = findViewById(R.id.imaadd);
        imgInfo = findViewById(R.id.imgmore);

        rvProdutos = findViewById(R.id.rvProdutos);
        txt_Info = findViewById(R.id.txtInfo);

        ouvinteClique();
    }
    @Override
    protected void onStart() {
        super.onStart();
        configRecycleView();
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

        produtoList.clear();

        produtoList = produtoDAO.getProdutoList();

        verificaQtdList();

        rvProdutos.setLayoutManager(new LinearLayoutManager(this));
        rvProdutos.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtoDAO.getProdutoList(), this);
        rvProdutos.setAdapter(adapterProduto);

        rvProdutos.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {

//                Produto produto = produtoList.get(position);
//
//                produtoDAO.deleteProduto(produto);
//                adapterProduto.notifyItemRemoved(position);

            }

            @Override
            public void onSwipedRight(int position) {

                Produto produto = produtoList.get(position);

                produtoDAO.deleteProduto(produto);
                produtoList.remove(produto);
                adapterProduto.notifyItemRemoved(position);

                verificaQtdList();


            }
        });
    }

    private void verificaQtdList(){
        if (produtoList.size() == 0){

            txt_Info.setVisibility(View.VISIBLE);
        }else {
            txt_Info.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClickListener(Produto produto) {
        Intent intent = new Intent(this, FormularioActivity.class);
        intent.putExtra("produto", produto);
        startActivity(intent);
    }

}