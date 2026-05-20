package com.example.enterquest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @GET("recompensas")
    Call<List<Recompensa>> listarRecompensas();

    @POST("recompensas")
    Call<String> criarRecompensa(@Body CriarRecompensaRequest request);

    @PUT("recompensas/{idRecompensa}")
    Call<String> atualizarRecompensa(
            @Path("idRecompensa") String idRecompensa,
            @Body AtualizarRecompensaRequest request
    );

    @DELETE("recompensas/{idRecompensa}")
    Call<String> excluirRecompensa(@Path("idRecompensa") String idRecompensa);

    @GET("gamificacao/configuracao")
    Call<ConfiguracaoGamificacao> buscarConfiguracaoPontuacao();

    @PUT("gamificacao/configuracao")
    Call<String> atualizarConfiguracaoPontuacao(@Body ConfiguracaoGamificacao configuracao);

    @GET("gamificacao/pontos/{idUsuario}")
    Call<Integer> buscarPontosUsuario(@Path("idUsuario") String idUsuario);

    @POST("recompensas/resgatar")
    Call<String> resgatarRecompensa(@Body ResgatarRecompensaRequest request);

    @GET("recompensas/resgates/usuario/{idUsuario}")
    Call<List<ResgateRecompensa>> listarResgatesPorUsuario(
            @Path("idUsuario") String idUsuario
    );
    @GET("recompensas/resgates")
    Call<List<ResgateRecompensa>> listarTodosResgates();
    @PUT("recompensas/resgates/{idResgate}/confirmar-entrega")
    Call<String> confirmarEntregaResgate(
            @Path("idResgate") String idResgate
    );
}
