package com.example.enterquest;

public class ResgateRecompensa {

    private String id;
    private String idUsuario;
    private String idRecompensa;
    private String nomeRecompensa;
    private String descricaoRecompensa;
    private int custoPontos;
    private String dataResgate;
    private String codigoResgate;
    private boolean utilizado;
    private String dataUtilizacao;

    public String getId() {
        return id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getIdRecompensa() {
        return idRecompensa;
    }

    public String getNomeRecompensa() {
        return nomeRecompensa;
    }

    public String getDescricaoRecompensa() {
        return descricaoRecompensa;
    }

    public int getCustoPontos() {
        return custoPontos;
    }

    public String getDataResgate() {
        return dataResgate;
    }
    public String getCodigoResgate() {
        return codigoResgate;
    }
    public boolean isUtilizado() {
        return utilizado;
    }

    public String getDataUtilizacao() {
        return dataUtilizacao;
    }
}