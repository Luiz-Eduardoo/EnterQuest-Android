package com.example.enterquest;

import java.io.Serializable;

public class Chamado implements Serializable {

    private String id;
    private String titulo;
    private String setor;
    private String categoria;
    private String descricao;
    private String foto;
    private String status;
    private String idUsuario;
    private String dataAbertura;
    private String dataEncerramento;

    public Chamado(String titulo, String setor, String categoria, String descricao, String foto) {
        this.titulo = titulo;
        this.setor = setor;
        this.categoria = categoria;
        this.descricao = descricao;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getSetor() {
        return setor;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getFoto() {
        return foto;
    }

    public String getStatus() {
        return status;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getDataAbertura() {
        return dataAbertura;
    }

    public String getDataEncerramento() {
        return dataEncerramento;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}