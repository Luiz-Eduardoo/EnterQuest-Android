package com.example.enterquest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisualizarChamados extends AppCompatActivity {

    private ListView listViewChamados;
    private List<Chamado> listaChamados = new ArrayList<>();
    private ChamadoAdapter chamadoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_chamados);

        listViewChamados = findViewById(R.id.listViewChamados);

        chamadoAdapter = new ChamadoAdapter(this, listaChamados);
        listViewChamados.setAdapter(chamadoAdapter);

        listViewChamados.setOnItemClickListener((parent, view, position, id) -> {
            Chamado chamadoSelecionado = listaChamados.get(position);

            Intent intent = new Intent(VisualizarChamados.this, DetalheChamado.class);
            intent.putExtra("chamado", chamadoSelecionado);
            startActivity(intent);
        });

        carregarChamados();
    }

    private void carregarChamados() {
        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        String idUsuarioLogado = getSharedPreferences("usuario_logado", MODE_PRIVATE)
                .getString("idUsuario", "");

        Call<List<Chamado>> call = apiService.listarChamadosPorUsuario(idUsuarioLogado);

        call.enqueue(new Callback<List<Chamado>>() {
            @Override
            public void onResponse(Call<List<Chamado>> call, Response<List<Chamado>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    listaChamados.clear();
                    listaChamados.addAll(response.body());

                    chamadoAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(VisualizarChamados.this, "Erro ao carregar chamados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chamado>> call, Throwable t) {
                Toast.makeText(VisualizarChamados.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}