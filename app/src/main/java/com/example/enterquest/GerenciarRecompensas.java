package com.example.enterquest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GerenciarRecompensas extends AppCompatActivity {

    private TextView btnConfigurarPontuacao;
    private TextView btnVisualizarResgates;
    private TextView btnCadastrarNovaRecompensa;
    private ListView listViewRecompensas;

    private List<Recompensa> listaRecompensas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_recompensas);

        btnConfigurarPontuacao = findViewById(R.id.btnConfigurarPontuacao);
        btnCadastrarNovaRecompensa = findViewById(R.id.btnCadastrarNovaRecompensa);
        listViewRecompensas = findViewById(R.id.listViewRecompensas);
        btnVisualizarResgates = findViewById(R.id.btnVisualizarResgates);

        btnConfigurarPontuacao.setOnClickListener(v -> {
            Intent intent = new Intent(GerenciarRecompensas.this, ConfigurarPontuacao.class);
            startActivity(intent);
        });

        btnCadastrarNovaRecompensa.setOnClickListener(v -> {
            Intent intent = new Intent(GerenciarRecompensas.this, CadastrarRecompensa.class);
            startActivity(intent);
        });

        btnVisualizarResgates.setOnClickListener(v -> {
            Intent intent = new Intent(GerenciarRecompensas.this, ResgateRecompensasAdmin.class);
            startActivity(intent);
        });

        listViewRecompensas.setOnItemClickListener((parent, view, position, id) -> {
            Recompensa recompensaSelecionada = listaRecompensas.get(position);

            Intent intent = new Intent(GerenciarRecompensas.this, DetalheRecompensaGerente.class);
            intent.putExtra("recompensa", recompensaSelecionada);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarRecompensas();
    }

    private void carregarRecompensas() {
        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        Call<List<Recompensa>> call = apiService.listarRecompensas();

        call.enqueue(new Callback<List<Recompensa>>() {
            @Override
            public void onResponse(Call<List<Recompensa>> call, Response<List<Recompensa>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    listaRecompensas.clear();
                    listaRecompensas.addAll(response.body());

                    atualizarLista();

                } else {
                    Toast.makeText(
                            GerenciarRecompensas.this,
                            "Erro ao carregar recompensas",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<List<Recompensa>> call, Throwable t) {
                Toast.makeText(
                        GerenciarRecompensas.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void atualizarLista() {
        RecompensaGerenteAdapter adapter = new RecompensaGerenteAdapter(
                GerenciarRecompensas.this,
                listaRecompensas
        );

        listViewRecompensas.setAdapter(adapter);
    }
}