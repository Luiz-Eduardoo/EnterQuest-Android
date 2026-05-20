package com.example.enterquest;

public class CriarRecompensaRequest {

    private String nome;
    private String descricao;
    private int custoPontos;
    private int estoque;

    public CriarRecompensaRequest(String nome, String descricao, int custoPontos, int estoque) {
        this.nome = nome;
        this.descricao = descricao;
        this.custoPontos = custoPontos;
        this.estoque = estoque;
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