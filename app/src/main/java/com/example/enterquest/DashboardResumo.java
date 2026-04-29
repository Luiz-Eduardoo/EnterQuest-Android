package com.example.enterquest;

import java.util.Map;

public class DashboardResumo {

    private int totalChamados;
    private int pendentes;
    private int emAndamento;
    private int concluidos;

    private Map<String, Integer> chamadosPorSetor;
    private Map<String, Integer> chamadosPorCategoria;

    private int abertosHoje;
    private int abertosSemana;
    private int abertosMes;

    private int concluidosHoje;
    private int concluidosSemana;
    private int concluidosMes;

    private String setorMaisChamados;
    private int quantidadeSetorMaisChamados;

    private String categoriaMaisRecorrente;
    private int quantidadeCategoriaMaisRecorrente;

    public int getTotalChamados() {
        return totalChamados;
    }

    public int getPendentes() {
        return pendentes;
    }

    public int getEmAndamento() {
        return emAndamento;
    }

    public int getConcluidos() {
        return concluidos;
    }

    public Map<String, Integer> getChamadosPorSetor() {
        return chamadosPorSetor;
    }

    public Map<String, Integer> getChamadosPorCategoria() {
        return chamadosPorCategoria;
    }

    public int getAbertosHoje() {
        return abertosHoje;
    }

    public int getAbertosSemana() {
        return abertosSemana;
    }

    public int getAbertosMes() {
        return abertosMes;
    }

    public int getConcluidosHoje() {
        return concluidosHoje;
    }

    public int getConcluidosSemana() {
        return concluidosSemana;
    }

    public int getConcluidosMes() {
        return concluidosMes;
    }

    public String getSetorMaisChamados() {
        return setorMaisChamados;
    }

    public int getQuantidadeSetorMaisChamados() {
        return quantidadeSetorMaisChamados;
    }

    public String getCategoriaMaisRecorrente() {
        return categoriaMaisRecorrente;
    }

    public int getQuantidadeCategoriaMaisRecorrente() {
        return quantidadeCategoriaMaisRecorrente;
    }
}