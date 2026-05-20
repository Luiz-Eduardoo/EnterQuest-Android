package com.example.enterquest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LojaRecompensas extends AppCompatActivity {

    private TextView txtPontosUsuarioLoja;
    private ListView listViewLojaRecompensas;

    private TextView btnMinhasRecompensas;

    private List<Recompensa> listaRecompensas = new ArrayList<>();
    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loja_recompensas);

        txtPontosUsuarioLoja = findViewById(R.id.txtPontosUsuarioLoja);
        listViewLojaRecompensas = findViewById(R.id.listViewLojaRecompensas);
        btnMinhasRecompensas = findViewById(R.id.btnMinhasRecompensas);

        idUsuarioLogado = getSharedPreferences("usuario_logado", MODE_PRIVATE)
                .getString("idUsuario", "");

        listViewLojaRecompensas.setOnItemClickListener((parent, view, position, id) -> {
            Recompensa recompensaSelecionada = listaRecompensas.get(position);
            confirmarResgate(recompensaSelecionada);
        });
        btnMinhasRecompensas.setOnClickListener(v -> {
            Intent intent = new Intent(LojaRecompensas.this, MinhasRecompensas.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarPontosUsuario();
        carregarRecompensas();
    }

    private void carregarPontosUsuario() {
        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        Call<Integer> call = apiService.buscarPontosUsuario(idUsuarioLogado);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    txtPontosUsuarioLoja.setText(response.body() + " pontos");
                } else {
                    Toast.makeText(
                            LojaRecompensas.this,
                            "Erro ao carregar pontos",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(
                        LojaRecompensas.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
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
                            LojaRecompensas.this,
                            "Erro ao carregar recompensas",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<List<Recompensa>> call, Throwable t) {
                Toast.makeText(
                        LojaRecompensas.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void atualizarLista() {
        RecompensaLojaAdapter adapter = new RecompensaLojaAdapter(
                LojaRecompensas.this,
                listaRecompensas
        );

        listViewLojaRecompensas.setAdapter(adapter);
    }

    private void confirmarResgate(Recompensa recompensa) {
        new AlertDialog.Builder(this)
                .setTitle("Resgatar recompensa")
                .setMessage(
                        "Deseja resgatar \"" + recompensa.getNome() +
                                "\" por " + recompensa.getCustoPontos() + " pontos?"
                )
                .setPositiveButton("Sim, resgatar", (dialog, which) -> resgatarRecompensa(recompensa))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void resgatarRecompensa(Recompensa recompensa) {
        ResgatarRecompensaRequest request = new ResgatarRecompensaRequest(
                idUsuarioLogado,
                recompensa.getId()
        );

        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        Call<String> call = apiService.resgatarRecompensa(request);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(
                            LojaRecompensas.this,
                            "Recompensa resgatada com sucesso!",
                            Toast.LENGTH_SHORT
                    ).show();

                    carregarPontosUsuario();
                    carregarRecompensas();

                } else {
                    Toast.makeText(
                            LojaRecompensas.this,
                            "Não foi possível resgatar a recompensa",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(
                        LojaRecompensas.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}