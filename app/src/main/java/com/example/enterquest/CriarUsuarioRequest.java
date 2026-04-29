package com.example.enterquest;

public class CriarUsuarioRequest {

    private String nome;
    private String login;
    private String email;
    private String senha;
    private String cargo;

    public CriarUsuarioRequest(String nome, String login, String email, String senha, String cargo) {
        this.nome = nome;
        this.login = login;
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getCargo() {
        return cargo;
    }
}