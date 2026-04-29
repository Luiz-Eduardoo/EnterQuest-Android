package com.example.enterquest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("chamados")
    Call<String> criarChamado(@Body Chamado chamado);

    @GET("chamados")
    Call<List<Chamado>> listarChamados();

    @POST("auth/login")
    Call<Usuario> login(@Body LoginRequest loginRequest);

    @GET("chamados/usuario/{idUsuario}")
    Call<List<Chamado>> listarChamadosPorUsuario(@Path("idUsuario") String idUsuario);

    @PUT("chamados/{idChamado}/status")
    Call<String> atualizarStatusChamado(
            @Path("idChamado") String idChamado,
            @Body Map<String, String> body
    );

    @PUT("auth/alterar-senha-primeiro-acesso")
    Call<String> alterarSenhaPrimeiroAcesso(@Body AlterarSenhaPrimeiroAcessoRequest request);
    @POST("auth/criar-usuario")
    Call<String> criarUsuario(@Body CriarUsuarioRequest request);

    @GET("chamados/dashboard")
    Call<DashboardResumo> buscarDashboard();
}
