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

public class EntrenadoresActivity extends AppCompatActivity {

    EditText txtUsuario, txtPassword, txtBody;
    Button btnCerrarSesion,btnExit, btnVolver, btnAnadirEntrenador, btnModifcarEntrenador,
    btnConsultarEntrenador,btnBorrarEntrenador,btnListarEntrenador;

    private JSONArray entrenadoresArray;

    String nombreCreador, nickCreador,nombreUser,nickUser, fechaUltimaConexionUser,
            fechaNacimientoUser,fechaAltaUser, direccionEstablecimiento;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private Spinner spinnerEntrenadores;
    private RequestQueue requestQueue;

   // private List<String> entrenadoresNombres = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenadores);


        String usuarioNick = getIntent().getStringExtra("usuarioNick");
        //nickUser= getIntent().getStringExtra("usuarioNick");;


        if (usuarioNick != null) {
            Toast.makeText(EntrenadoresActivity.this, "Bienvenido, " + usuarioNick, Toast.LENGTH_SHORT).show();

        }

        requestQueue = Volley.newRequestQueue(this);

        ejecutarEncontrarUsuario ();

        //////////////////////

        btnCerrarSesion=findViewById(R.id.btnCerrarSesion);
        btnExit=findViewById(R.id.btnSalir);
        btnVolver=findViewById(R.id.btnVolver);
        btnAnadirEntrenador=findViewById(R.id.btnAnadirEntrenador);
        btnBorrarEntrenador=findViewById(R.id.btnBorrarEntrenador);
        btnConsultarEntrenador=findViewById(R.id.btnConsultarEntrnenador);
        btnListarEntrenador=findViewById(R.id.btnListarEntrenadores);
        btnModifcarEntrenador=findViewById(R.id.btnModificarEntrenador);

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
                    Toast.makeText(EntrenadoresActivity.this, "Configuraciones seleccionadas", Toast.LENGTH_SHORT).show();
                }  else if (id == R.id.nav_creator_info) {
                // Mostrar el AlertDialog con la información del creador
                mostrarInfoCreador();
            } else if (id == R.id.nav_trainers) {

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


        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(EntrenadoresActivity.this, MenuActivity.class);
                startActivity(intent);

                intent.putExtra("usuarioNick", nickUser);
                startActivity(intent);
                finish();
                ;
            }
        });

        btnAnadirEntrenador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogoRegistro();
            }
        });

        btnListarEntrenador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSpinnerDialog();


            }
        });

        btnBorrarEntrenador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showDeleteEntrenadorDialog();

            }
        });

        btnConsultarEntrenador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //showEntrenadorDialog();

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




    private void salirDeLaApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EntrenadoresActivity.this);
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
                        Toast.makeText(EntrenadoresActivity.this, "No se ha cerrado la app", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void cerrarSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EntrenadoresActivity.this);
        builder.setMessage("¿Estás seguro que deseas cerrar sesión?")
                .setCancelable(false)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EntrenadoresActivity.this, MainActivity.class);
                        startActivity(intent);

                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        Toast.makeText(EntrenadoresActivity.this, "No se ha cerrado sesión", Toast.LENGTH_SHORT).show();
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

                            JSONObject usuario = new JSONObject(response);


                            callback.onSuccess(usuario);

                        } catch (JSONException e) {

                            callback.onError("Error de parseo JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

    private void dialogoRegistro() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añada un entrenador a a la base de datos");


        View dialogView = getLayoutInflater().inflate(R.layout.crear_entrenador, null);
        final EditText editText1 = dialogView.findViewById(R.id.editText1);
        final EditText editText2 = dialogView.findViewById(R.id.editText2);
        final EditText editText3 = dialogView.findViewById(R.id.editText3);
        final EditText editText4 = dialogView.findViewById(R.id.editText4);
        final DatePicker datePicker =dialogView.findViewById(R.id.datePicker);
        final CheckBox checkBox=dialogView.findViewById(R.id.checkbox_terms);
        builder.setView(dialogView);


        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre = editText1.getText().toString();
                String nick = editText2.getText().toString();
                String pass1 = editText3.getText().toString();
                String pass2=editText4.getText().toString();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1; // Los meses empiezan en 0, por lo que sumamos 1
                int year = datePicker.getYear();
                String fechaNacimiento = year + "-" + month + "-" + day;
                boolean esAdmin = checkBox.isChecked();

                boolean registro=verificarRegistro(nombre,nick,pass1,pass2);

                if (registro==true) {
                    registrarUsuario(nombre,nick,pass1,fechaNacimiento,esAdmin);
                }





            }
        });


        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean verificarRegistro(String nombre, String nick, String pass, String pass2){
        if(nombre.length()>0&&nick.length()>0&&pass.length()>0&&pass2.length()>0) {

            if(!pass.equals(pass2)) {
                Toast.makeText(EntrenadoresActivity.this, "Error, las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }

        } else {
            Toast.makeText(EntrenadoresActivity.this, "Error, rellene todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return false;

        }




    }

    public void registrarUsuario(String nombre, String nick, String pass, String fechaNacimiento, boolean esAdmin) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre", nombre);
            jsonObject.put("nick", nick);
            jsonObject.put("contraseña", pass);
            jsonObject.put("fechaNacimiento", fechaNacimiento);
            jsonObject.put("esAdmin", esAdmin);
            jsonObject.put("foto", "url_foto.jpg");
            jsonObject.put("fechaAlta", obtenerFechaActual());
            jsonObject.put("fechaUltimoLogin", "2024-11-08T10:00:00");
            jsonObject.put("establecimientoId", 3);
            jsonObject.put("creadorId", 3);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        String url = "https://quintobackendgym-production.up.railway.app/api/entrenadores";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    Toast.makeText(EntrenadoresActivity.this, "Agregado exitosamente a la base de datos", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(EntrenadoresActivity.this, "Error, no se ha podido añadir el entrenador", Toast.LENGTH_SHORT).show();
                });

        // Añadir la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonRequest);

        ejecutarEncontrarUsuario();



    }

    private void showSpinnerDialog() {
        // Infla el layout del diálogo con el Spinner
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.spinner_dialog, null);

        // Encuentra el Spinner en el layout del diálogo
        spinnerEntrenadores = dialogView.findViewById(R.id.spinnerEntrenadoresDialog);

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona un Entrenador")
                .setView(dialogView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Obtener el elemento seleccionado
                        String seleccionado = spinnerEntrenadores.getSelectedItem().toString();
                        Toast.makeText(EntrenadoresActivity.this, "Seleccionaste: " + seleccionado, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        builder.create().show();

        // Realiza la solicitud POST para obtener la lista de entrenadores
        fetchEntrenadores();
    }


    private void fetchEntrenadores() {
        String url = "https://quintobackendgym-production.up.railway.app/entrenadores/all"; // Cambia esto a tu URL de API


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Procesar la respuesta y actualizar el Spinner
                        List<String> entrenadoresNombres = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject entrenador = response.getJSONObject(i);
                                String nombre = entrenador.getString("nombre");
                                String nick = entrenador.getString("nick");
                                entrenadoresNombres.add(nombre + " (" + nick + ")");
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(EntrenadoresActivity.this,
                                    android.R.layout.simple_spinner_item, entrenadoresNombres);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerEntrenadores.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EntrenadoresActivity.this, "Error procesando datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EntrenadoresActivity.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        requestQueue.add(jsonArrayRequest);
    }


/*
    private void fetchEntrenadores() {
        String url = "https://quintobackendgym-production.up.railway.app/entrenadores/all"; // Cambia esto a tu URL de API

        // Crear una solicitud de tipo JSONArrayRequest
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,  // Cambia a GET si es necesario
                url,
                null,
                response -> {
                    entrenadoresNombres.clear();  // Limpiar la lista antes de agregar nuevos datos
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject entrenador = response.getJSONObject(i);
                            String nombre = entrenador.getString("nombre");
                            String nick = entrenador.getString("nick");
                            entrenadoresNombres.add(nombre + " (" + nick + ")");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(EntrenadoresActivity.this, "Error procesando datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(EntrenadoresActivity.this, "Error en la solicitud", Toast.LENGTH_SHORT).show()
        );

        // Agregar la solicitud a la cola de Volley
        requestQueue.add(jsonArrayRequest);
    }

*/
    private void showDeleteEntrenadorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EntrenadoresActivity.this);
        builder.setTitle("Eliminar Entrenador");

        // Crear un Spinner dentro del AlertDialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_delete_entrenador, null);
        Spinner spinnerEntrenadores = dialogView.findViewById(R.id.spinnerEntrenadores);


        List<String> entrenadoresNombres = new ArrayList<>();

        fetchEntrenadores(entrenadoresNombres, spinnerEntrenadores);

        builder.setView(dialogView);


        builder.setPositiveButton("Eliminar", null); // Configuraremos el onClickListener después


        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();


        dialog.setOnShowListener(d -> {
            Button deleteButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            deleteButton.setOnClickListener(v -> {
                String selectedEntrenador = (String) spinnerEntrenadores.getSelectedItem();
                if (selectedEntrenador != null) {

                    new AlertDialog.Builder(EntrenadoresActivity.this)
                            .setTitle("Confirmar Eliminación")
                            .setMessage("¿Estás seguro de que deseas eliminar a " + selectedEntrenador + "?")
                            .setPositiveButton("Sí", (confirmDialog, confirmWhich) -> {

                                deleteEntrenador(selectedEntrenador);
                                dialog.dismiss();
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    Toast.makeText(EntrenadoresActivity.this, "Selecciona un entrenador", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }




    private void deleteEntrenador(String entrenador) {
        String entrenadorId = obtenerIdDeEntrenador(entrenador); // Implementar lógica para obtener ID
        String url = "https://quintobackendgym-production.up.railway.app/entrenadores/" + entrenadorId;

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                response -> Toast.makeText(EntrenadoresActivity.this, "Entrenador eliminado correctamente", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(EntrenadoresActivity.this, "Error eliminando el entrenador", Toast.LENGTH_SHORT).show());

        requestQueue.add(deleteRequest);
    }

    // Método para cargar la lista de entrenadores en el Spinner
    private void fetchEntrenadores(List<String> entrenadoresNombres, Spinner spinnerEntrenadores) {
        String url = "https://quintobackendgym-production.up.railway.app/entrenadores/all";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        entrenadoresNombres.clear();
                        entrenadoresArray = response; // Guardamos el JSON de entrenadores

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject entrenador = response.getJSONObject(i);
                            String nombre = entrenador.getString("nombre");
                            String nick = entrenador.getString("nick");
                            entrenadoresNombres.add(nombre + " (" + nick + ")");
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(EntrenadoresActivity.this,
                                android.R.layout.simple_spinner_item, entrenadoresNombres);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerEntrenadores.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(EntrenadoresActivity.this, "Error obteniendo entrenadores", Toast.LENGTH_SHORT).show());

        requestQueue.add(jsonArrayRequest);
    }

// Método para obtener el ID del entrenador seleccionado

   private String obtenerIdDeEntrenador(String entrenadorSeleccionado) {
        if (entrenadoresArray != null) {
            try {
                for (int i = 0; i < entrenadoresArray.length(); i++) {
                    JSONObject entrenador = entrenadoresArray.getJSONObject(i);
                    String nombre = entrenador.getString("nombre");
                    String nick = entrenador.getString("nick");
                    String nombreConNick = nombre + " (" + nick + ")";

                    // Si el entrenador seleccionado coincide, devuelve el ID
                    if (nombreConNick.equals(entrenadorSeleccionado)) {
                        return entrenador.getString("id"); // Devuelve el ID como String
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null; // Si no se encuentra, devuelve null
    }




void ejecutarEncontrarUsuario (){

        requestQueue = Volley.newRequestQueue(this);

        encontrarUsuario(nickUser, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject usuario) {
                try {

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
    }


}
