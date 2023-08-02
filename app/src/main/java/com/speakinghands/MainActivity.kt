package com.speakinghands

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.VideoView
import androidx.core.app.ActivityCompat
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var grabarButton: Button
    private lateinit var galeriaButton: Button
    private lateinit var cancelarButton: Button
    private lateinit var traducirButton: Button
    private lateinit var videoView: VideoView
    private lateinit var description: TextView

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        grabarButton = findViewById(R.id.buttonGrabar)
        galeriaButton = findViewById(R.id.buttonGaleria)
        cancelarButton = findViewById(R.id.cancelButton)
        traducirButton = findViewById(R.id.translateButton)
        videoView = findViewById(R.id.videoGrabado)
        description = findViewById(R.id.appDescription)

        galeriaButton.isEnabled = false
        grabarButton.isEnabled = false

        setVideoInvisible()

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)
        } else {
            grabarButton.isEnabled = true
            galeriaButton.isEnabled = true
        }

        grabarButton.setOnClickListener {
            val i = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(i, 1111)
        }

        galeriaButton.setOnClickListener {
            // TODO open gallery
        }

        cancelarButton.setOnClickListener {
            setVideoInvisible()
        }

        traducirButton.setOnClickListener {
            run("https://speaking-hands-api-zysstglldq-ey.a.run.app")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1111 && data?.data != null){
                setVideoVisible(data)
        }
    }

    private fun setVideoVisible(data: Intent?) {
        videoView.visibility = View.VISIBLE
        cancelarButton.visibility = View.VISIBLE
        traducirButton.visibility = View.VISIBLE

        description.visibility = View.INVISIBLE
        grabarButton.visibility = View.INVISIBLE
        galeriaButton.visibility = View.INVISIBLE

        videoView.setVideoURI(data?.data)
        videoView.start()
    }

    private fun setVideoInvisible() {
        videoView.visibility = View.INVISIBLE
        cancelarButton.visibility = View.INVISIBLE
        traducirButton.visibility = View.INVISIBLE

        description.visibility = View.VISIBLE
        grabarButton.visibility = View.VISIBLE
        galeriaButton.visibility = View.VISIBLE

        videoView.setVideoURI(null)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            grabarButton.isEnabled = true
        }
    }

    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .addHeader("x-api-key", "PwBpyZ0rW57yrbcNUhFUNaVJMMWDbwm6")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) = println(response.body()?.string())
        })
    }
}