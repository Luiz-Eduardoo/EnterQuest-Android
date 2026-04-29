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

public class VisualizarChamadoTecnico extends AppCompatActivity {

    private ListView listViewChamadosTecnico;
    private List<Chamado> listaChamados = new ArrayList<>();
    private ChamadoTecnicoAdapter chamadoTecnicoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_chamado_tecnico);

        listViewChamadosTecnico = findViewById(R.id.listViewChamadosTecnico);

        chamadoTecnicoAdapter = new ChamadoTecnicoAdapter(this, listaChamados);
        listViewChamadosTecnico.setAdapter(chamadoTecnicoAdapter);

        listViewChamadosTecnico.setOnItemClickListener((parent, view, position, id) -> {
            Chamado chamadoSelecionado = listaChamados.get(position);

            Intent intent = new Intent(VisualizarChamadoTecnico.this, DetalheChamadoTecnico.class);
            intent.putExtra("chamado", chamadoSelecionado);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarChamados();
    }

    private void carregarChamados() {
        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        Call<List<Chamado>> call = apiService.listarChamados();

        call.enqueue(new Callback<List<Chamado>>() {
            @Override
            public void onResponse(Call<List<Chamado>> call, Response<List<Chamado>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    listaChamados.clear();
                    listaChamados.addAll(response.body());

                    chamadoTecnicoAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(VisualizarChamadoTecnico.this, "Erro ao carregar chamados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chamado>> call, Throwable t) {
                Toast.makeText(VisualizarChamadoTecnico.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}