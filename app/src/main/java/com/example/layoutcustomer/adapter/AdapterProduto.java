package com.example.layoutcustomer.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.layoutcustomer.R;
import com.example.layoutcustomer.model.Produto;

import java.util.List;

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder> {

    private List<Produto> produtoList;

    private OnClick onClick;

    public AdapterProduto(List<Produto> produtoList, OnClick onclick) {
        this.produtoList = produtoList;
        this.onClick = onclick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produto, parent, false);

        return new MyViewHolder(itemView);
        // será chamdo o layout item_produtos

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // exibir na tela o layout

        Produto produto = produtoList.get(position);

        holder.txtProduto.setText(produto.getNome());
        holder.txtUni.setText("Estoque: " + produto.getEstoque());
        holder.txtPreco.setText("R$: " + produto.getPreco());

        holder.itemView.setOnClickListener(view -> onClick.onClickListener(produto));

    }

    @Override
    public int getItemCount() {
        return produtoList.size();
    }

    public interface OnClick{
        void onClickListener(Produto produto);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtProduto, txtUni, txtPreco;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // quais elementos será mostrado no layout
            txtProduto = itemView.findViewById(R.id.texproduto);
            txtUni = itemView.findViewById(R.id.txtquanti);
            txtPreco = itemView.findViewById(R.id.textpreco);
        }
    }
}
