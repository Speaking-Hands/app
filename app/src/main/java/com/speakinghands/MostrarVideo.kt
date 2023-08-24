package com.speakinghands

import android.content.Intent
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.speakinghands.databinding.ActivityMostrarVideoBinding
import java.util.Timer
import java.util.TimerTask


class MostrarVideo : AppCompatActivity() {

    private lateinit var binding: ActivityMostrarVideoBinding
    private lateinit var fullscreenContent: VideoView
    private lateinit var cancelarButton: Button
    private lateinit var traducirButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMostrarVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fullscreenContent = binding.fullscreenContent
        cancelarButton = binding.cancelButton
        traducirButton = binding.traducirButton
        progressBar = binding.progressBar

        val extras = intent.extras ?: throw Exception("Error in app")

        val value = extras.get("videoUri") as Uri

        fullscreenContent.setOnPreparedListener(OnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            mediaPlayer.setVolume(0f, 0f)

            setDuration();
            timerCounter();

        })

        fullscreenContent.setVideoURI(value)
        fullscreenContent.start()

        cancelarButton.setOnClickListener {
            val i = Intent(this@MostrarVideo, Inicio::class.java)
            startActivity(i)
        }

        traducirButton.setOnClickListener {
            val i = Intent(this@MostrarVideo, Traduciendo::class.java)
            i.putExtra("videoUri", value)
            startActivity(i)
        }
    }

private var timer: Timer? = null
private fun timerCounter() {
    timer = Timer()
    val task: TimerTask = object : TimerTask() {
        override fun run() {
            runOnUiThread(Runnable { updateUI() })
        }
    }
    timer!!.schedule(task, 0, 20)
}

private var duration = 0

private fun setDuration() {
    duration = fullscreenContent.getDuration()
}

private fun updateUI() {
    if (progressBar.progress >= 100) {
        timer!!.cancel()
    }
    val current: Int = fullscreenContent.getCurrentPosition()
    val progress = current * 100 / duration
    progressBar.progress = progress
}

}