package com.example.carloslam.proyectedii;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ContactosApi {
    @POST("/users/login")
    Call<Autorizacion> LogIn(@Body LOGI logIn);
    @GET("/users")
    Call<ResponseBody> getSecret(@Header("Authorization") String authtoken);
    @POST("/mensajes")
    Call<Autorizacion2> Mensajeria(@Body EnviarMensajes mensaje);
    @POST("/users/registrarse")
    Call<AutorizacionRegistro> Registro(@Body Registro registro);

}
