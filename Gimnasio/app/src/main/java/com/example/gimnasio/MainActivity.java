package com.example.gimnasio;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    EditText txtUsuario, txtPassword, txtBody;
    Button btnLogin,btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtUsuario = findViewById(R.id.etLogin);
        txtPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnExit = findViewById(R.id.btnExit);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //leerWs();
                //enviarWs();
                login(txtUsuario.getText().toString(),txtPassword.getText().toString());
            }
        });


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salirDeLaApp();
            }
        });


    }

    /* private void leerWs() {
        String url = "https://tercerbackendgym-production.up.railway.app/entrenadores/1";

        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    txtUser.setText(jsonObject.getString("nombre"));
                    txtNick.setText(jsonObject.getString("nick"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error:", error.getMessage() != null ? error.getMessage() : "Error desconocido");

            }
        });

        Volley.newRequestQueue(this).add(postRequest);

    }

    private void enviarWs() {
        String url = "https://tercerbackendgym-production.up.railway.app/api/tipoActividad";

        // Construimos el JSON que enviaremos en la solicitud POST
        JSONObject params = new JSONObject();
        try {
            params.put("nombreTipoActividad", "prueba android con fecha buena");
            params.put("creadorId", 1); // Enviamos el ID como entero
            params.put("fechaCreacionRegistro", obtenerFechaActual());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Creamos la solicitud POST con JsonObjectRequest
        JsonObjectRequest postRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Procesamos la respuesta
                            txtUser.setText(response.getString("nombreTipoActividad"));

                            // Accedemos al objeto "creador" y luego obtenemos el "nombre"
                            JSONObject creador = response.getJSONObject("creador");
                            String nombreCreador = creador.getString("nombre");

                            // Mostramos el nombre del creador en el EditText
                            txtNick.setText(nombreCreador);

                            // Si quieres mostrar el ID del tipo de actividad, sigue como antes:
                            Toast.makeText(MainActivity.this, "ID=" + response.getString("id"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error:", error.getMessage() != null ? error.getMessage() : "Error desconocido");
                    }
                }
        );

        // Añadimos la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(postRequest);
    } */



    private void login(String nick, String password) {
        // URL del backend
        String url = "https://quintobackendgym-production.up.railway.app/login";

        // Crear el objeto LoginDTO para enviar los datos
        JSONObject loginData = new JSONObject();
        try {
            loginData.put("nick", nick);
            loginData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear la solicitud StringRequest con Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Login Response", response);

                if (response.equals("Login exitoso")) {

                    Toast.makeText(MainActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this,MenuActivity.class);


                    intent.putExtra("usuarioNick", nick);
                    startActivity(intent);
                    finish();
                } else if (response.equals("Credenciales incorrectas")) {

                    Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                    // Si el código de estado es 401, las credenciales son incorrectas
                    Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                } else {
                    // Si hay otros errores (por ejemplo, problemas de red)
                    Log.e("Login Error", error.toString());
                    Toast.makeText(MainActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                // Convierte el objeto JSONObject a una cadena en UTF-8 para el cuerpo de la solicitud
                return loginData.toString().getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                // Especificamos que el cuerpo de la solicitud es de tipo JSON
                return "application/json";
            }
        };

        // Agregar la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void salirDeLaApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("¿Estás seguro que deseas salir de la aplicación?")
                .setCancelable(false)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "No se ha cerrado la app", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }






    public String obtenerFechaActual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Atlantic/Canary"));
        return dateFormat.format(new Date());
    }

}
