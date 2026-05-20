package com.example.enterquest;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CriarUsuario extends AppCompatActivity {

    private EditText editNomeUsuario;
    private EditText editLoginUsuario;
    private EditText editEmailUsuario;
    private EditText editSenhaUsuario;
    private Spinner spinnerCargoUsuario;
    private Button btnCriarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_usuario);

        editNomeUsuario = findViewById(R.id.editNomeUsuario);
        editLoginUsuario = findViewById(R.id.editLoginUsuario);
        editEmailUsuario = findViewById(R.id.editEmailUsuario);
        editSenhaUsuario = findViewById(R.id.editSenhaUsuario);
        spinnerCargoUsuario = findViewById(R.id.spinnerCargoUsuario);
        btnCriarUsuario = findViewById(R.id.btnCriarUsuario);

        configurarSpinnerCargo();

        btnCriarUsuario.setOnClickListener(v -> criarUsuario());
    }

    private void configurarSpinnerCargo() {
        String[] cargos = {
                "Selecione o cargo",
                "Funcionário",
                "Técnico",
                "Admin"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                cargos
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCargoUsuario.setAdapter(adapter);
    }

    private void criarUsuario() {
        String nome = editNomeUsuario.getText().toString().trim();
        String login = editLoginUsuario.getText().toString().trim();
        String email = editEmailUsuario.getText().toString().trim();
        String senha = editSenhaUsuario.getText().toString().trim();
        String cargoSelecionado = spinnerCargoUsuario.getSelectedItem().toString();

        if (nome.isEmpty()) {
            editNomeUsuario.setError("Informe o nome");
            return;
        }

        if (login.isEmpty()) {
            editLoginUsuario.setError("Informe o login");
            return;
        }

        if (email.isEmpty()) {
            editEmailUsuario.setError("Informe o e-mail");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmailUsuario.setError("Informe um e-mail válido");
            return;
        }

        if (senha.isEmpty()) {
            editSenhaUsuario.setError("Informe a senha provisória");
            return;
        }

        if (!senhaForte(senha)) {
            editSenhaUsuario.setError("Use 8 caracteres, maiúscula, minúscula, número e especial");
            return;
        }

        if (cargoSelecionado.equals("Selecione o cargo")) {
            Toast.makeText(this, "Selecione um cargo", Toast.LENGTH_SHORT).show();
            return;
        }

        String cargo = converterCargo(cargoSelecionado);

        CriarUsuarioRequest request = new CriarUsuarioRequest(
                nome,
                login,
                email,
                senha,
                cargo
        );

        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        btnCriarUsuario.setEnabled(false);

        Call<String> call = apiService.criarUsuario(request);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                btnCriarUsuario.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(CriarUsuario.this, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show();

                    editNomeUsuario.setText("");
                    editLoginUsuario.setText("");
                    editEmailUsuario.setText("");
                    editSenhaUsuario.setText("");
                    spinnerCargoUsuario.setSelection(0);

                } else {
                    Toast.makeText(CriarUsuario.this, "Erro ao criar usuário", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                btnCriarUsuario.setEnabled(true);
                Toast.makeText(CriarUsuario.this, "Falha na conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String converterCargo(String cargoSelecionado) {
        if (cargoSelecionado.equals("Funcionário")) {
            return "FUNCIONARIO";
        } else if (cargoSelecionado.equals("Técnico")) {
            return "TECNICO";
        } else if (cargoSelecionado.equals("Admin")) {
            return "GERENTE";
        }

        return "";
    }

    private boolean senhaForte(String senha) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!?.*_-]).{8,}$";
        return senha.matches(regex);
    }
}