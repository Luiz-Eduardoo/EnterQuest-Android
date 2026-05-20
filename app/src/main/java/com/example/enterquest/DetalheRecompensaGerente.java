package com.example.enterquest;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalheRecompensaGerente extends AppCompatActivity {

    private EditText editNomeDetalheRecompensa;
    private EditText editDescricaoDetalheRecompensa;
    private EditText editCustoDetalheRecompensa;
    private EditText editEstoqueDetalheRecompensa;
    private TextView btnSalvarAlteracoesRecompensa;
    private TextView btnExcluirRecompensa;

    private Recompensa recompensa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_recompensa_gerente);

        editNomeDetalheRecompensa = findViewById(R.id.editNomeDetalheRecompensa);
        editDescricaoDetalheRecompensa = findViewById(R.id.editDescricaoDetalheRecompensa);
        editCustoDetalheRecompensa = findViewById(R.id.editCustoDetalheRecompensa);
        editEstoqueDetalheRecompensa = findViewById(R.id.editEstoqueDetalheRecompensa);
        btnSalvarAlteracoesRecompensa = findViewById(R.id.btnSalvarAlteracoesRecompensa);
        btnExcluirRecompensa = findViewById(R.id.btnExcluirRecompensa);

        recompensa = (Recompensa) getIntent().getSerializableExtra("recompensa");

        if (recompensa == null) {
            Toast.makeText(this, "Erro ao carregar recompensa", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        preencherCampos();

        btnSalvarAlteracoesRecompensa.setOnClickListener(v -> atualizarRecompensa());

        btnExcluirRecompensa.setOnClickListener(v -> confirmarExclusao());
    }

    private void preencherCampos() {
        editNomeDetalheRecompensa.setText(recompensa.getNome());
        editDescricaoDetalheRecompensa.setText(recompensa.getDescricao());
        editCustoDetalheRecompensa.setText(String.valueOf(recompensa.getCustoPontos()));
        editEstoqueDetalheRecompensa.setText(String.valueOf(recompensa.getEstoque()));
    }

    private void atualizarRecompensa() {
        String nome = editNomeDetalheRecompensa.getText().toString().trim();
        String descricao = editDescricaoDetalheRecompensa.getText().toString().trim();
        String custoTexto = editCustoDetalheRecompensa.getText().toString().trim();
        String estoqueTexto = editEstoqueDetalheRecompensa.getText().toString().trim();

        if (nome.isEmpty()) {
            editNomeDetalheRecompensa.setError("Informe o nome");
            return;
        }

        if (descricao.isEmpty()) {
            editDescricaoDetalheRecompensa.setError("Informe a descrição");
            return;
        }

        if (custoTexto.isEmpty()) {
            editCustoDetalheRecompensa.setError("Informe o custo");
            return;
        }

        if (estoqueTexto.isEmpty()) {
            editEstoqueDetalheRecompensa.setError("Informe o estoque");
            return;
        }

        int custoPontos;
        int estoque;

        try {
            custoPontos = Integer.parseInt(custoTexto);
            estoque = Integer.parseInt(estoqueTexto);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Custo e estoque devem ser números válidos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (custoPontos <= 0) {
            editCustoDetalheRecompensa.setError("O custo deve ser maior que zero");
            return;
        }

        if (estoque < 0) {
            editEstoqueDetalheRecompensa.setError("O estoque não pode ser negativo");
            return;
        }

        AtualizarRecompensaRequest request = new AtualizarRecompensaRequest(
                nome,
                descricao,
                custoPontos,
                estoque
        );

        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        btnSalvarAlteracoesRecompensa.setEnabled(false);

        Call<String> call = apiService.atualizarRecompensa(recompensa.getId(), request);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                btnSalvarAlteracoesRecompensa.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(
                            DetalheRecompensaGerente.this,
                            "Recompensa atualizada com sucesso!",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                } else {
                    Toast.makeText(
                            DetalheRecompensaGerente.this,
                            "Erro ao atualizar recompensa",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                btnSalvarAlteracoesRecompensa.setEnabled(true);

                Toast.makeText(
                        DetalheRecompensaGerente.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void confirmarExclusao() {
        new AlertDialog.Builder(this)
                .setTitle("Excluir recompensa")
                .setMessage("Tem certeza que deseja excluir esta recompensa?")
                .setPositiveButton("Sim, excluir", (dialog, which) -> excluirRecompensa())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void excluirRecompensa() {
        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        btnExcluirRecompensa.setEnabled(false);

        Call<String> call = apiService.excluirRecompensa(recompensa.getId());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                btnExcluirRecompensa.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(
                            DetalheRecompensaGerente.this,
                            "Recompensa excluída com sucesso!",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                } else {
                    Toast.makeText(
                            DetalheRecompensaGerente.this,
                            "Erro ao excluir recompensa",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                btnExcluirRecompensa.setEnabled(true);

                Toast.makeText(
                        DetalheRecompensaGerente.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}