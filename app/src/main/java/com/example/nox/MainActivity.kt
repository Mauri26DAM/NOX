package com.example.nox

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {

    private lateinit var durationPicker: NumberPicker
    private lateinit var resultMessage: TextView

    private val adaptationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val message = if (result.resultCode == RESULT_OK) {
                getString(R.string.adaptation_completed)
            } else {
                getString(R.string.adaptation_cancelled)
            }
            resultMessage.text = message
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logLifecycle("onCreate")
        setContentView(R.layout.activity_main)

        durationPicker = findViewById(R.id.durationPicker)
        durationPicker.minValue = MIN_DURATION_MINUTES
        durationPicker.maxValue = MAX_DURATION_MINUTES
        durationPicker.value = savedInstanceState
            ?.getInt(STATE_DURATION_MINUTES)
            ?.coerceIn(MIN_DURATION_MINUTES, MAX_DURATION_MINUTES)
            ?: DEFAULT_DURATION_MINUTES
        durationPicker.wrapSelectorWheel = false

        resultMessage = findViewById(R.id.resultMessage)
        resultMessage.text = savedInstanceState?.getString(STATE_RESULT_MESSAGE).orEmpty()

        findViewById<Button>(R.id.btnMiNoche).setOnClickListener {
            logLifecycle("abrir Mi noche")
            startActivity(
                Intent(this, SectionActivity::class.java)
                    .putExtra(SectionActivity.EXTRA_SECTION, SectionActivity.SECTION_HOME)
            )
        }

        findViewById<Button>(R.id.btnAdaptacion).setOnClickListener {
            val durationMs = durationPicker.value * MILLIS_PER_MINUTE
            val intent = Intent(this, AdaptationActivity::class.java)
                .putExtra(AdaptationActivity.EXTRA_DURATION_MS, durationMs)
            adaptationLauncher.launch(intent)
        }

        findViewById<Button>(R.id.btnAcerca).setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.project_url))
            )
            try {
                startActivity(intent)
            } catch (_: ActivityNotFoundException) {
                Toast.makeText(this, R.string.no_browser, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        logLifecycle("onStart")
    }

    override fun onResume() {
        super.onResume()
        logLifecycle("onResume")
    }

    override fun onPause() {
        logLifecycle("onPause")
        super.onPause()
    }

    override fun onStop() {
        logLifecycle("onStop")
        super.onStop()
    }

    override fun onDestroy() {
        logLifecycle("onDestroy")
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_DURATION_MINUTES, durationPicker.value)
        outState.putString(STATE_RESULT_MESSAGE, resultMessage.text.toString())
        super.onSaveInstanceState(outState)
    }

    private fun logLifecycle(event: String) {
        Log.d(TAG, event)
    }

    private companion object {
        const val TAG = "NOX-Lifecycle"
        const val STATE_DURATION_MINUTES = "state_duration_minutes"
        const val STATE_RESULT_MESSAGE = "state_result_message"
        const val DEFAULT_DURATION_MINUTES = 20
        const val MIN_DURATION_MINUTES = 1
        const val MAX_DURATION_MINUTES = 30
        const val MILLIS_PER_MINUTE = 60_000L
    }
}
