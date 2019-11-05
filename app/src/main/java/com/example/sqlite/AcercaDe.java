package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AcercaDe extends AppCompatActivity {

    TextView tvNombre, tvCarnet, tvEdad,tvCarre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        tvNombre = findViewById(R.id.tvnombre);
        tvCarnet = findViewById(R.id.tvcarnet);
        tvEdad = findViewById(R.id.tvedad);
        tvCarre= findViewById(R.id.tvcarre);
        informacion();
    }

    public void informacion (){

        tvNombre.setText("Brenda Yamileth Rivas Mejia");
        tvCarnet.setText("144018");
        tvEdad.setText("20 años");
        tvCarre.setText("Ingenieria en sistema informaticos");

    }
}
