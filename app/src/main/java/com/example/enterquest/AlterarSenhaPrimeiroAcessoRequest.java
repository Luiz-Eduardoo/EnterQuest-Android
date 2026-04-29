package com.example.enterquest;

public class AlterarSenhaPrimeiroAcessoRequest {

    private String idUsuario;
    private String novaSenha;

    public AlterarSenhaPrimeiroAcessoRequest(String idUsuario, String novaSenha) {
        this.idUsuario = idUsuario;
        this.novaSenha = novaSenha;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getNovaSenha() {
        return novaSenha;
    }
}