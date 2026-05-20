package com.example.enterquest;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MinhasRecompensas extends AppCompatActivity {

    private ListView listViewMinhasRecompensas;
    private List<ResgateRecompensa> listaResgates = new ArrayList<>();
    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_recompensas);

        listViewMinhasRecompensas = findViewById(R.id.listViewMinhasRecompensas);

        idUsuarioLogado = getSharedPreferences("usuario_logado", MODE_PRIVATE)
                .getString("idUsuario", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarMinhasRecompensas();
    }

    private void carregarMinhasRecompensas() {
        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        Call<List<ResgateRecompensa>> call =
                apiService.listarResgatesPorUsuario(idUsuarioLogado);

        call.enqueue(new Callback<List<ResgateRecompensa>>() {
            @Override
            public void onResponse(Call<List<ResgateRecompensa>> call, Response<List<ResgateRecompensa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaResgates.clear();
                    listaResgates.addAll(response.body());

                    atualizarLista();
                } else {
                    Toast.makeText(
                            MinhasRecompensas.this,
                            "Erro ao carregar recompensas resgatadas",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<List<ResgateRecompensa>> call, Throwable t) {
                Toast.makeText(
                        MinhasRecompensas.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void atualizarLista() {
        if (listaResgates.isEmpty()) {
            List<String> listaVazia = new ArrayList<>();
            listaVazia.add("Você ainda não resgatou nenhuma recompensa.");

            ArrayAdapter<String> adapterVazio = new ArrayAdapter<>(
                    MinhasRecompensas.this,
                    android.R.layout.simple_list_item_1,
                    listaVazia
            );

            listViewMinhasRecompensas.setAdapter(adapterVazio);
            return;
        }

        ResgateRecompensaAdapter adapter = new ResgateRecompensaAdapter(
                MinhasRecompensas.this,
                listaResgates
        );

        listViewMinhasRecompensas.setAdapter(adapter);
    }

    private String formatarData(String data) {
        if (data == null || data.trim().isEmpty()) {
            return "Não informada";
        }

        return data.replace("T", " ");
    }
}