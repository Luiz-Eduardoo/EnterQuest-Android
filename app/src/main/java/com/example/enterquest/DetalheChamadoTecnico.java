package com.example.enterquest;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AlertDialog;

public class DetalheChamadoTecnico extends AppCompatActivity {

    private TextView txtTituloDetalheTecnico;
    private TextView txtSetorDetalheTecnico;
    private TextView txtCategoriaDetalheTecnico;
    private TextView txtDescricaoDetalheTecnico;
    private TextView txtDataAberturaDetalheTecnico;
    private TextView txtDataEncerramentoDetalheTecnico;
    private ImageView imgFotoDetalheTecnico;
    private Spinner spinnerStatusTecnico;
    private Button btnAtualizarStatus;

    private Chamado chamado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_chamado_tecnico);

        txtTituloDetalheTecnico = findViewById(R.id.txtTituloDetalheTecnico);
        txtSetorDetalheTecnico = findViewById(R.id.txtSetorDetalheTecnico);
        txtCategoriaDetalheTecnico = findViewById(R.id.txtCategoriaDetalheTecnico);
        txtDescricaoDetalheTecnico = findViewById(R.id.txtDescricaoDetalheTecnico);
        txtDataAberturaDetalheTecnico = findViewById(R.id.txtDataAberturaDetalheTecnico);
        txtDataEncerramentoDetalheTecnico = findViewById(R.id.txtDataEncerramentoDetalheTecnico);
        imgFotoDetalheTecnico = findViewById(R.id.imgFotoDetalheTecnico);
        spinnerStatusTecnico = findViewById(R.id.spinnerStatusTecnico);
        btnAtualizarStatus = findViewById(R.id.btnAtualizarStatus);

        configurarSpinnerStatus();

        chamado = (Chamado) getIntent().getSerializableExtra("chamado");

        if (chamado == null) {
            Toast.makeText(this, "Erro ao abrir chamado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        preencherDadosChamado();

        btnAtualizarStatus.setOnClickListener(v -> atualizarStatus());
    }

    private void configurarSpinnerStatus() {
        String[] status = {
                "Pendente",
                "Em andamento",
                "Concluído"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                status
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatusTecnico.setAdapter(adapter);
    }

    private void preencherDadosChamado() {
        txtTituloDetalheTecnico.setText(tratarTextoVazio(chamado.getTitulo()));
        txtSetorDetalheTecnico.setText("Setor: " + tratarTextoVazio(chamado.getSetor()));
        txtCategoriaDetalheTecnico.setText("Categoria: " + tratarTextoVazio(chamado.getCategoria()));
        txtDescricaoDetalheTecnico.setText("Descrição: " + tratarTextoVazio(chamado.getDescricao()));
        txtDataAberturaDetalheTecnico.setText("Aberto em: " + formatarData(chamado.getDataAbertura()));
        txtDataEncerramentoDetalheTecnico.setText("Encerrado em: " + formatarData(chamado.getDataEncerramento()));

        selecionarStatusAtual();

        if ("Concluído".equals(chamado.getStatus())) {
            spinnerStatusTecnico.setEnabled(false);
            btnAtualizarStatus.setEnabled(false);
            btnAtualizarStatus.setText("Chamado já concluído");
        }

        String foto = chamado.getFoto();

        if (foto != null && !foto.trim().isEmpty()) {
            try {
                imgFotoDetalheTecnico.setVisibility(View.VISIBLE);
                imgFotoDetalheTecnico.setImageURI(Uri.parse(foto));
            } catch (Exception e) {
                imgFotoDetalheTecnico.setVisibility(View.GONE);
            }
        } else {
            imgFotoDetalheTecnico.setVisibility(View.GONE);
        }
    }

    private void selecionarStatusAtual() {
        String statusAtual = chamado.getStatus();

        if (statusAtual == null) {
            return;
        }

        if (statusAtual.equals("Pendente")) {
            spinnerStatusTecnico.setSelection(0);
        } else if (statusAtual.equals("Em andamento")) {
            spinnerStatusTecnico.setSelection(1);
        } else if (statusAtual.equals("Concluído")) {
            spinnerStatusTecnico.setSelection(2);
        }
    }

    private void atualizarStatus() {
        String novoStatus = spinnerStatusTecnico.getSelectedItem().toString();
        String statusAtual = chamado.getStatus();

        if (chamado.getId() == null || chamado.getId().isEmpty()) {
            Toast.makeText(this, "ID do chamado não encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        if ("Concluído".equals(statusAtual)) {
            Toast.makeText(this, "Este chamado já foi concluído", Toast.LENGTH_SHORT).show();
            return;
        }

        if ("Pendente".equals(novoStatus) && !"Pendente".equals(statusAtual)) {
            Toast.makeText(this, "Não é possível voltar o chamado para pendente", Toast.LENGTH_SHORT).show();
            selecionarStatusAtual();
            return;
        }

        if (novoStatus.equals(statusAtual)) {
            Toast.makeText(this, "O chamado já está com esse status", Toast.LENGTH_SHORT).show();
            return;
        }

        if ("Concluído".equals(novoStatus)) {
            new AlertDialog.Builder(this)
                    .setTitle("Concluir chamado")
                    .setMessage("Tem certeza que deseja concluir este chamado? Depois disso, ele não poderá ser alterado.")
                    .setPositiveButton("Sim, concluir", (dialog, which) -> enviarAtualizacaoStatus(novoStatus))
                    .setNegativeButton("Cancelar", null)
                    .show();
        } else {
            enviarAtualizacaoStatus(novoStatus);
        }
    }

    private void enviarAtualizacaoStatus(String novoStatus) {
        Map<String, String> body = new HashMap<>();
        body.put("status", novoStatus);

        String idTecnicoLogado = getSharedPreferences("usuario_logado", MODE_PRIVATE)
                .getString("idUsuario", "");

        body.put("idTecnico", idTecnicoLogado);

        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        Call<String> call = apiService.atualizarStatusChamado(chamado.getId(), body);

        btnAtualizarStatus.setEnabled(false);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                btnAtualizarStatus.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(DetalheChamadoTecnico.this, "Status atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DetalheChamadoTecnico.this, "Erro ao atualizar status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                btnAtualizarStatus.setEnabled(true);
                Toast.makeText(DetalheChamadoTecnico.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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