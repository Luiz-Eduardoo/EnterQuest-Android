package com.example.enterquest;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastrarRecompensa extends AppCompatActivity {

    private EditText editNomeRecompensa;
    private EditText editDescricaoRecompensa;
    private EditText editCustoPontosRecompensa;
    private EditText editEstoqueRecompensa;
    private TextView btnSalvarRecompensa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_recompensa);

        editNomeRecompensa = findViewById(R.id.editNomeRecompensa);
        editDescricaoRecompensa = findViewById(R.id.editDescricaoRecompensa);
        editCustoPontosRecompensa = findViewById(R.id.editCustoPontosRecompensa);
        editEstoqueRecompensa = findViewById(R.id.editEstoqueRecompensa);
        btnSalvarRecompensa = findViewById(R.id.btnSalvarRecompensa);

        btnSalvarRecompensa.setOnClickListener(v -> cadastrarRecompensa());
    }

    private void cadastrarRecompensa() {
        String nome = editNomeRecompensa.getText().toString().trim();
        String descricao = editDescricaoRecompensa.getText().toString().trim();
        String custoTexto = editCustoPontosRecompensa.getText().toString().trim();
        String estoqueTexto = editEstoqueRecompensa.getText().toString().trim();

        if (nome.isEmpty()) {
            editNomeRecompensa.setError("Informe o nome da recompensa");
            return;
        }

        if (descricao.isEmpty()) {
            editDescricaoRecompensa.setError("Informe a descrição");
            return;
        }

        if (custoTexto.isEmpty()) {
            editCustoPontosRecompensa.setError("Informe o custo em pontos");
            return;
        }

        if (estoqueTexto.isEmpty()) {
            editEstoqueRecompensa.setError("Informe o estoque");
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
            editCustoPontosRecompensa.setError("O custo deve ser maior que zero");
            return;
        }

        if (estoque < 0) {
            editEstoqueRecompensa.setError("O estoque não pode ser negativo");
            return;
        }

        CriarRecompensaRequest request = new CriarRecompensaRequest(
                nome,
                descricao,
                custoPontos,
                estoque
        );

        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        btnSalvarRecompensa.setEnabled(false);

        Call<String> call = apiService.criarRecompensa(request);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                btnSalvarRecompensa.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(
                            CadastrarRecompensa.this,
                            "Recompensa cadastrada com sucesso!",
                            Toast.LENGTH_SHORT
                    ).show();

                    finish();
                } else {
                    Toast.makeText(
                            CadastrarRecompensa.this,
                            "Erro ao cadastrar recompensa",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                btnSalvarRecompensa.setEnabled(true);

                Toast.makeText(
                        CadastrarRecompensa.this,
                        "Falha na conexão: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}