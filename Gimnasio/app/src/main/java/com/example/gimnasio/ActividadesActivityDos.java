package com.example.gimnasio;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ActividadesActivityDos extends AppCompatActivity {

    EditText txtUsuario, txtPassword, txtBody;
    Button btnCerrarSesion,btnExit;

    String nombreCreador, nickCreador,nombreUser,nickUser, fechaUltimaConexionUser,
            fechaNacimientoUser,fechaAltaUser, direccionEstablecimiento;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades_dos);


        String usuarioNick = getIntent().getStringExtra("usuarioNick");


        if (usuarioNick != null) {
            Toast.makeText(ActividadesActivityDos.this, "Bienvenido, " + usuarioNick, Toast.LENGTH_SHORT).show();

        }

        encontrarUsuario(usuarioNick, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject usuario) {
                try {
                    // Aquí tienes acceso al objeto JSON `usuario`
                    String nombre = usuario.getString("nombre");
                    String nick = usuario.getString("nick");
                    JSONObject creador = usuario.getJSONObject("creador");
                    JSONObject establecimiento=usuario.getJSONObject("establecimiento");
                    JSONArray entrenadoresCreados = usuario.getJSONArray("entrenadoresCreados");
                    JSONArray clientes =usuario.getJSONArray("clientes");
                    JSONArray salas=usuario.getJSONArray("salas");

                     nombreCreador = creador.getString("nombre");
                     nickCreador = creador.getString("nick");

                    nombreUser=usuario.getString("nombre");
                    nickUser=usuario.getString("nick");
                    fechaUltimaConexionUser=usuario.getString("fechaUltimoLogin");
                    fechaNacimientoUser=usuario.getString("fechaNacimiento");
                    fechaAltaUser=usuario.getString("fechaAlta");

                    direccionEstablecimiento=establecimiento.getString("direccion");


                    String nombreUser = usuario.getString("nombre");
                    String nickUser = usuario.getString("nick");

                    // Acceder al NavigationView y al menú
                    NavigationView navigationView = findViewById(R.id.nav_view);
                    Menu menu = navigationView.getMenu();

                    // Actualizar el título de los items con el nombre y el nick
                    MenuItem nameItem = menu.findItem(R.id.nav_user_name);
                    MenuItem nickItem = menu.findItem(R.id.nav_user_nick);

                    nameItem.setTitle("Nombre: " + nombreUser);
                    nickItem.setTitle("Nick: " + nickUser);


                    MenuItem itemEntrenadores = menu.findItem(R.id.nav_trainers);
                    SubMenu subMenu = itemEntrenadores.getSubMenu();
                    //TextView title1 = (TextView) itemEntrenadores.getActionView();
                    //title1.setTypeface(null, Typeface.BOLD);

                    subMenu.clear();

                    for (int i = 0; i < entrenadoresCreados.length(); i++) {
                        JSONObject entrenador = entrenadoresCreados.getJSONObject(i);
                        String nombreEntrenador = entrenador.getString("nombre");

                        // Crear un nuevo MenuItem para cada entrenador
                        subMenu.add(Menu.NONE, i, Menu.NONE, nombreEntrenador);
                    }

                    MenuItem itemClientes=menu.findItem(R.id.nav_clientes);
                    SubMenu subMenu2=itemClientes.getSubMenu();
                    //TextView title2 = (TextView) itemClientes.getActionView();
                   // title2.setTypeface(null, Typeface.BOLD);

                    subMenu2.clear();

                    for (int i = 0; i < clientes.length(); i++) {
                        JSONObject cliente = clientes.getJSONObject(i);
                        String nombreCliente = cliente.getString("nombre");

                        // Crear un nuevo MenuItem para cada cliente
                        subMenu2.add(Menu.NONE, i, Menu.NONE, nombreCliente);
                    }


                    MenuItem itemSalas=menu.findItem(R.id.nav_salas);
                    SubMenu subMenu3=itemSalas.getSubMenu();
                    //TextView title3 = (TextView) itemSalas.getActionView();
                    //title3.setTypeface(null, Typeface.BOLD);

                    subMenu3.clear();

                    for (int i = 0; i < salas.length(); i++) {
                        JSONObject sala = salas.getJSONObject(i);
                        String nombreSala = sala.getString("nombre");

                        // Crear un nuevo MenuItem para cada sala
                        subMenu3.add(Menu.NONE, i, Menu.NONE, nombreSala);
                    }


                    Log.d("Usuario", "Nombre: " + nombre + ", Nick: " + nick);
                    Log.d("Creador","Creador: "+creador.toString());
                    Log.d("Entrenadores creados","Entrenadores creados: "+entrenadoresCreados.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Log.e("Error en encontrarUsuario:", error);
            }
        });

        //////////////////////

        btnCerrarSesion=findViewById(R.id.btnCerrarSesion);
        btnExit=findViewById(R.id.btnSalir);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        // Abrir y cerrar el menú lateral con el icono
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Configurar las opciones del menú
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    // Acción para "Inicio"
                    mostrarInfoPropia();
                    //Toast.makeText(MenuActivity.this, "Inicio seleccionado", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_settings) {
                    // Acción para "Configuraciones"
                    Toast.makeText(ActividadesActivityDos.this, "Configuraciones seleccionadas", Toast.LENGTH_SHORT).show();
                }  else if (id == R.id.nav_creator_info) {
                // Mostrar el AlertDialog con la información del creador
                mostrarInfoCreador();
            } else if (id == R.id.nav_trainers) {
                    // Este ítem no es seleccionable directamente, pero el SubMenu de entrenadores sí lo es
                    // Los entrenadores agregados dinámicamente tendrán su ID como el índice
                    int index = item.getItemId();
                    Log.d("Entrenador seleccionado", "Entrenador: " + index);
                } else if (id == R.id.nav_clientes) {

                    int index = item.getItemId();
                    Log.d("Cliente seleccionado", "Cliente: " + index);
                } else if (id == R.id.nav_salas) {

                    int index = item.getItemId();
                    Log.d("Sala seleccionada", "Sala: " + index);
                } else if (id == R.id.nav_establecimiento) {

                    mostrarInfoEstablecimiento();
                }
                // Agregar más condiciones según los elementos del menú

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

        });



        //////////////////////////////////////////////





        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salirDeLaApp();
            }
        });


        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });






    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
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





    private void salirDeLaApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActividadesActivityDos.this);
        builder.setMessage("¿Estás seguro que deseas salir de la aplicación?")
                .setCancelable(false) // Evita que se cierre tocando fuera del diálogo
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Si el usuario selecciona "Sí", cerrar la app
                        finish(); // Esto cierra la actividad y la app
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Si el usuario selecciona "No", solo se cierra el diálogo
                        dialog.dismiss();
                        Toast.makeText(ActividadesActivityDos.this, "No se ha cerrado la app", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void cerrarSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActividadesActivityDos.this);
        builder.setMessage("¿Estás seguro que deseas cerrar sesión?")
                .setCancelable(false) // Evita que se cierre tocando fuera del diálogo
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ActividadesActivityDos.this, MainActivity.class);
                        startActivity(intent);

                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        Toast.makeText(ActividadesActivityDos.this, "No se ha cerrado sesión", Toast.LENGTH_SHORT).show();
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

    private void encontrarUsuario(String nick, final VolleyCallback callback) {
        String url = "https://quintobackendgym-production.up.railway.app/entrenadores/api/" + nick;

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parseamos la respuesta JSON
                            JSONObject usuario = new JSONObject(response);

                            // Llamamos al método onSuccess del callback con el JSON recibido
                            callback.onSuccess(usuario);

                        } catch (JSONException e) {
                            // En caso de error de parseo JSON, llamamos a onError del callback
                            callback.onError("Error de parseo JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Llamamos a onError del callback con el mensaje de error
                        callback.onError(error.getMessage() != null ? error.getMessage() : "Error desconocido");
                    }
                });

        // Añadimos la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void mostrarInfoCreador() {


        // Crear y mostrar el AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("Información del Entrenador Creador")
                .setMessage("Nombre: " + nombreCreador + "\nNick: " + nickCreador)
                .setPositiveButton("Cerrar", null)
                .show();
    }

    private void mostrarInfoPropia() {



        new AlertDialog.Builder(this)
                .setTitle("Mis datos")
                .setMessage("Nombre: " + nombreUser + "\nNick: " + nickUser + "\nFecha última conexión: " + fechaUltimaConexionUser
                        + "\nFecha nacimiento: "+fechaNacimientoUser + "\nFecha alta: "+fechaAltaUser)
                .setPositiveButton("Cerrar", null)
                .show();
    }


    private void mostrarInfoEstablecimiento() {



        new AlertDialog.Builder(this)
                .setTitle("Establecimiento asignado")
                .setMessage("Dirección: " + direccionEstablecimiento)
                .setPositiveButton("Cerrar", null)
                .show();
    }


}
