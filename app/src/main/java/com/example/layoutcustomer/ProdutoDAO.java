package com.example.layoutcustomer;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.layoutcustomer.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private final SQLiteDatabase write;
    private final SQLiteDatabase read;

    public ProdutoDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        this.write = dbHelper.getWritableDatabase();
        this.read = dbHelper.getReadableDatabase();
    }

    public void salvarProduto(Produto produto){
        ContentValues cv = new ContentValues();

        cv.put("nome", produto.getNome());
        cv.put("estoque", produto.getEstoque());
        cv.put("preco", produto.getPreco());

        try {
            write.insert(DBHelper.TB_PRODUTO, null, cv);
//            write.close();
        }catch (Exception e){
            Log.i("ERROR", "Erro ao salvar o produto: " + e.getMessage());
        }

    }

    public List<Produto> getProdutoList(){

        List<Produto> produtoList = new ArrayList<>();

        String sql =  "SELECT * FROM " + DBHelper.TB_PRODUTO + ";";
        Cursor c  = read.rawQuery(sql, null);

        while (c.moveToNext()){
            @SuppressLint("Range") int id = c.getInt(c.getColumnIndex("id"));
            @SuppressLint("Range") String nome = c.getString(c.getColumnIndex("nome"));
            @SuppressLint("Range") int estoque = c.getInt(c.getColumnIndex("estoque"));
            @SuppressLint("Range") double preco = c.getDouble(c.getColumnIndex("preco"));

            Produto produto = new Produto();
            produto.setId(id);
            produto.setNome(nome);
            produto.setEstoque(estoque);
            produto.setPreco(preco);

            produtoList.add(produto);

        }

        return produtoList;
    }

    public void atualizarProdutos(Produto produto){

        ContentValues cv =  new ContentValues();
        cv.put("nome", produto.getNome());
        cv.put("estoque", produto.getEstoque());
        cv.put("preco", produto.getPreco());

        String where = "id=?";
        String[] args = {String.valueOf(produto.getId())};

        try {
                write.update(DBHelper.TB_PRODUTO, cv, where, args);
//                write.close();
        }catch (Exception e){
            Log.i("ERROR", "Erro ao Atualizar produto: " + e.getMessage());
        }

    }

    public void deleteProduto(Produto produto){

        try {

            String[] args = {String.valueOf(produto.getId())};

            String where = "id=?";
            write.delete(DBHelper.TB_PRODUTO, where, args);

        }catch (Exception e){
            Log.i("ERROR", "Erro ao deletar produto!" + e.getMessage());
        }
    }

}
