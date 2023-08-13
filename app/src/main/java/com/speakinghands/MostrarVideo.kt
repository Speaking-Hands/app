package com.speakinghands

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.speakinghands.databinding.ActivityMostrarVideoBinding

class MostrarVideo : AppCompatActivity() {

    private lateinit var binding: ActivityMostrarVideoBinding
    private lateinit var fullscreenContent: VideoView
    private lateinit var cancelarButton: Button
    private lateinit var traducirButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMostrarVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fullscreenContent = binding.fullscreenContent
        cancelarButton = binding.cancelButton
        traducirButton = binding.traducirButton

        val extras = intent.extras ?: throw Exception("Error in app")

        val value = extras.get("videoUri") as Uri
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
}