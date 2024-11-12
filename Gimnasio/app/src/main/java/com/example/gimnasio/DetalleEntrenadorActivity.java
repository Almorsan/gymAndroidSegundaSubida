package com.example.gimnasio;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DetalleEntrenadorActivity extends AppCompatActivity {
    private Spinner spinnerClientes, spinnerEntrenadoresCreados, spinnerSalas;
    private String entrenadorNombre;
    private RequestQueue requestQueue;  // Asegúrate de declarar el requestQueue

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_entrenador);

        // Inicializar el requestQueue
        requestQueue = Volley.newRequestQueue(this);  // Esto inicializa el requestQueue

        // Obtener el nombre del entrenador desde el Intent
        entrenadorNombre = getIntent().getStringExtra("entrenador_nombre");

        // Inicializar los Spinners
        spinnerClientes = findViewById(R.id.spinnerClientes);
        spinnerEntrenadoresCreados = findViewById(R.id.spinnerEntrenadoresCreados);
        spinnerSalas = findViewById(R.id.spinnerSalas);

        // Llamar a un método para cargar la información del entrenador
        fetchEntrenadorDetails(entrenadorNombre);
    }

    private void fetchEntrenadorDetails(String entrenadorNombre) {
        String url = "https://tu_api_endpoint.com/entrenador/details?nombre=" + entrenadorNombre;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject entrenadorDetails = new JSONObject(response);

                            // Cargar los clientes
                            JSONArray clientes = entrenadorDetails.getJSONArray("clientes");
                            List<String> clientesList = new ArrayList<>();
                            for (int i = 0; i < clientes.length(); i++) {
                                JSONObject cliente = clientes.getJSONObject(i);
                                clientesList.add(cliente.getString("nombre"));
                            }
                            ArrayAdapter<String> clientesAdapter = new ArrayAdapter<>(DetalleEntrenadorActivity.this,
                                    android.R.layout.simple_spinner_item, clientesList);
                            spinnerClientes.setAdapter(clientesAdapter);

                            // Cargar los entrenadores creados
                            JSONArray entrenadoresCreados = entrenadorDetails.getJSONArray("entrenadoresCreados");
                            List<String> entrenadoresCreadosList = new ArrayList<>();
                            for (int i = 0; i < entrenadoresCreados.length(); i++) {
                                JSONObject entrenador = entrenadoresCreados.getJSONObject(i);
                                entrenadoresCreadosList.add(entrenador.getString("nombre"));
                            }
                            ArrayAdapter<String> entrenadoresCreadosAdapter = new ArrayAdapter<>(DetalleEntrenadorActivity.this,
                                    android.R.layout.simple_spinner_item, entrenadoresCreadosList);
                            spinnerEntrenadoresCreados.setAdapter(entrenadoresCreadosAdapter);

                            // Cargar las salas
                            JSONArray salas = entrenadorDetails.getJSONArray("salas");
                            List<String> salasList = new ArrayList<>();
                            for (int i = 0; i < salas.length(); i++) {
                                JSONObject sala = salas.getJSONObject(i);
                                salasList.add(sala.getString("nombre"));
                            }
                            ArrayAdapter<String> salasAdapter = new ArrayAdapter<>(DetalleEntrenadorActivity.this,
                                    android.R.layout.simple_spinner_item, salasList);
                            spinnerSalas.setAdapter(salasAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetalleEntrenadorActivity.this, "Error cargando datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetalleEntrenadorActivity.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
                    }
                });

        // Añadir la solicitud a la cola de Volley
        requestQueue.add(request);  // Aquí usamos requestQueue que hemos inicializado previamente
    }
}

