package com.speakinghands

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.speakinghands.databinding.ActivityInfoBinding

class Info : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding

    private lateinit var volverButton: Button

    private lateinit var menu2Button: Button
    private lateinit var menu3Button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        volverButton = binding.volverButton
        menu2Button = binding.menu2
        menu3Button = binding.menu3
        volverButton.setOnClickListener {
            val i = Intent(this@Info, Inicio::class.java)
            startActivity(i)
        }

        menu2Button.setOnClickListener {
            val i = Intent(this@Info, Proximamente::class.java)
            startActivity(i)
        }

        menu3Button.setOnClickListener {
            val i = Intent(this@Info, Proximamente::class.java)
            startActivity(i)
        }
    }
}