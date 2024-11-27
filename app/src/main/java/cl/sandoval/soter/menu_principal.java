package cl.sandoval.soter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class menu_principal extends AppCompatActivity {
    private TextView saludoTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);
        // Referencias a los elementos del layout
        saludoTextView = findViewById(R.id.saludo);

        // Configuración del saludo personalizado según la hora
        setSaludoPersonalizado();


        // CamaraJumper
        ImageButton CamaraJumper = findViewById(R.id.camerajump);
        CamaraJumper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu_principal.this, camaras.class));
            }
        });

        //SoterAlert Jumper
        ImageView SoterJumper = findViewById(R.id.soteralertbutt);
        SoterJumper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu_principal.this, alertasend.class));
            }
        });

        //Inicio Jumper
//        ImageView InicioJumper = findViewById(R.id.iniciojumpbutt);
//        InicioJumper.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(menu_principal.this, camaras.class));
//            }
//        });


    }
    private void setSaludoPersonalizado() {
        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);

        String saludo;
        if (hora >= 6 && hora < 12) {
            saludo = "¡Buenos días!";
        } else if (hora >= 12 && hora < 18) {
            saludo = "¡Buenas tardes!";
        } else {
            saludo = "¡Buenas noches!";
        }
        saludoTextView.setText(saludo);
    }
}
