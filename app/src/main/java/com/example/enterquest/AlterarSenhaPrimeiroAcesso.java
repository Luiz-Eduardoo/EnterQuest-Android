package com.example.enterquest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlterarSenhaPrimeiroAcesso extends AppCompatActivity {

    private EditText editNovaSenhaPrimeiroAcesso;
    private EditText editConfirmarSenhaPrimeiroAcesso;
    private TextView btnAlterarSenhaPrimeiroAcesso;

    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha_primeiro_acesso);

        editNovaSenhaPrimeiroAcesso = findViewById(R.id.editNovaSenhaPrimeiroAcesso);
        editConfirmarSenhaPrimeiroAcesso = findViewById(R.id.editConfirmarSenhaPrimeiroAcesso);
        btnAlterarSenhaPrimeiroAcesso = findViewById(R.id.btnAlterarSenhaPrimeiroAcesso);

        idUsuario = getIntent().getStringExtra("idUsuario");

        btnAlterarSenhaPrimeiroAcesso.setOnClickListener(v -> alterarSenha());
    }

    private void alterarSenha() {
        String novaSenha = editNovaSenhaPrimeiroAcesso.getText().toString().trim();
        String confirmarSenha = editConfirmarSenhaPrimeiroAcesso.getText().toString().trim();

        if (novaSenha.isEmpty()) {
            editNovaSenhaPrimeiroAcesso.setError("Informe a nova senha");
            return;
        }

        if (confirmarSenha.isEmpty()) {
            editConfirmarSenhaPrimeiroAcesso.setError("Confirme a nova senha");
            return;
        }

        if (!novaSenha.equals(confirmarSenha)) {
            editConfirmarSenhaPrimeiroAcesso.setError("As senhas não coincidem");
            return;
        }

        if (!senhaForte(novaSenha)) {
            editNovaSenhaPrimeiroAcesso.setError("Use 8 caracteres, maiúscula, minúscula, número e especial");
            return;
        }

        if (idUsuario == null || idUsuario.isEmpty()) {
            Toast.makeText(this, "Usuário não identificado", Toast.LENGTH_SHORT).show();
            return;
        }

        AlterarSenhaPrimeiroAcessoRequest request =
                new AlterarSenhaPrimeiroAcessoRequest(idUsuario, novaSenha);

        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        btnAlterarSenhaPrimeiroAcesso.setEnabled(false);

        Call<String> call = apiService.alterarSenhaPrimeiroAcesso(request);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                btnAlterarSenhaPrimeiroAcesso.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(AlterarSenhaPrimeiroAcesso.this, "Senha alterada com sucesso! Faça login novamente.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(AlterarSenhaPrimeiroAcesso.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AlterarSenhaPrimeiroAcesso.this, "Erro ao alterar senha", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                btnAlterarSenhaPrimeiroAcesso.setEnabled(true);
                Toast.makeText(AlterarSenhaPrimeiroAcesso.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean senhaForte(String senha) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!?.*_-]).{8,}$";
        return senha.matches(regex);
    }
}