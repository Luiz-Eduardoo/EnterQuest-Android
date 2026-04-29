package com.example.enterquest;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

public class DashboardGerente extends AppCompatActivity {

    private TextView txtTotalChamados;
    private TextView txtPendentes;
    private TextView txtEmAndamento;
    private TextView txtConcluidos;

    private TextView txtAbertosHoje;
    private TextView txtAbertosSemana;
    private TextView txtAbertosMes;

    private TextView txtConcluidosHoje;
    private TextView txtConcluidosSemana;
    private TextView txtConcluidosMes;

    private TextView txtSetorMaisChamados;
    private TextView txtCategoriaMaisRecorrente;

    private LinearLayout layoutChamadosPorSetor;
    private LinearLayout layoutChamadosPorCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_gerente);

        txtTotalChamados = findViewById(R.id.txtTotalChamados);
        txtPendentes = findViewById(R.id.txtPendentes);
        txtEmAndamento = findViewById(R.id.txtEmAndamento);
        txtConcluidos = findViewById(R.id.txtConcluidos);

        txtAbertosHoje = findViewById(R.id.txtAbertosHoje);
        txtAbertosSemana = findViewById(R.id.txtAbertosSemana);
        txtAbertosMes = findViewById(R.id.txtAbertosMes);

        txtConcluidosHoje = findViewById(R.id.txtConcluidosHoje);
        txtConcluidosSemana = findViewById(R.id.txtConcluidosSemana);
        txtConcluidosMes = findViewById(R.id.txtConcluidosMes);

        txtSetorMaisChamados = findViewById(R.id.txtSetorMaisChamados);
        txtCategoriaMaisRecorrente = findViewById(R.id.txtCategoriaMaisRecorrente);

        layoutChamadosPorSetor = findViewById(R.id.layoutChamadosPorSetor);
        layoutChamadosPorCategoria = findViewById(R.id.layoutChamadosPorCategoria);

        carregarDashboard();
    }

    private void carregarDashboard() {
        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        Call<DashboardResumo> call = apiService.buscarDashboard();

        call.enqueue(new Callback<DashboardResumo>() {
            @Override
            public void onResponse(Call<DashboardResumo> call, Response<DashboardResumo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    preencherDashboard(response.body());
                } else {
                    Toast.makeText(DashboardGerente.this, "Erro ao carregar dashboard", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DashboardResumo> call, Throwable t) {
                Toast.makeText(DashboardGerente.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void preencherDashboard(DashboardResumo resumo) {
        txtTotalChamados.setText("Total de chamados: " + resumo.getTotalChamados());
        txtPendentes.setText("Pendentes: " + resumo.getPendentes());
        txtEmAndamento.setText("Em andamento: " + resumo.getEmAndamento());
        txtConcluidos.setText("Concluídos: " + resumo.getConcluidos());

        txtAbertosHoje.setText("Hoje: " + resumo.getAbertosHoje());
        txtAbertosSemana.setText("Últimos 7 dias: " + resumo.getAbertosSemana());
        txtAbertosMes.setText("Este mês: " + resumo.getAbertosMes());

        txtConcluidosHoje.setText("Hoje: " + resumo.getConcluidosHoje());
        txtConcluidosSemana.setText("Últimos 7 dias: " + resumo.getConcluidosSemana());
        txtConcluidosMes.setText("Este mês: " + resumo.getConcluidosMes());

        txtSetorMaisChamados.setText(
                "Setor com mais chamados: " +
                        tratarTexto(resumo.getSetorMaisChamados()) +
                        " (" + resumo.getQuantidadeSetorMaisChamados() + ")"
        );

        txtCategoriaMaisRecorrente.setText(
                "Categoria mais recorrente: " +
                        tratarTexto(resumo.getCategoriaMaisRecorrente()) +
                        " (" + resumo.getQuantidadeCategoriaMaisRecorrente() + ")"
        );

        montarGraficoBarras(layoutChamadosPorSetor, "Chamados por setor", resumo.getChamadosPorSetor());
        montarGraficoBarras(layoutChamadosPorCategoria, "Chamados por categoria", resumo.getChamadosPorCategoria());
    }

    private String formatarMapa(Map<String, Integer> mapa) {
        if (mapa == null || mapa.isEmpty()) {
            return "Nenhum dado encontrado";
        }

        StringBuilder resultado = new StringBuilder();

        for (Map.Entry<String, Integer> entrada : mapa.entrySet()) {
            resultado.append("- ")
                    .append(entrada.getKey())
                    .append(": ")
                    .append(entrada.getValue())
                    .append("\n");
        }

        return resultado.toString();
    }

    private void montarGraficoBarras(LinearLayout container, String titulo, Map<String, Integer> dados) {
        container.removeAllViews();

        TextView txtTitulo = new TextView(this);
        txtTitulo.setText(titulo);
        txtTitulo.setTextSize(15);
        txtTitulo.setTextColor(Color.parseColor("#071A33"));
        txtTitulo.setTypeface(null, android.graphics.Typeface.BOLD);
        container.addView(txtTitulo);

        if (dados == null || dados.isEmpty()) {
            TextView txtVazio = new TextView(this);
            txtVazio.setText("Nenhum dado encontrado");
            txtVazio.setTextSize(14);
            txtVazio.setTextColor(Color.parseColor("#6B7280"));
            txtVazio.setPadding(0, 12, 0, 0);
            container.addView(txtVazio);
            return;
        }

        int maiorValor = 0;

        for (Integer valor : dados.values()) {
            if (valor != null && valor > maiorValor) {
                maiorValor = valor;
            }
        }

        for (Map.Entry<String, Integer> entrada : dados.entrySet()) {
            String nome = entrada.getKey();
            int valor = entrada.getValue() != null ? entrada.getValue() : 0;

            TextView txtLinha = new TextView(this);
            txtLinha.setText(nome + "  •  " + valor);
            txtLinha.setTextSize(13);
            txtLinha.setTextColor(Color.parseColor("#374151"));
            txtLinha.setPadding(0, 16, 0, 6);
            container.addView(txtLinha);

            LinearLayout barraFundo = new LinearLayout(this);
            barraFundo.setOrientation(LinearLayout.HORIZONTAL);
            barraFundo.setBackgroundColor(Color.parseColor("#DFF7FA"));

            LinearLayout.LayoutParams paramsFundo = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    18
            );
            barraFundo.setLayoutParams(paramsFundo);

            View barraPreenchida = new View(this);
            barraPreenchida.setBackgroundColor(Color.parseColor("#00AFC1"));

            int larguraPeso = maiorValor > 0 ? valor : 0;

            LinearLayout.LayoutParams paramsBarra = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    larguraPeso
            );

            barraPreenchida.setLayoutParams(paramsBarra);

            View espacoRestante = new View(this);

            LinearLayout.LayoutParams paramsEspaco = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    maiorValor - larguraPeso
            );

            espacoRestante.setLayoutParams(paramsEspaco);

            barraFundo.addView(barraPreenchida);
            barraFundo.addView(espacoRestante);

            container.addView(barraFundo);
        }
    }

    private String tratarTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "Não informado";
        }

        return texto;
    }
}