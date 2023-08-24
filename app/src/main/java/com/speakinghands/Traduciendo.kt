package com.speakinghands

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.util.IOUtils
import com.speakinghands.databinding.ActivityTraduciendoBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class Traduciendo : AppCompatActivity() {

    private lateinit var binding: ActivityTraduciendoBinding

    private lateinit var textoTraduccion: TextView
    private lateinit var textoResultado: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var startButton: Button


    private var URL_BACKEND = "https://speaking-hands-api-zysstglldq-ey.a.run.app"

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(2, TimeUnit.MINUTES)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traduciendo)

        binding = ActivityTraduciendoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.textoTraduccion = binding.textView2
        this.textoResultado = binding.textView3
        
        progressBar = binding.progressBar
        startButton = binding.startButton

        val extras = intent.extras ?: throw Exception("Error in app")

        val value = extras.get("videoUri") as Uri

        val stream = contentResolver.openInputStream(value)
        val byteArray: ByteArray = IOUtils.toByteArray(stream)
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "video", "fname",
                RequestBody.create(MediaType.parse("video/mp4"), byteArray)
            )
            .build()
        val request: Request = Request.Builder()
            .url("$URL_BACKEND/predict")
            .addHeader("x-api-key", "PwBpyZ0rW57yrbcNUhFUNaVJMMWDbwm6")
            .post(requestBody)
            .build()

        startButton.setOnClickListener {
            val i = Intent(this@Traduciendo, Inicio::class.java)
            startActivity(i)
        }

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    progressBar.visibility = GONE
                    startButton.visibility = VISIBLE
                    textoResultado.visibility = VISIBLE

                    textoTraduccion.text = getString(R.string.ups)

                    textoResultado.text = getString(R.string.translate_fail)
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful()) println("Unexpected code $response")

                val jsonDataString = response.peekBody(Long.MAX_VALUE).string()

                val result = JSONObject(jsonDataString)

                runOnUiThread {
                    println(jsonDataString)
                    progressBar.visibility = GONE
                    startButton.visibility = VISIBLE
                    textoResultado.visibility = VISIBLE

                    textoTraduccion.text = getString(R.string.traducido)

                    textoResultado.text = result.getString("prediction")
                }

            }
        })

    }

    override fun onBackPressed() {
        val i = Intent(this@Traduciendo, Inicio::class.java)
        startActivity(i)
    }
}