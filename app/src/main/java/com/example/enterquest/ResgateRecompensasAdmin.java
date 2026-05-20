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

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class ResgateRecompensasAdmin extends AppCompatActivity {

    private ListView listViewResgatesAdmin;
    private List<ResgateRecompensa> listaResgates = new ArrayList<>();

    private EditText editPesquisarCodigoResgate;

    private List<ResgateRecompensa> listaResgatesFiltrada = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgate_recompensas_admin);

        listViewResgatesAdmin = findViewById(R.id.listViewResgatesAdmin);
        editPesquisarCodigoResgate = findViewById(R.id.editPesquisarCodigoResgate);

        configurarPesquisa();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarTodosResgates();
    }

    private void carregarTodosResgates() {
        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        Call<List<ResgateRecompensa>> call = apiService.listarTodosResgates();

        call.enqueue(new Callback<List<ResgateRecompensa>>() {
            @Override
            public void onResponse(Call<List<ResgateRecompensa>> call, Response<List<ResgateRecompensa>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaResgates.clear();
                    listaResgates.addAll(response.body());

                    aplicarFiltroCodigo(
                            editPesquisarCodigoResgate.getText().toString().trim()
                    );
                } else {
                    Toast.makeText(
                            ResgateRecompensasAdmin.this,
                            "Erro ao carregar os resgates",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<List<ResgateRecompensa>> call, Throwable t) {
                Toast.makeText(
                        ResgateRecompensasAdmin.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void atualizarLista() {
        if (listaResgatesFiltrada.isEmpty()) {
            List<String> listaVazia = new ArrayList<>();
            listaVazia.add("Nenhuma recompensa foi resgatada ainda.");

            ArrayAdapter<String> adapterVazio = new ArrayAdapter<>(
                    ResgateRecompensasAdmin.this,
                    android.R.layout.simple_list_item_1,
                    listaVazia
            );

            listViewResgatesAdmin.setAdapter(adapterVazio);
            return;
        }

        ResgateAdminAdapter adapter = new ResgateAdminAdapter(
                ResgateRecompensasAdmin.this,
                listaResgatesFiltrada,
                resgate -> confirmarEntrega(resgate)
        );

        listViewResgatesAdmin.setAdapter(adapter);
    }

    private void confirmarEntrega(ResgateRecompensa resgate) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Confirmar entrega")
                .setMessage("Deseja confirmar que a recompensa foi entregue? Esse cupom não poderá ser reutilizado.")
                .setPositiveButton("Sim, confirmar", (dialog, which) -> enviarConfirmacaoEntrega(resgate))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void enviarConfirmacaoEntrega(ResgateRecompensa resgate) {
        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        Call<String> call = apiService.confirmarEntregaResgate(resgate.getId());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(
                            ResgateRecompensasAdmin.this,
                            "Entrega confirmada com sucesso!",
                            Toast.LENGTH_SHORT
                    ).show();

                    carregarTodosResgates();
                } else {
                    Toast.makeText(
                            ResgateRecompensasAdmin.this,
                            "Não foi possível confirmar a entrega",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(
                        ResgateRecompensasAdmin.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void configurarPesquisa() {
        editPesquisarCodigoResgate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aplicarFiltroCodigo(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void aplicarFiltroCodigo(String textoPesquisa) {
        listaResgatesFiltrada.clear();

        if (textoPesquisa.isEmpty()) {
            listaResgatesFiltrada.addAll(listaResgates);
        } else {
            for (ResgateRecompensa resgate : listaResgates) {
                String codigo = resgate.getCodigoResgate();

                if (codigo != null &&
                        codigo.toLowerCase().contains(textoPesquisa.toLowerCase())) {
                    listaResgatesFiltrada.add(resgate);
                }
            }
        }

        atualizarLista();
    }
}