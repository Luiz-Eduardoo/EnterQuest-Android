package com.example.enterquest;

import java.io.Serializable;

public class Recompensa implements Serializable {

    private String id;
    private String nome;
    private String descricao;
    private int custoPontos;
    private int estoque;

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getCustoPontos() {
        return custoPontos;
    }

    public int getEstoque() {
        return estoque;
    }
}