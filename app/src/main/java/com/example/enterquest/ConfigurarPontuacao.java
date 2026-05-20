package com.example.enterquest;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfigurarPontuacao extends AppCompatActivity {

    private EditText editPontosAbrirChamado;
    private EditText editPontosConcluirChamado;
    private TextView btnSalvarPontuacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_pontuacao);

        editPontosAbrirChamado = findViewById(R.id.editPontosAbrirChamado);
        editPontosConcluirChamado = findViewById(R.id.editPontosConcluirChamado);
        btnSalvarPontuacao = findViewById(R.id.btnSalvarPontuacao);

        carregarConfiguracaoAtual();

        btnSalvarPontuacao.setOnClickListener(v -> salvarPontuacao());
    }

    private void carregarConfiguracaoAtual() {
        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        Call<ConfiguracaoGamificacao> call = apiService.buscarConfiguracaoPontuacao();

        call.enqueue(new Callback<ConfiguracaoGamificacao>() {
            @Override
            public void onResponse(Call<ConfiguracaoGamificacao> call, Response<ConfiguracaoGamificacao> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ConfiguracaoGamificacao configuracao = response.body();

                    editPontosAbrirChamado.setText(String.valueOf(configuracao.getPontosAbrirChamado()));
                    editPontosConcluirChamado.setText(String.valueOf(configuracao.getPontosConcluirChamado()));
                } else {
                    Toast.makeText(
                            ConfigurarPontuacao.this,
                            "Erro ao carregar configuração",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<ConfiguracaoGamificacao> call, Throwable t) {
                Toast.makeText(
                        ConfigurarPontuacao.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void salvarPontuacao() {
        String pontosAbrirTexto = editPontosAbrirChamado.getText().toString().trim();
        String pontosConcluirTexto = editPontosConcluirChamado.getText().toString().trim();

        if (pontosAbrirTexto.isEmpty()) {
            editPontosAbrirChamado.setError("Informe os pontos por abertura");
            return;
        }

        if (pontosConcluirTexto.isEmpty()) {
            editPontosConcluirChamado.setError("Informe os pontos por conclusão");
            return;
        }

        int pontosAbrir;
        int pontosConcluir;

        try {
            pontosAbrir = Integer.parseInt(pontosAbrirTexto);
            pontosConcluir = Integer.parseInt(pontosConcluirTexto);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Informe valores numéricos válidos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pontosAbrir < 0) {
            editPontosAbrirChamado.setError("O valor não pode ser negativo");
            return;
        }

        if (pontosConcluir < 0) {
            editPontosConcluirChamado.setError("O valor não pode ser negativo");
            return;
        }

        ConfiguracaoGamificacao configuracao = new ConfiguracaoGamificacao(
                pontosAbrir,
                pontosConcluir
        );

        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        btnSalvarPontuacao.setEnabled(false);

        Call<String> call = apiService.atualizarConfiguracaoPontuacao(configuracao);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                btnSalvarPontuacao.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(
                            ConfigurarPontuacao.this,
                            "Pontuação atualizada com sucesso!",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                } else {
                    Toast.makeText(
                            ConfigurarPontuacao.this,
                            "Erro ao atualizar pontuação",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                btnSalvarPontuacao.setEnabled(true);

                Toast.makeText(
                        ConfigurarPontuacao.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}