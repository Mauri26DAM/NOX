
package com.example.nox;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SectionActivity extends Activity
        implements SensorEventListener {

    private LinearLayout contenido;
    private TextView resultado;
    private SensorManager sensorManager;
    private Sensor sensor;
    private String seccion;

    private int puntuacion = 0;
    private long ultimoPunto = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        seccion = getIntent().getStringExtra("nombre_seccion");

        ScrollView scroll = new ScrollView(this);

        contenido = new LinearLayout(this);
        contenido.setOrientation(LinearLayout.VERTICAL);
        contenido.setGravity(Gravity.CENTER);
        contenido.setPadding(24, 35, 24, 35);
        contenido.setBackgroundColor(0xFF101318);

        scroll.addView(contenido);
        setContentView(scroll);

        if (seccion == null) {
            seccion = "";
        }

        if (seccion.equals("Índice de oscuridad")) {
            mostrarOscuridad();
        } else if (seccion.equals("Brújula")) {
            mostrarBrujula();
        } else if (seccion.equals("Constelaciones")) {
            mostrarConstelaciones();
        } else if (seccion.equals("Diario de observaciones")) {
            mostrarDiario();
        } else if (seccion.equals("Cazaestrellas")) {
            mostrarCazaestrellas();
        }

        Button volver = crearBoton("VOLVER AL INICIO");
        volver.setOnClickListener(v -> finish());
    }

    private TextView crearTexto(String texto, int tamano) {

        TextView tv = new TextView(this);

        tv.setText(texto);
        tv.setTextColor(0xFFFFFFFF);
        tv.setTextSize(tamano);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(10, 15, 10, 15);

        contenido.addView(tv);

        return tv;
    }

    private Button crearBoton(String texto) {

        Button boton = new Button(this);
        boton.setText(texto);

        contenido.addView(boton);

        return boton;
    }

    private void prepararSensor(int tipo) {

        sensorManager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);

        sensor = sensorManager.getDefaultSensor(tipo);

        if (sensor == null) {
            resultado.setText(
                    "Este dispositivo no dispone de este sensor."
            );
        }
    }

    // INDICE DE OSCURIDAD

    private void mostrarOscuridad() {

        crearTexto("ÍNDICE DE OSCURIDAD", 26);

        crearTexto(
                "Medición de la iluminación ambiental",
                16
        );

        resultado = crearTexto(
                "Esperando lectura del sensor...",
                21
        );

        prepararSensor(Sensor.TYPE_LIGHT);
    }

    // BRUJULA

    private void mostrarBrujula() {

        crearTexto("BRÚJULA", 28);

        crearTexto(
                "Orientación del dispositivo",
                16
        );

        resultado = crearTexto(
                "Buscando orientación...",
                25
        );

        prepararSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    // CATALOGO DE CONSTELACIONES

    private void mostrarConstelaciones() {

        crearTexto("CATÁLOGO DE CONSTELACIONES", 24);

        crearTexto(
                "Selecciona una constelación para conocerla.",
                16
        );

        String[] nombres = {
                "Orión",
                "Osa Mayor",
                "Osa Menor",
                "Casiopea",
                "Escorpio",
                "Leo"
        };

        String[] descripciones = {

                "Orión es una de las constelaciones más conocidas. "
                        + "Destaca por las tres estrellas de su cinturón.",

                "La Osa Mayor es conocida por el asterismo "
                        + "del Carro y permite localizar la estrella Polar.",

                "La Osa Menor contiene la estrella Polar, "
                        + "que señala aproximadamente el norte celeste.",

                "Casiopea tiene una forma característica "
                        + "de W o M según su posición.",

                "Escorpio representa un escorpión y destaca "
                        + "por su estrella rojiza Antares.",

                "Leo representa un león y contiene "
                        + "la brillante estrella Régulo."
        };

        resultado = crearTexto(
                "Aquí aparecerá la información.",
                17
        );

        for (int i = 0; i < nombres.length; i++) {

            final int posicion = i;

            Button boton = crearBoton(nombres[i]);

            boton.setOnClickListener(v -> {

                resultado.setText(
                        nombres[posicion] + "\n\n"
                                + descripciones[posicion]
                );
            });
        }
    }

    // DIARIO DE OBSERVACIONES

    private void mostrarDiario() {

        crearTexto("DIARIO DE OBSERVACIONES", 23);

        EditText campo = new EditText(this);

        campo.setHint("Escribe tu observación...");
        campo.setTextColor(0xFFFFFFFF);
        campo.setHintTextColor(0xFFAAAAAA);
        campo.setMinLines(3);
        campo.setGravity(Gravity.TOP);

        contenido.addView(campo);

        resultado = crearTexto("", 16);

        Button guardar = crearBoton("GUARDAR OBSERVACIÓN");

        guardar.setOnClickListener(v -> {

            String texto = campo.getText().toString().trim();

            if (texto.isEmpty()) {

                Toast.makeText(
                        this,
                        "Escribe una observación",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            SharedPreferences preferencias =
                    getSharedPreferences("NOX_DIARIO", MODE_PRIVATE);

            String anteriores = preferencias.getString(
                    "observaciones", ""
            );

            String fecha = new SimpleDateFormat(
                    "dd/MM/yyyy HH:mm",
                    Locale.getDefault()
            ).format(new Date());

            String nueva = fecha + "\n" + texto;

            String contenidoNuevo = anteriores.isEmpty()
                    ? nueva
                    : anteriores + "\n\n" + nueva;

            preferencias.edit()
                    .putString("observaciones", contenidoNuevo)
                    .apply();

            campo.setText("");

            Toast.makeText(
                    this,
                    "Observación guardada",
                    Toast.LENGTH_SHORT
            ).show();
        });

        Button consultar = crearBoton("VER OBSERVACIONES");

        consultar.setOnClickListener(v -> {

            SharedPreferences preferencias =
                    getSharedPreferences("NOX_DIARIO", MODE_PRIVATE);

            String datos = preferencias.getString(
                    "observaciones",
                    "Todavía no hay observaciones guardadas."
            );

            resultado.setText(datos);
        });
    }

    // CAZAESTRELLAS

    private void mostrarCazaestrellas() {

        crearTexto("CAZAESTRELLAS", 29);

        crearTexto(
                "Inclina el móvil para conseguir estrellas.",
                17
        );

        puntuacion = 0;

        resultado = crearTexto(
                "Estrellas: 0",
                26
        );

        crearTexto(
                "Cada movimiento válido suma un punto.",
                15
        );

        prepararSensor(Sensor.TYPE_ACCELEROMETER);
    }

    // LECTURA DE SENSORES

    @Override
    protected void onResume() {

        super.onResume();

        if (sensorManager != null && sensor != null) {

            sensorManager.registerListener(
                    this,
                    sensor,
                    SensorManager.SENSOR_DELAY_UI
            );
        }
    }

    @Override
    protected void onPause() {

        super.onPause();

        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent evento) {

        if (resultado == null) {
            return;
        }

        // LECTURA DE LUZ AMBIENTAL

        if (seccion.equals("Índice de oscuridad")) {

            float luz = evento.values[0];

            String nivel;

            if (luz < 10) {
                nivel = "Muy poca iluminación";
            } else if (luz < 100) {
                nivel = "Iluminación baja";
            } else if (luz < 500) {
                nivel = "Iluminación moderada";
            } else {
                nivel = "Mucha iluminación";
            }

            resultado.setText(
                    "Luz ambiental: " + luz
                            + " lux\n\n" + nivel
            );
        }

        // LECTURA DE BRUJULA

        else if (seccion.equals("Brújula")) {

            float[] matriz = new float[9];
            float[] orientacion = new float[3];

            SensorManager.getRotationMatrixFromVector(
                    matriz,
                    evento.values
            );

            SensorManager.getOrientation(
                    matriz,
                    orientacion
            );

            float grados = (float) Math.toDegrees(
                    orientacion[0]
            );

            if (grados < 0) {
                grados += 360;
            }

            String direccion;

            if (grados < 45 || grados >= 315) {
                direccion = "NORTE";
            } else if (grados < 135) {
                direccion = "ESTE";
            } else if (grados < 225) {
                direccion = "SUR";
            } else {
                direccion = "OESTE";
            }

            resultado.setText(
                    direccion + "\n\n"
                            + Math.round(grados) + " grados"
            );
        }

        // JUEGO CAZAESTRELLAS

        else if (seccion.equals("Cazaestrellas")) {

            float x = evento.values[0];
            float y = evento.values[1];

            if (Math.abs(x) > 4 || Math.abs(y) > 4) {

                long ahora = System.currentTimeMillis();

                if (ahora - ultimoPunto > 1000) {

                    puntuacion++;
                    ultimoPunto = ahora;

                    resultado.setText(
                            "Estrellas: " + puntuacion
                    );
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(
            Sensor sensor,
            int precision
    ) {
    }
}
