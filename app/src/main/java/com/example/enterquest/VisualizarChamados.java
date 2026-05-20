package com.example.enterquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisualizarChamados extends AppCompatActivity {

    private ListView listViewChamados;
    private Spinner spinnerFiltroStatusUsuario;

    private List<Chamado> listaChamadosCompleta = new ArrayList<>();
    private List<Chamado> listaChamadosFiltrada = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_chamados);

        listViewChamados = findViewById(R.id.listViewChamados);
        spinnerFiltroStatusUsuario = findViewById(R.id.spinnerFiltroStatusUsuario);

        configurarSpinnerFiltro();
        configurarCliqueLista();
        configurarEventoFiltro();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarChamados();
    }

    private void configurarSpinnerFiltro() {
        String[] status = {
                "Todos os status",
                "Pendente",
                "Em andamento",
                "Concluído"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                status
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltroStatusUsuario.setAdapter(adapter);
    }

    private void configurarEventoFiltro() {
        spinnerFiltroStatusUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aplicarFiltroStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void configurarCliqueLista() {
        listViewChamados.setOnItemClickListener((parent, view, position, id) -> {
            Chamado chamadoSelecionado = listaChamadosFiltrada.get(position);

            Intent intent = new Intent(VisualizarChamados.this, DetalheChamado.class);
            intent.putExtra("chamado", chamadoSelecionado);
            startActivity(intent);
        });
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

                    listaChamadosCompleta.clear();
                    listaChamadosCompleta.addAll(response.body());

                    aplicarFiltroStatus();

                } else {
                    Toast.makeText(
                            VisualizarChamados.this,
                            "Erro ao carregar chamados",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chamado>> call, Throwable t) {
                Toast.makeText(
                        VisualizarChamados.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void aplicarFiltroStatus() {
        String statusSelecionado = spinnerFiltroStatusUsuario.getSelectedItem().toString();

        listaChamadosFiltrada.clear();

        for (Chamado chamado : listaChamadosCompleta) {
            boolean passouFiltro =
                    statusSelecionado.equals("Todos os status") ||
                            statusSelecionado.equals(chamado.getStatus());

            if (passouFiltro) {
                listaChamadosFiltrada.add(chamado);
            }
        }

        atualizarListaNaTela();
    }

    private void atualizarListaNaTela() {
        List<String> listaFormatada = new ArrayList<>();

        for (Chamado chamado : listaChamadosFiltrada) {
            String item =
                    "Título: " + chamado.getTitulo() + "\n" +
                            "Setor: " + chamado.getSetor() + "\n" +
                            "Categoria: " + chamado.getCategoria() + "\n" +
                            "Status: " + chamado.getStatus();

            listaFormatada.add(item);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                VisualizarChamados.this,
                android.R.layout.simple_list_item_1,
                listaFormatada
        );

        listViewChamados.setAdapter(adapter);
    }
}