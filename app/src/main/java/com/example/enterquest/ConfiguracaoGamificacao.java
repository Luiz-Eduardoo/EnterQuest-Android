package com.example.enterquest;

public class ConfiguracaoGamificacao {

    private int pontosAbrirChamado;
    private int pontosConcluirChamado;

    public ConfiguracaoGamificacao() {
    }

    public ConfiguracaoGamificacao(int pontosAbrirChamado, int pontosConcluirChamado) {
        this.pontosAbrirChamado = pontosAbrirChamado;
        this.pontosConcluirChamado = pontosConcluirChamado;
    }

    public int getPontosAbrirChamado() {
        return pontosAbrirChamado;
    }

    public int getPontosConcluirChamado() {
        return pontosConcluirChamado;
    }
}