package com.example.android_gestiondetareas;

import android.content.Intent; // Import necesario para iniciar la actividad
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_gestiondetareas.Data.UsuariosDbHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText nombreEditText, apellidosEditText, emailEditText, contraseñaEditText, confirmarContraseñaEditText;
    private Button registrarButton, volverButton; // Agregamos el botón de "Volver"
    private UsuariosDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar las vistas
        nombreEditText = findViewById(R.id.nombre);
        apellidosEditText = findViewById(R.id.apellidos);
        emailEditText = findViewById(R.id.email);
        contraseñaEditText = findViewById(R.id.contraseña);
        confirmarContraseñaEditText = findViewById(R.id.confirmarContraseña);
        registrarButton = findViewById(R.id.buttonRegistrar);
        volverButton = findViewById(R.id.buttonVolver); // Inicializar el botón de "Volver"

        // Inicializar el DbHelper
        dbHelper = new UsuariosDbHelper(this);

        // Configurar el botón de registro
        registrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        // Configurar el botón de "Volver" para llevar a MainActivity
        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent); // Inicia la actividad principal (MainActivity)
            }
        });
    }

    private void registrarUsuario() {
        // Obtener los valores de los campos
        String nombre = nombreEditText.getText().toString();
        String apellidos = apellidosEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String contraseña = contraseñaEditText.getText().toString();
        String confirmarContraseña = confirmarContraseñaEditText.getText().toString();

        // Validar los campos
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellidos) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(contraseña) || TextUtils.isEmpty(confirmarContraseña)) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que las contraseñas coincidan
        if (!contraseña.equals(confirmarContraseña)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Registrar el usuario en la base de datos
        try {
            long resultado = dbHelper.registrarUsuario(nombre, apellidos, email, contraseña);

            if (resultado != -1) {
                Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                finish(); // Finalizar la actividad
            } else {
                Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteConstraintException e) {
            // Captura el error de restricción única (email duplicado)
            Toast.makeText(this, "El email ya está registrado", Toast.LENGTH_SHORT).show();
        }
    }
}
