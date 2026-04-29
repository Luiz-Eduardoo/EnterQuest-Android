package com.example.enterquest;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbrirChamado extends AppCompatActivity {

    private EditText editTitulo;
    private Spinner spinnerSetor;
    private EditText editDescricao;
    private Spinner spinnerCategoria;
    private TextView btnSelecionarFoto;
    private TextView btnAbrirChamado;
    private ImageView imgFotoChamado;

    private Uri fotoSelecionada;

    private boolean enviandoChamado = false;

    private ActivityResultLauncher<String> selecionarFotoLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_chamado);

        editTitulo = findViewById(R.id.editTitulo);
        spinnerSetor = findViewById(R.id.spinnerSetor);
        editDescricao = findViewById(R.id.editDescricao);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnSelecionarFoto = findViewById(R.id.btnSelecionarFoto);
        btnAbrirChamado = findViewById(R.id.btnAbrirChamado);
        imgFotoChamado = findViewById(R.id.imgFotoChamado);

        configurarSpinners();
        configurarSelecionarFoto();

        btnSelecionarFoto.setOnClickListener(v -> selecionarFotoLauncher.launch("image/*"));

        btnAbrirChamado.setOnClickListener(v -> abrirChamado());
    }

    private void configurarSpinners() {
        String[] setores = {
                "Selecione um setor",
                "Financeiro",
                "RH",
                "Administrativo",
                "TI",
                "Estoque",
                "Atendimento",
                "Outro"
        };

        String[] categorias = {
                "Selecione uma categoria",
                "Informática",
                "Jardinagem",
                "Manutenção",
                "Limpeza",
                "Outro"
        };

        ArrayAdapter<String> adapterSetor = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                setores
        );

        adapterSetor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSetor.setAdapter(adapterSetor);

        ArrayAdapter<String> adapterCategoria = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categorias
        );

        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapterCategoria);
    }

    private void configurarSelecionarFoto() {
        selecionarFotoLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        fotoSelecionada = uri;
                        imgFotoChamado.setImageURI(uri);
                    }
                }
        );
    }

    private void abrirChamado() {
        String titulo = editTitulo.getText().toString().trim();
        String setor = spinnerSetor.getSelectedItem().toString();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String descricao = editDescricao.getText().toString().trim();
        String foto = "";

        if (fotoSelecionada != null) {
            foto = fotoSelecionada.toString();
        }

        if (titulo.isEmpty()) {
            editTitulo.setError("Informe o título do chamado");
            return;
        }

        if (setor.equals("Selecione um setor")) {
            Toast.makeText(this, "Selecione um setor", Toast.LENGTH_SHORT).show();
            return;
        }

        if (categoria.equals("Selecione uma categoria")) {
            Toast.makeText(this, "Selecione uma categoria", Toast.LENGTH_SHORT).show();
            return;
        }

        if (descricao.isEmpty()) {
            editDescricao.setError("Informe a descrição do problema");
            return;
        }

        if (enviandoChamado) {
            return;
        }

        enviandoChamado = true;
        btnAbrirChamado.setEnabled(false);

        Chamado chamado = new Chamado(
                titulo,
                setor,
                categoria,
                descricao,
                foto
        );

        String idUsuarioLogado = getSharedPreferences("usuario_logado", MODE_PRIVATE)
                .getString("idUsuario", "");

        chamado.setIdUsuario(idUsuarioLogado);

        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        Call<String> call = apiService.criarChamado(chamado);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                enviandoChamado = false;
                btnAbrirChamado.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(AbrirChamado.this, "Chamado aberto com sucesso!", Toast.LENGTH_SHORT).show();

                    editTitulo.setText("");
                    editDescricao.setText("");
                    spinnerSetor.setSelection(0);
                    spinnerCategoria.setSelection(0);
                    imgFotoChamado.setImageDrawable(null);
                    fotoSelecionada = null;
                } else {
                    Toast.makeText(AbrirChamado.this, "Erro ao abrir chamado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                enviandoChamado = false;
                btnAbrirChamado.setEnabled(true);
                Toast.makeText(AbrirChamado.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}