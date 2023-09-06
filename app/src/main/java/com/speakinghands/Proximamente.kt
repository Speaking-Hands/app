package com.speakinghands

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.speakinghands.databinding.ActivityInfoBinding
import com.speakinghands.databinding.ActivityProximamenteBinding

class Proximamente : AppCompatActivity() {
    private lateinit var binding: ActivityProximamenteBinding

    private lateinit var volverButton: Button

    private lateinit var menu1Button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProximamenteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        volverButton = binding.volverButton
        menu1Button = binding.menu1


        volverButton.setOnClickListener {
            val i = Intent(this@Proximamente, Inicio::class.java)
            startActivity(i)
        }

        menu1Button.setOnClickListener {
            val i = Intent(this@Proximamente, Inicio::class.java)
            startActivity(i)
        }
    }
}