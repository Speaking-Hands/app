package com.speakinghands

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.common.util.IOUtils
import com.speakinghands.databinding.ActivityMostrarVideoBinding
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

class Traduciendo : AppCompatActivity() {

    private lateinit var binding: ActivityTraduciendoBinding

    private lateinit var texto: TextView


    private var URL_BACKEND = "https://speaking-hands-api-zysstglldq-ey.a.run.app"

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traduciendo)

        binding = ActivityTraduciendoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        texto = binding.textView2

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

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful()) println("Unexpected code $response")

                val jsonDataString = response.peekBody(Long.MAX_VALUE).string()

                val result = JSONObject(jsonDataString)

                runOnUiThread {
                    println(jsonDataString)
                    texto.text = result.getString("prediction")
                }

            }
        })

    }
}