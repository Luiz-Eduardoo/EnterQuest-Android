package com.example.enterquest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Tecnico extends AppCompatActivity {

    private TextView btnVisualizarChamados, textView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tecnico);

        textView6 = findViewById(R.id.textView6);

        String nomeTecnico = getSharedPreferences("usuario_logado", MODE_PRIVATE)
                .getString("nome", "");

        if (nomeTecnico != null && !nomeTecnico.trim().isEmpty()) {
            textView6.setText("Olá, " + nomeTecnico + " 👋");
        } else {
            textView6.setText("Olá 👋");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnVisualizarChamados = findViewById(R.id.button2);

        btnVisualizarChamados.setOnClickListener(v -> {
            Intent intent = new Intent(Tecnico.this, VisualizarChamadoTecnico.class);
            startActivity(intent);
        });
    }
}