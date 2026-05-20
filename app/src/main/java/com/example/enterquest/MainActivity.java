package com.example.enterquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText editLogin;
    private TextInputEditText editSenha;
    private Spinner spinner;
    private TextView btnCheck2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        TextView txtEsqueciSenha = findViewById(R.id.txtEsqueciSenha);

        txtEsqueciSenha.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EsqueciSenha.class);
            startActivity(intent);
        });

        editLogin = findViewById(R.id.editLogin);
        editSenha = findViewById(R.id.editSenha);
        spinner = findViewById(R.id.spinner);
        btnCheck2 = findViewById(R.id.btnCheck2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.opcoes_spinner,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnCheck2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                realizarLogin();

            }
        });
    }

    private void realizarLogin() {
        String login = editLogin.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();
        String cargoSelecionado = spinner.getSelectedItem().toString();

        if (login.isEmpty()) {
            editLogin.setError("Informe o login");
            return;
        }

        if (senha.isEmpty()) {
            editSenha.setError("Informe a senha");
            return;
        }

        if (cargoSelecionado.equals("Selecione o cargo")) {
            Toast.makeText(MainActivity.this, "Selecione uma opção válida", Toast.LENGTH_SHORT).show();
            return;
        }

        String cargo = converterCargo(cargoSelecionado);


        LoginRequest loginRequest = new LoginRequest(login, senha, cargo);

        ApiService apiService = RetrofitClient
                .getRetrofitInstance()
                .create(ApiService.class);

        Call<Usuario> call = apiService.login(loginRequest);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Usuario usuario = response.body();

                    getSharedPreferences("usuario_logado", MODE_PRIVATE)
                            .edit()
                            .putString("idUsuario", usuario.getId())
                            .putString("nome", usuario.getNome())
                            .putString("cargo", usuario.getCargo())
                            .apply();

                    if (Boolean.TRUE.equals(usuario.getPrimeiroAcesso())) {
                        Intent intent = new Intent(MainActivity.this, AlterarSenhaPrimeiroAcesso.class);
                        intent.putExtra("idUsuario", usuario.getId());
                        startActivity(intent);
                        finish();
                        return;
                    }

                    getSharedPreferences("usuario_logado", MODE_PRIVATE)
                            .edit()
                            .putString("idUsuario", usuario.getId())
                            .putString("nome", usuario.getNome())
                            .putString("cargo", usuario.getCargo())
                            .apply();

                    Toast.makeText(MainActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

                    abrirTelaPorCargo(usuario.getCargo());

                } else {
                    Toast.makeText(MainActivity.this, "Login, senha ou cargo inválido", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String converterCargo(String cargoSelecionado) {
        cargoSelecionado = cargoSelecionado.trim();

        if (cargoSelecionado.equals("Funcionário") || cargoSelecionado.equals("FUNCIONARIO")) {
            return "FUNCIONARIO";
        } else if (cargoSelecionado.equals("Técnico") || cargoSelecionado.equals("TECNICO")) {
            return "TECNICO";
        } else if (cargoSelecionado.equals("Admin") || cargoSelecionado.equals("GERENTE")) {
            return "GERENTE";
        }

        return "";
    }

    private void abrirTelaPorCargo(String cargo) {
        Intent intent;

        if (cargo.equals("FUNCIONARIO")) {
            intent = new Intent(MainActivity.this, Funcionario.class);
        } else if (cargo.equals("TECNICO")) {
            intent = new Intent(MainActivity.this, Tecnico.class);
        } else if (cargo.equals("GERENTE")) {
            intent = new Intent(MainActivity.this, Gerente.class);
        } else {
            Toast.makeText(MainActivity.this, "Cargo não reconhecido", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(intent);
        finish();
    }

}