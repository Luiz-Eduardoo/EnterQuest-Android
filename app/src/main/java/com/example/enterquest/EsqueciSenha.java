package com.example.enterquest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class EsqueciSenha extends AppCompatActivity {

    private EditText editEmailRecuperacao;
    private TextView btnEnviarCodigo;
    private TextView txtVoltarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_esqueci_senha);

        editEmailRecuperacao = findViewById(R.id.editEmailRecuperacao);
        btnEnviarCodigo = findViewById(R.id.btnEnviarCodigo);
        txtVoltarLogin = findViewById(R.id.txtVoltarLogin);

        btnEnviarCodigo.setOnClickListener(v -> enviarCodigo());

        txtVoltarLogin.setOnClickListener(v -> {
            Intent intent = new Intent(EsqueciSenha.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void enviarCodigo() {
        String email = editEmailRecuperacao.getText().toString().trim();

        if (email.isEmpty()) {
            editEmailRecuperacao.setError("Informe o e-mail");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmailRecuperacao.setError("Informe um e-mail válido");
            return;
        }

        Toast.makeText(this, "Código enviado para o e-mail informado", Toast.LENGTH_SHORT).show();
    }
}