package com.example.nox

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

class SectionActivity : Activity(), SensorEventListener {

    private lateinit var content: LinearLayout
    private lateinit var result: TextView
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    private var section = SECTION_HOME
    private var score = 0
    private var lastPointAt = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        section = intent.getStringExtra(EXTRA_SECTION) ?: SECTION_HOME
        val scroll = ScrollView(this)
        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(24, 35, 24, 35)
            setBackgroundColor(0xFF101318.toInt())
        }
        scroll.addView(content)
        setContentView(scroll)

        when (section) {
            SECTION_HOME -> showNightHome()
            SECTION_DARKNESS -> showDarkness()
            SECTION_COMPASS -> showCompass()
            SECTION_CONSTELLATIONS -> showConstellations()
            SECTION_DIARY -> showDiary()
            SECTION_STAR_CATCHER -> showStarCatcher()
            else -> showNightHome()
        }

        createButton(getString(R.string.back_home)).setOnClickListener { finish() }
    }

    private fun showNightHome() {
        createText(getString(R.string.night_home_title), 26)
        createText(getString(R.string.night_home_subtitle), 16)

        listOf(
            R.string.section_darkness to SECTION_DARKNESS,
            R.string.section_compass to SECTION_COMPASS,
            R.string.section_constellations to SECTION_CONSTELLATIONS,
            R.string.section_diary to SECTION_DIARY,
            R.string.section_star_catcher to SECTION_STAR_CATCHER
        ).forEach { (label, destination) ->
            createButton(getString(label)).setOnClickListener {
                startActivity(
                    Intent(this, SectionActivity::class.java)
                        .putExtra(EXTRA_SECTION, destination)
                )
            }
        }
    }

    private fun createText(text: String, size: Int): TextView {
        return TextView(this).apply {
            this.text = text
            setTextColor(0xFFFFFFFF.toInt())
            textSize = size.toFloat()
            gravity = Gravity.CENTER
            setPadding(10, 15, 10, 15)
            content.addView(this)
        }
    }

    private fun createButton(text: String): Button {
        return Button(this).apply {
            this.text = text
            content.addView(this)
        }
    }

    private fun prepareSensor(type: Int) {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager?.getDefaultSensor(type)
        if (sensor == null) {
            result.setText(R.string.sensor_unavailable)
        }
    }

    private fun showDarkness() {
        createText(getString(R.string.darkness_title), 26)
        createText(getString(R.string.darkness_subtitle), 16)
        result = createText(getString(R.string.sensor_waiting), 21)
        prepareSensor(Sensor.TYPE_LIGHT)
    }

    private fun showCompass() {
        createText(getString(R.string.compass_title), 28)
        createText(getString(R.string.compass_subtitle), 16)
        result = createText(getString(R.string.sensor_searching), 25)
        prepareSensor(Sensor.TYPE_ROTATION_VECTOR)
    }

    private fun showConstellations() {
        createText(getString(R.string.constellations_title), 24)
        createText(getString(R.string.constellations_subtitle), 16)

        result = createText(getString(R.string.constellation_prompt), 17)
        val constellations = listOf(
            R.string.orion to R.string.orion_description,
            R.string.ursa_major to R.string.ursa_major_description,
            R.string.ursa_minor to R.string.ursa_minor_description,
            R.string.cassiopeia to R.string.cassiopeia_description,
            R.string.scorpius to R.string.scorpius_description,
            R.string.leo to R.string.leo_description
        )
        constellations.forEach { (name, description) ->
            createButton(getString(name)).setOnClickListener {
                result.text = "${getString(name)}\n\n${getString(description)}"
            }
        }
    }

    private fun showDiary() {
        createText(getString(R.string.diary_title), 23)

        val input = EditText(this).apply {
            hint = getString(R.string.diary_hint)
            setTextColor(0xFFFFFFFF.toInt())
            setHintTextColor(0xFFAAAAAA.toInt())
            minLines = 3
            gravity = Gravity.TOP
        }
        content.addView(input)
        result = createText("", 16)

        createButton(getString(R.string.diary_save)).setOnClickListener {
            val observation = input.text.toString().trim()
            if (observation.isEmpty()) {
                Toast.makeText(this, R.string.diary_empty, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val preferences = getSharedPreferences(DIARY_PREFERENCES, MODE_PRIVATE)
            val previous = preferences.getString(DIARY_KEY, "").orEmpty()
            val date = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
            val entry = "$date\n$observation"
            val updated = if (previous.isEmpty()) entry else "$previous\n\n$entry"
            preferences.edit().putString(DIARY_KEY, updated).apply()
            input.text.clear()
            Toast.makeText(this, R.string.diary_saved, Toast.LENGTH_SHORT).show()
        }

        createButton(getString(R.string.diary_view)).setOnClickListener {
            val preferences: SharedPreferences =
                getSharedPreferences(DIARY_PREFERENCES, MODE_PRIVATE)
            result.text = preferences.getString(DIARY_KEY, null)
                ?: getString(R.string.diary_empty_list)
        }
    }

    private fun showStarCatcher() {
        createText(getString(R.string.star_catcher_title), 29)
        createText(getString(R.string.star_catcher_subtitle), 17)
        score = 0
        result = createText(getString(R.string.star_score, score), 26)
        createText(getString(R.string.star_catcher_instruction), 15)
        prepareSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        val manager = sensorManager
        val activeSensor = sensor
        if (manager != null && activeSensor != null) {
            manager.registerListener(this, activeSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        sensorManager?.unregisterListener(this)
        super.onPause()
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (section) {
            SECTION_DARKNESS -> updateDarkness(event)
            SECTION_COMPASS -> updateCompass(event)
            SECTION_STAR_CATCHER -> updateStarCatcher(event)
        }
    }

    private fun updateDarkness(event: SensorEvent) {
        val light = event.values[0]
        val level = when {
            light < 10 -> R.string.light_very_low
            light < 100 -> R.string.light_low
            light < 500 -> R.string.light_moderate
            else -> R.string.light_high
        }
        result.text = getString(R.string.light_reading, light, getString(level))
    }

    private fun updateCompass(event: SensorEvent) {
        val matrix = FloatArray(9)
        val orientation = FloatArray(3)
        SensorManager.getRotationMatrixFromVector(matrix, event.values)
        SensorManager.getOrientation(matrix, orientation)

        var degrees = Math.toDegrees(orientation[0].toDouble()).toFloat()
        if (degrees < 0) degrees += 360f
        val direction = when {
            degrees < 45 || degrees >= 315 -> R.string.north
            degrees < 135 -> R.string.east
            degrees < 225 -> R.string.south
            else -> R.string.west
        }
        result.text = getString(R.string.compass_reading, getString(direction), degrees.roundToInt())
    }

    private fun updateStarCatcher(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        if (abs(x) > 4 || abs(y) > 4) {
            val now = System.currentTimeMillis()
            if (now - lastPointAt > POINT_INTERVAL_MS) {
                score++
                lastPointAt = now
                result.text = getString(R.string.star_score, score)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    companion object {
        const val EXTRA_SECTION = "nombre_seccion"
        const val SECTION_HOME = "Mi noche"
        private const val SECTION_DARKNESS = "Índice de oscuridad"
        private const val SECTION_COMPASS = "Brújula"
        private const val SECTION_CONSTELLATIONS = "Constelaciones"
        private const val SECTION_DIARY = "Diario de observaciones"
        private const val SECTION_STAR_CATCHER = "Cazaestrellas"
        private const val DIARY_PREFERENCES = "NOX_DIARIO"
        private const val DIARY_KEY = "observaciones"
        private const val DATE_FORMAT = "dd/MM/yyyy HH:mm"
        private const val POINT_INTERVAL_MS = 1_000L
    }
}
