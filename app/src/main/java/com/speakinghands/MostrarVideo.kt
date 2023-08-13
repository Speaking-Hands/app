package com.speakinghands

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.speakinghands.databinding.MostrarVideoBinding

class MostrarVideo : AppCompatActivity() {

    private lateinit var binding: MostrarVideoBinding
    private lateinit var fullscreenContent: VideoView
    private lateinit var cancelarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MostrarVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent = binding.fullscreenContent
        cancelarButton = binding.cancelButton

        val extras = intent.extras
        if (extras != null) {
            val value = extras.get("videoUri") as Uri
            fullscreenContent.setVideoURI(value)
            fullscreenContent.start()
        }

        cancelarButton.setOnClickListener {
            val i = Intent(this@MostrarVideo, Inicio::class.java)
            startActivity(i)
        }
    }
}