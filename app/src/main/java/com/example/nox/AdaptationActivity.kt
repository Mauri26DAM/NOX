package com.example.nox

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.TextView

class AdaptationActivity : Activity() {

    private lateinit var countdownText: TextView
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private var countdown: CountDownTimer? = null
    private var remainingMs = DEFAULT_DURATION_MS
    private var deadlineMs = 0L
    private var shouldResume = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logLifecycle("onCreate")
        setContentView(R.layout.activity_adaptation)

        countdownText = findViewById(R.id.countdownText)
        startButton = findViewById(R.id.btnStartTimer)
        pauseButton = findViewById(R.id.btnPauseTimer)

        if (savedInstanceState == null) {
            remainingMs = intent.getLongExtra(EXTRA_DURATION_MS, DEFAULT_DURATION_MS)
                .coerceIn(MIN_DURATION_MS, MAX_DURATION_MS)
        } else {
            remainingMs = savedInstanceState.getLong(STATE_REMAINING_MS, DEFAULT_DURATION_MS)
                .coerceIn(0L, MAX_DURATION_MS)
            shouldResume = savedInstanceState.getBoolean(STATE_SHOULD_RESUME)
        }

        updateCountdownText()
        startButton.setOnClickListener {
            if (remainingMs <= 0L) {
                remainingMs = intent.getLongExtra(EXTRA_DURATION_MS, DEFAULT_DURATION_MS)
                    .coerceIn(MIN_DURATION_MS, MAX_DURATION_MS)
            }
            shouldResume = true
            startCountdownIfResumed()
        }
        pauseButton.setOnClickListener {
            pauseCountdown()
        }
    }

    override fun onStart() {
        super.onStart()
        logLifecycle("onStart")
    }

    override fun onResume() {
        super.onResume()
        logLifecycle("onResume")
        startCountdownIfResumed()
    }

    override fun onPause() {
        logLifecycle("onPause")
        saveRemainingTime()
        countdown?.cancel()
        countdown = null
        super.onPause()
    }

    override fun onStop() {
        logLifecycle("onStop")
        super.onStop()
    }

    override fun onDestroy() {
        logLifecycle("onDestroy")
        countdown?.cancel()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        saveRemainingTime()
        outState.putLong(STATE_REMAINING_MS, remainingMs)
        outState.putBoolean(STATE_SHOULD_RESUME, shouldResume)
        super.onSaveInstanceState(outState)
    }

    private fun startCountdownIfResumed() {
        if (!shouldResume || countdown != null || isFinishing) return

        if (remainingMs <= 0L) {
            completeAdaptation()
            return
        }

        deadlineMs = SystemClock.elapsedRealtime() + remainingMs
        countdown = object : CountDownTimer(remainingMs, COUNTDOWN_INTERVAL_MS) {
            override fun onTick(millisUntilFinished: Long) {
                remainingMs = millisUntilFinished
                updateCountdownText()
            }

            override fun onFinish() {
                countdown = null
                remainingMs = 0L
                completeAdaptation()
            }
        }.start()
        updateButtons()
    }

    private fun pauseCountdown() {
        saveRemainingTime()
        shouldResume = false
        countdown?.cancel()
        countdown = null
        updateCountdownText()
        updateButtons()
    }

    private fun saveRemainingTime() {
        if (countdown != null) {
            remainingMs = (deadlineMs - SystemClock.elapsedRealtime()).coerceAtLeast(0L)
        }
    }

    private fun updateCountdownText() {
        val totalSeconds = (remainingMs + 999L) / 1_000L
        val minutes = totalSeconds / 60L
        val seconds = totalSeconds % 60L
        countdownText.text = getString(R.string.countdown_format, minutes, seconds)
        updateButtons()
    }

    private fun updateButtons() {
        if (!::startButton.isInitialized || !::pauseButton.isInitialized) return
        startButton.isEnabled = !shouldResume || countdown == null
        pauseButton.isEnabled = shouldResume && countdown != null
    }

    private fun completeAdaptation() {
        shouldResume = false
        updateCountdownText()
        setResult(RESULT_OK)
        finish()
    }

    private fun logLifecycle(event: String) {
        Log.d(TAG, event)
    }

    companion object {
        const val TAG = "NOX-Lifecycle"
        const val EXTRA_DURATION_MS = "duration_ms"
        const val STATE_REMAINING_MS = "state_remaining_ms"
        const val STATE_SHOULD_RESUME = "state_should_resume"
        private const val DEFAULT_DURATION_MS = 20 * 60_000L
        private const val MIN_DURATION_MS = 60_000L
        private const val MAX_DURATION_MS = 30 * 60_000L
        private const val COUNTDOWN_INTERVAL_MS = 1_000L
    }
}
