package com.speakinghands

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.speakinghands.databinding.ActivityTraducidoBinding

class Traducido : AppCompatActivity() {

    private lateinit var binding: ActivityTraducidoBinding

    private lateinit var textoResultado: TextView
    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traducido)

        binding = ActivityTraducidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.textoResultado = binding.textView3

        startButton = binding.startButton

        val extras = intent.extras ?: throw Exception("Error in app")

        val value = extras.get("result") as String

        if (value.trim() == "") showResult(getString(R.string.translate_fail)) else showResult(value)

        startButton.setOnClickListener {
            val i = Intent(this@Traducido, Inicio::class.java)
            startActivity(i)
        }
    }

    override fun onBackPressed() {
        val i = Intent(this@Traducido, Inicio::class.java)
        startActivity(i)
    }

    fun showResult(resultado: String) {
        textoResultado.text = resultado
    }
}