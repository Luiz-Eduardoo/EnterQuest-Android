package com.example.enterquest;

public class Usuario {

    private String id;
    private String nome;
    private String login;
    private String senha;
    private String cargo;

    private Boolean primeiroAcesso;



    public String getId() {
        return id;
    }


    public String getNome() {
        return nome;
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

    public Boolean getPrimeiroAcesso() {
        return primeiroAcesso;
    }

}