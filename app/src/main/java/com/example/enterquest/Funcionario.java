package com.example.enterquest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Funcionario extends AppCompatActivity {

    private TextView textView6, btnIrAbrirChamado, btnVisualizarChamados, btnLojaRecompensas, btnAssistenteVirtualFuncionario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcionario);

        textView6 = findViewById(R.id.textView6);

        String nomeFuncionario = getSharedPreferences("usuario_logado", MODE_PRIVATE)
                .getString("nome", "");


        if (nomeFuncionario != null && !nomeFuncionario.trim().isEmpty()) {
            textView6.setText("Olá, " + nomeFuncionario + " 👋");
        } else {
            textView6.setText("Olá 👋");
        }

        btnIrAbrirChamado = findViewById(R.id.btnIrAbrirChamado);
        btnLojaRecompensas = findViewById(R.id.btnLojaRecompensas);
        btnAssistenteVirtualFuncionario = findViewById(R.id.btnAssistenteVirtualFuncionario);

        btnIrAbrirChamado.setOnClickListener(v -> {
            Intent intent = new Intent(Funcionario.this, AbrirChamado.class);
            startActivity(intent);
        });

        TextView btnVisualizarChamados = findViewById(R.id.btnVisualizarChamados);

        btnVisualizarChamados.setOnClickListener(v -> {
            Intent intent = new Intent(Funcionario.this, VisualizarChamados.class);
            startActivity(intent);
        });

        btnLojaRecompensas.setOnClickListener(v -> {
            Intent intent = new Intent(Funcionario.this, LojaRecompensas.class);
            startActivity(intent);
        });

        btnAssistenteVirtualFuncionario.setOnClickListener(v -> {
            Intent intent = new Intent(Funcionario.this, Chatbot.class);
            startActivity(intent);
        });
    }
}