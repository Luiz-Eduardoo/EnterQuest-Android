package com.example.enterquest;

public class ResgatarRecompensaRequest {

    private String idUsuario;
    private String idRecompensa;

    public ResgatarRecompensaRequest(String idUsuario, String idRecompensa) {
        this.idUsuario = idUsuario;
        this.idRecompensa = idRecompensa;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getIdRecompensa() {
        return idRecompensa;
    }
}