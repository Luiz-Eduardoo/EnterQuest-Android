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

public class VisualizarChamadoTecnico extends AppCompatActivity {

    private ListView listViewChamadosTecnico;
    private Spinner spinnerFiltroStatus;
    private Spinner spinnerFiltroSetor;

    private List<Chamado> listaChamadosCompleta = new ArrayList<>();
    private List<Chamado> listaChamadosFiltrada = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_chamado_tecnico);

        listViewChamadosTecnico = findViewById(R.id.listViewChamadosTecnico);
        spinnerFiltroStatus = findViewById(R.id.spinnerFiltroStatus);
        spinnerFiltroSetor = findViewById(R.id.spinnerFiltroSetor);

        configurarSpinners();
        configurarCliqueLista();
        configurarEventosFiltros();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarChamados();
    }

    private void configurarSpinners() {
        String[] status = {
                "Todos os status",
                "Pendente",
                "Em andamento",
                "Concluído"
        };

        String[] setores = {
                "Todos os setores",
                "Financeiro",
                "RH",
                "Administrativo",
                "TI",
                "Estoque",
                "Atendimento",
                "Outro"
        };

        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                status
        );
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltroStatus.setAdapter(adapterStatus);

        ArrayAdapter<String> adapterSetor = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                setores
        );
        adapterSetor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltroSetor.setAdapter(adapterSetor);
    }

    private void configurarEventosFiltros() {
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aplicarFiltros();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        spinnerFiltroStatus.setOnItemSelectedListener(listener);
        spinnerFiltroSetor.setOnItemSelectedListener(listener);
    }

    private void configurarCliqueLista() {
        listViewChamadosTecnico.setOnItemClickListener((parent, view, position, id) -> {
            Chamado chamadoSelecionado = listaChamadosFiltrada.get(position);

            Intent intent = new Intent(
                    VisualizarChamadoTecnico.this,
                    DetalheChamadoTecnico.class
            );

            intent.putExtra("chamado", chamadoSelecionado);
            startActivity(intent);
        });
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
                    listaChamadosCompleta.clear();
                    listaChamadosCompleta.addAll(response.body());

                    aplicarFiltros();
                } else {
                    Toast.makeText(
                            VisualizarChamadoTecnico.this,
                            "Erro ao carregar chamados",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chamado>> call, Throwable t) {
                Toast.makeText(
                        VisualizarChamadoTecnico.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void aplicarFiltros() {
        String statusSelecionado = spinnerFiltroStatus.getSelectedItem().toString();
        String setorSelecionado = spinnerFiltroSetor.getSelectedItem().toString();

        listaChamadosFiltrada.clear();

        for (Chamado chamado : listaChamadosCompleta) {
            boolean passouFiltroStatus =
                    statusSelecionado.equals("Todos os status") ||
                            statusSelecionado.equals(chamado.getStatus());

            boolean passouFiltroSetor =
                    setorSelecionado.equals("Todos os setores") ||
                            setorSelecionado.equals(chamado.getSetor());

            if (passouFiltroStatus && passouFiltroSetor) {
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
                VisualizarChamadoTecnico.this,
                android.R.layout.simple_list_item_1,
                listaFormatada
        );

        listViewChamadosTecnico.setAdapter(adapter);
    }
}