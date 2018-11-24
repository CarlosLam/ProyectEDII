package com.example.carloslam.proyectedii;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Registrarse extends AppCompatActivity {

    String ip = "http://192.168.43.23:7000";
    Retrofit.Builder builder = new Retrofit.Builder().baseUrl(ip).addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.build();
    ContactosApi api = retrofit.create(ContactosApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
    }

    public void Crear(View view){
        EditText nombre = findViewById(R.id.editText3);
        EditText usuario = findViewById(R.id.editText5);
        EditText contra = findViewById(R.id.editText6);
        EditText apellido = findViewById(R.id.editText4);

        String Nombre = nombre.getText().toString();
        String Usuario = usuario.getText().toString();
        String Contra = contra.getText().toString();
        String Apellido = apellido.getText().toString();

        if (Nombre.length() == 0){
            Toast.makeText(this,"Ingresar nombre",Toast.LENGTH_SHORT).show();
        }else if (Apellido.length() == 0){
            Toast.makeText(this,"Ingresar apellido",Toast.LENGTH_SHORT).show();
        }else if (Usuario.length() == 0){
            Toast.makeText(this,"Ingresar usuario",Toast.LENGTH_SHORT).show();
        }else if (Contra.length() == 0) {
            Toast.makeText(this, "Ingresar contraseña", Toast.LENGTH_SHORT).show();
        }else{
            Registro r = new Registro(Nombre,Contra,Usuario,Apellido);
            SendUsers(r);
        }
    }

    private void SendUsers(Registro users) {
        Call<AutorizacionRegistro> call = api.Registro(users);
        call.enqueue(new Callback<AutorizacionRegistro>() {
            @Override
            public void onResponse(Call<AutorizacionRegistro> call, Response<AutorizacionRegistro> response) {
                try {
                    Toast.makeText(Registrarse.this, "", Toast.LENGTH_SHORT).show();
                    String string = response.body().getMessage();
                    Validar validar = new Validar();
                    boolean q = validar.Autorizacion(string);
                    if (q == true) {
                        Toast.makeText(Registrarse.this, "Autentifación valida", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Registrarse.this, Login.class);
                        startActivity(intent);
                    }
                }
                catch (Exception e) {
                    Toast.makeText(Registrarse.this, "Error de autenticacion", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<AutorizacionRegistro> call, Throwable t) {
                Toast.makeText(Registrarse.this,"Autentifacion valida",Toast.LENGTH_SHORT).show();
            }
        });
    }

    class Validar {
        public ArrayList<String> ReadJson(String json) {
            String s= json.substring(1, json.length()-1);
            ArrayList<String> Json = new ArrayList<String>();
            String tempo ="";
            for (int i = 0; i < s.length(); i++) {
                String v = s.charAt(i)+"";
                if(v.equals("{")) {
                    tempo += v;
                }if(!v.equals("{") &&!v.equals("]")) {
                    tempo += v;
                }if(v.equals("}")) {
                    tempo = tempo+v;
                    String d = tempo.substring(0,1);
                    if(!d.equals(",")) {
                        String agregar = tempo.substring(1,tempo.length()-2);
                        Json.add(agregar);
                    }if(d.equals(",")) {
                        String agregar = tempo.substring(2,tempo.length()-2);
                        Json.add(agregar);
                    }
                    tempo ="";
                }
            }
            ArrayList<String> Json2 = new ArrayList<String>();
            for (int i = 0; i < Json.size(); i++) {
                String[] campos = Json.get(i).split(",");
                String name = campos[1].substring(12,campos[1].length()-1);
                Json2.add(name+","+"lol");
            }
            return Json2;
        }

        public boolean Autorizacion(String s) {
            boolean permiso = false;
            try {
                if(s.equals("¡Usuario creado con exito!")) {
                    permiso =true;
                } else {
                    Toast.makeText(Registrarse.this,"Ya existe el usuario",Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                Toast.makeText(Registrarse.this,"Error de autenticacion",Toast.LENGTH_SHORT).show();
            }
            return permiso;
        }
    }
}
