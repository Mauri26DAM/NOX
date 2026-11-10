
package com.example.nox;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button btnOscuridad;
    private Button btnBrujula;
    private Button btnConstelaciones;
    private Button btnDiario;
    private Button btnCazaestrellas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOscuridad = findViewById(R.id.btnOscuridad);
        btnBrujula = findViewById(R.id.btnBrujula);
        btnConstelaciones = findViewById(R.id.btnConstelaciones);
        btnDiario = findViewById(R.id.btnDiario);
        btnCazaestrellas = findViewById(R.id.btnCazaestrellas);

        btnOscuridad.setOnClickListener(v ->
                abrirSeccion("Índice de oscuridad"));

        btnBrujula.setOnClickListener(v ->
                abrirSeccion("Brújula"));

        btnConstelaciones.setOnClickListener(v ->
                abrirSeccion("Constelaciones"));

        btnDiario.setOnClickListener(v ->
                abrirSeccion("Diario de observaciones"));

        btnCazaestrellas.setOnClickListener(v ->
                abrirSeccion("Cazaestrellas"));
    }

    private void abrirSeccion(String nombre) {

        Intent intent = new Intent(
                MainActivity.this,
                SectionActivity.class
        );

        intent.putExtra("nombre_seccion", nombre);

        startActivity(intent);
    }
}
