package cl.sandoval.soter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class login extends AppCompatActivity implements FingerprintDialogFragment.OnFingerprintChoiceListener {

    private EditText inputUsuario, inputClave;
    private Button botonIniciar;
    private Button botonReset;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    // BiometricPrompt variables
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FF3488AF")); //
        }

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Inicializar las vistas
        inputUsuario = findViewById(R.id.inputUsuario);
        inputClave = findViewById(R.id.inputClave);
        botonIniciar = findViewById(R.id.botonIniciar);
        botonReset = findViewById(R.id.botonNoeres);

        // Inicializar SharedPreferences para verificar si es la primera vez
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Verificar si el usuario eligió huella dactilar anteriormente
        boolean useFingerprint = sharedPreferences.getBoolean("useFingerprint", false);
        if (useFingerprint) {
            // Ocultar los campos de usuario y contraseña, y el botón de iniciar sesión
            inputUsuario.setVisibility(EditText.GONE);
            inputClave.setVisibility(EditText.GONE);
            botonIniciar.setVisibility(Button.GONE);
            botonReset.setVisibility(View.VISIBLE);

            // Esperar 1 segundos y luego intentar la autenticación biométrica
            new Handler().postDelayed(() -> biometricPrompt.authenticate(promptInfo), 1000);
        } else {
            // Si no, mostrar los campos de texto y el botón de inicio de sesión
            inputUsuario.setVisibility(EditText.VISIBLE);
            inputClave.setVisibility(EditText.VISIBLE);
            botonIniciar.setVisibility(Button.VISIBLE);
            botonReset.setVisibility(View.GONE);

            // Configurar el botón de iniciar sesión
            botonIniciar.setOnClickListener(v -> iniciarSesion());
        }

        // Configuración del BiometricPrompt
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(login.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Autenticación exitosa
                Toast.makeText(login.this, "Autenticación exitosa!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(login.this, menu_principal.class));
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(login.this, "Autenticación fallida", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(login.this, "Error de autenticación: " + errString, Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación biométrica")
                .setSubtitle("Usa tu huella dactilar para iniciar sesión")
                .setNegativeButtonText("Cancelar")
                .build();
    }

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
                        // Si la autenticación es exitosa
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String username = getUsernameFromEmail(user.getEmail());

                            // Guardar el username en SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", username);
                            editor.apply();

                            Toast.makeText(login.this, "Bienvenido, " + username, Toast.LENGTH_SHORT).show();

                            boolean isFirstLogin = sharedPreferences.getBoolean("isFirstLogin", true);
                            if (isFirstLogin) {
                                FingerprintDialogFragment dialog = new FingerprintDialogFragment();
                                dialog.show(getSupportFragmentManager(), "FingerprintDialog");
                            }
                        }
                    } else {
                        Toast.makeText(login.this, "Error de autenticación: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    @Override
    public void onFingerprintEnabled(boolean enabled) {
        if (enabled) {
            // El usuario quiere usar la huella, guardar la preferencia y proceder con la autenticación biométrica
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("useFingerprint", true);
            editor.apply();

            // Iniciar la autenticación biométrica
            biometricPrompt.authenticate(promptInfo);
        } else {
            // Si el usuario no quiere usar la huella, proceder normalmente
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("useFingerprint", false);
            editor.apply();
            startActivity(new Intent(login.this, menu_principal.class));
            finish();
        }
    }

    // Hack Username
    private String getUsernameFromEmail(String email) {
        if (email != null) {
            String[] parts = email.split("@");
            if (parts.length > 0) {
                return parts[0]; // Retorna la parte antes del '@'
            }
        }
        return "Usuario desconocido"; //
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Autologout
        FirebaseAuth.getInstance().signOut();
    }
}
