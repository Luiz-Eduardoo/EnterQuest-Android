package com.example.enterquest;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetalheChamado extends AppCompatActivity {

    private TextView txtTituloDetalhe;
    private TextView txtStatusDetalhe;
    private TextView txtDescricaoDetalhe;
    private TextView txtDataAberturaDetalhe;
    private TextView txtDataEncerramentoDetalhe;
    private ImageView imgFotoDetalhe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_chamado);

        txtTituloDetalhe = findViewById(R.id.txtTituloDetalhe);
        txtStatusDetalhe = findViewById(R.id.txtStatusDetalhe);
        txtDescricaoDetalhe = findViewById(R.id.txtDescricaoDetalhe);
        txtDataAberturaDetalhe = findViewById(R.id.txtDataAberturaDetalhe);
        txtDataEncerramentoDetalhe = findViewById(R.id.txtDataEncerramentoDetalhe);
        imgFotoDetalhe = findViewById(R.id.imgFotoDetalhe);

        Chamado chamado = (Chamado) getIntent().getSerializableExtra("chamado");

        if (chamado == null) {
            Toast.makeText(this, "Erro ao abrir chamado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        txtTituloDetalhe.setText(tratarTextoVazio(chamado.getTitulo()));
        txtStatusDetalhe.setText("Status: " + tratarTextoVazio(chamado.getStatus()));
        txtDescricaoDetalhe.setText("Descrição: " + tratarTextoVazio(chamado.getDescricao()));
        txtDataAberturaDetalhe.setText("Aberto em: " + formatarData(chamado.getDataAbertura()));
        txtDataEncerramentoDetalhe.setText("Encerrado em: " + formatarData(chamado.getDataEncerramento()));

        String foto = chamado.getFoto();

        if (foto != null && !foto.trim().isEmpty()) {
            try {
                imgFotoDetalhe.setVisibility(View.VISIBLE);
                imgFotoDetalhe.setImageURI(Uri.parse(foto));
            } catch (Exception e) {
                imgFotoDetalhe.setVisibility(View.GONE);
            }
        } else {
            imgFotoDetalhe.setVisibility(View.GONE);
        }
    }

    private String tratarTextoVazio(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "Não informado";
        }

        return texto;
    }
    private String formatarData(String dataOriginal) {
        if (dataOriginal == null || dataOriginal.trim().isEmpty()) {
            return "Não informado";
        }

        try {
            String dataTratada = dataOriginal.trim();

            if (dataTratada.contains(".")) {
                dataTratada = dataTratada.substring(0, dataTratada.indexOf("."));
            }

            java.time.LocalDateTime data = java.time.LocalDateTime.parse(dataTratada);

            java.time.format.DateTimeFormatter formato =
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

            return data.format(formato);

        } catch (Exception e) {
            return dataOriginal;
        }
    }
}