package com.example.homework10

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.homework10.databinding.ActivityMainBinding

const val IS_TIMER_RUNNING = "isTimerRunning"
const val TIME_LEFT = "timeLeft"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var timer: CountDownTimer? = null
    private var isTimerRunning = false
    private var timeLeft: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.seekBarTimer.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.textViewProgress.text = "$progress"
                binding.progressBar.progress = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.buttonStartStop.setOnClickListener {
            if (!isTimerRunning) {
                binding.seekBarTimer.isEnabled = false

                startTimer(binding.seekBarTimer.progress * 1000L)
            } else {
                timer?.cancel()
                isTimerRunning = false
                binding.seekBarTimer.isEnabled = true
            }
        }

        if (savedInstanceState != null) {
            isTimerRunning = savedInstanceState.getBoolean(IS_TIMER_RUNNING, false)
            if (isTimerRunning) {
                timeLeft = savedInstanceState.getLong(TIME_LEFT)
                startTimer(timeLeft)
            }
        }
    }

    private fun startTimer(time: Long) {
        timer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                val secondsRemaining = millisUntilFinished / 1000
                binding.textViewProgress.text = "$secondsRemaining"
                binding.progressBar.progress = secondsRemaining.toInt()
            }

            override fun onFinish() {
                timer?.cancel()
                isTimerRunning = false
                binding.seekBarTimer.isEnabled = true
            }
        }.start()
        isTimerRunning = true
        binding.seekBarTimer.isEnabled = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_TIMER_RUNNING, isTimerRunning)
        outState.putLong(TIME_LEFT, timeLeft)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}