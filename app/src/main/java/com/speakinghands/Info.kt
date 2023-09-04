package com.speakinghands

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.speakinghands.databinding.ActivityInfoBinding

class Info : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding

    private lateinit var volverButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        volverButton = binding.volverButton

        volverButton.setOnClickListener {
            val i = Intent(this@Info, Inicio::class.java)
            startActivity(i)
        }
    }
}