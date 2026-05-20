package com.example.enterquest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Gerente extends AppCompatActivity {

    private TextView btnAbrirChamadoGerente;
    private TextView btnMeusChamadosGerente;
    private TextView btnTodosChamadosGerente;
    private TextView btnCriarUsuarioGerente;
    private TextView btnAbrirDashboardGerente;

    private TextView btnGerenciarRecompensasGerente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerente);

        TextView txtOlaGerente = findViewById(R.id.txtOlaGerente);

        String nomeGerente = getSharedPreferences("usuario_logado", MODE_PRIVATE)
                .getString("nome", "");

        if (nomeGerente != null && !nomeGerente.trim().isEmpty()) {
            txtOlaGerente.setText("Olá, " + nomeGerente + " 👋");
        } else {
            txtOlaGerente.setText("Olá 👋");
        }

        btnAbrirChamadoGerente = findViewById(R.id.btnAbrirChamadoGerente);
        btnMeusChamadosGerente = findViewById(R.id.btnMeusChamadosGerente);
        btnTodosChamadosGerente = findViewById(R.id.btnTodosChamadosGerente);
        btnCriarUsuarioGerente = findViewById(R.id.btnCriarUsuarioGerente);
        btnAbrirDashboardGerente = findViewById(R.id.btnAbrirDashboardGerente);
        btnGerenciarRecompensasGerente = findViewById(R.id.btnGerenciarRecompensasGerente);

        btnAbrirChamadoGerente.setOnClickListener(v -> {
            Intent intent = new Intent(Gerente.this, AbrirChamado.class);
            startActivity(intent);
        });

        btnMeusChamadosGerente.setOnClickListener(v -> {
            Intent intent = new Intent(Gerente.this, VisualizarChamados.class);
            startActivity(intent);
        });

        btnTodosChamadosGerente.setOnClickListener(v -> {
            Intent intent = new Intent(Gerente.this, VisualizarChamadoTecnico.class);
            startActivity(intent);
        });

         btnCriarUsuarioGerente.setOnClickListener(v -> {
            Intent intent = new Intent(Gerente.this, CriarUsuario.class);
            startActivity(intent);
        });

        btnAbrirDashboardGerente.setOnClickListener(v -> {
            Intent intent = new Intent(Gerente.this, DashboardGerente.class);
            startActivity(intent);
        });
        btnGerenciarRecompensasGerente.setOnClickListener(v -> {
            Intent intent = new Intent(Gerente.this, GerenciarRecompensas.class);
            startActivity(intent);
        });
    }
}