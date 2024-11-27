package cl.sandoval.soter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
        setContentView(R.layout.activity_menu_principal);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#FF3488AF")); //
        }

        // Referencias a los elementos del layout
        saludoTextView = findViewById(R.id.saludo);



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
        //SoterAlert Jumper
        ImageView HiddenButt = findViewById(R.id.hiddenbutt);
        HiddenButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menu_principal.this, chat.class));
            }
        });

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
