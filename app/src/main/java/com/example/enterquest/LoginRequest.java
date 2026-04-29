package com.example.enterquest;

public class LoginRequest {

    private String login;
    private String senha;
    private String cargo;

    public LoginRequest(String login, String senha, String cargo) {
        this.login = login;
        this.senha = senha;
        this.cargo = cargo;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getCargo() {
        return cargo;
    }
}