package com.example.android_gestiondetareas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android_gestiondetareas.Data.UsuariosDbHelper;

public class MainActivity extends AppCompatActivity {

    private Button loginButton, registerButton;
    private EditText emailEditText, passwordEditText;
    private UsuariosDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        loginButton = findViewById(R.id.buttonIniciarSesion);
        registerButton = findViewById(R.id.buttonRegistrarse);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);

        // Inicializar el DbHelper
        dbHelper = new UsuariosDbHelper(this);

        // Listener para el botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String contraseña = passwordEditText.getText().toString();

                // Validar que no haya campos vacíos
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(contraseña)) {
                    Toast.makeText(MainActivity.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar si hay usuarios registrados
                if (dbHelper.hayUsuariosRegistrados()) {
                    // Verificar si las credenciales son correctas
                    if (dbHelper.loginUsuario(email, contraseña)) {
                        // Obtener el nombre del usuario
                        String nombreUsuario = dbHelper.obtenerNombreDeUsuario(email);

                        // Navegar a PrincipalActivity con el nombre del usuario
                        Intent loginIntent = new Intent(MainActivity.this, PrincipalActivity.class);
                        loginIntent.putExtra("nombreUsuario", nombreUsuario);
                        startActivity(loginIntent);
                    } else {
                        Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No hay usuarios registrados. Por favor regístrate.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listener para el botón de registro
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}
