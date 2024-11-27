package cl.sandoval.soter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private EditText inputUsuario, inputClave;
    private Button botonIniciar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Inicializar las vistas
        inputUsuario = findViewById(R.id.inputUsuario);
        inputClave = findViewById(R.id.inputClave);
        botonIniciar = findViewById(R.id.botonIniciar);

        // Configurar el botón de iniciar sesión
        botonIniciar.setOnClickListener(v -> iniciarSesion());
    }

    // Método para iniciar sesión con correo y contraseña
    private void iniciarSesion() {
        String usuario = inputUsuario.getText().toString().trim();
        String clave = inputClave.getText().toString().trim();

        if (TextUtils.isEmpty(usuario)) {
            inputUsuario.setError("Por favor ingresa tu correo");
            inputUsuario.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(clave)) {
            inputClave.setError("Por favor ingresa tu contraseña");
            inputClave.requestFocus();
            return;
        }

        // Iniciar sesión con Firebase Authentication
        mAuth.signInWithEmailAndPassword(usuario, clave)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Si la autenticación es exitosa, obtener el usuario y su nombre
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String username = getUsernameFromEmail(user.getEmail()); //puto firebase te odio
                            Toast.makeText(login.this, "Bienvenido, " + username, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(login.this, menu_principal.class));
                            finish(); // Cierra
                        }
                    } else {
                        //falla la autenticación
                        Toast.makeText(login.this, "Error de autenticación: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Hack
    private String getUsernameFromEmail(String email) {
        if (email != null) {
            String[] parts = email.split("@");
            if (parts.length > 0) {
                return parts[0]; // Retorna la parte antes del '@'
            }
        }
        return "Usuario desconocido"; // En caso de que no se pueda obtener el nombre
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Cerrar sesión auto
        FirebaseAuth.getInstance().signOut();
    }

}
