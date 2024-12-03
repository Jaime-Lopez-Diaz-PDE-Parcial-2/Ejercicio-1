package com.example.ejercicio_1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.ejercicio_1.model.Ubicacion;
import com.example.ejercicio_1.model.Usuario;
import com.example.ejercicio_1.repos.firebase.FirebaseHandler;
import com.example.ejercicio_1.ui.activities.AddActivity;
import com.example.ejercicio_1.ui.activities.CalendarActivity;
import com.example.ejercicio_1.ui.activities.CurrentTasksActivity;
import com.example.ejercicio_1.ui.activities.LoginActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.material.navigation.NavigationView;

public class PantallaPrincipalActivity extends AppCompatActivity {

    private static final int LOCATION_REQUEST_CODE = 1001;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Button btnLogout;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseHandler firebaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        // Inicializar vistas
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        btnLogout = findViewById(R.id.btnLogout);

        // Configurar el ActionBarDrawerToggle
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configurar el listener de navegación
        setupNavigationView();

        // Inicializar Firebase y la ubicación
        firebaseHandler = new FirebaseHandler();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Pedir permisos de ubicación
        checkLocationPermission();

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(PantallaPrincipalActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void setupNavigationView() {
        navigationView.findViewById(R.id.nav_add_activity).setOnClickListener(v -> {
            startActivity(new Intent(this, AddActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        navigationView.findViewById(R.id.nav_calendar).setOnClickListener(v -> {
            startActivity(new Intent(this, CalendarActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        navigationView.findViewById(R.id.nav_current_tasks).setOnClickListener(v -> {
            startActivity(new Intent(this, CurrentTasksActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Manejar el toggle del menú
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permisos
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            // Permisos otorgados, obtener la ubicación
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        // Verificar si los permisos están otorgados
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si los permisos no están otorgados, solicitar permisos
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        // Si los permisos están otorgados, obtener la ubicación
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        updateUserLocation(location);
                    } else {
                        Toast.makeText(this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al obtener ubicación: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateUserLocation(Location location) {
        Ubicacion ubicacionActual = new Ubicacion(location.getLatitude(), location.getLongitude(), "Ubicación actual");

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseHandler.obtenerUsuario(userId, task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                Usuario usuario = task.getResult().toObject(Usuario.class);
                if (usuario != null) {
                    usuario.setUbicacionActual(ubicacionActual);

                    firebaseHandler.guardarUsuario(usuario, updateTask -> {
                        if (updateTask.isSuccessful()) {
                            Toast.makeText(this, "Ubicación actualizada correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error al actualizar la ubicación", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(this, "No se pudo obtener la información del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

