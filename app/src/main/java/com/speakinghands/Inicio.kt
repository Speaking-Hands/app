package com.speakinghands

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.util.IOUtils
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

class Inicio : AppCompatActivity() {

    private lateinit var grabarButton: Button
    private lateinit var galeriaButton: Button
    private lateinit var cancelarButton: Button
    private lateinit var traducirButton: Button
    private lateinit var videoView: VideoView
    private lateinit var description: TextView
    private lateinit var progressBar: ProgressBar

    private val GALLERY = 1
    private val CAMERA = 2
    private val REQUEST_PERMISIONS = 3

    private var URL_BACKEND = "https://speaking-hands-api-zysstglldq-ey.a.run.app";

    private var videoUri = null as Uri?

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        grabarButton = findViewById(R.id.buttonGrabar)
        galeriaButton = findViewById(R.id.buttonGaleria)
        cancelarButton = findViewById(R.id.cancelButton)
        traducirButton = findViewById(R.id.translateButton)
        videoView = findViewById(R.id.videoGrabado)
        description = findViewById(R.id.appDescription)
        progressBar = findViewById(R.id.progressBar)

        progressBar.visibility = View.INVISIBLE

        galeriaButton.isEnabled = false
        grabarButton.isEnabled = false

        setVideoInvisible()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_VIDEO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_VIDEO),
                REQUEST_PERMISIONS
            )
        } else {
            grabarButton.isEnabled = true
            galeriaButton.isEnabled = true
        }

        grabarButton.setOnClickListener {
            val i = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(i, CAMERA)
        }

        galeriaButton.setOnClickListener {
            val i = Intent(
                Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(i, GALLERY)
        }

        cancelarButton.setOnClickListener {
            setVideoInvisible()
            endTranslation()
        }

        traducirButton.setOnClickListener {
            runUploadVideoEndpoint()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA && data?.data != null) {
            setVideoVisible(data)
        }

        if (requestCode == GALLERY && data?.data != null) {
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

        val i = Intent(this@Inicio, MostrarVideo::class.java)
        i.putExtra("videoUri", data?.data)
        startActivity(i)

        videoUri = data?.data
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

        videoUri = null
        videoView.setVideoURI(null)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            grabarButton.isEnabled = true
            galeriaButton.isEnabled = true
        }
    }

    fun runBasicEndpoint() {
        val request = Request.Builder()
            .url(URL_BACKEND)
            .addHeader("x-api-key", "PwBpyZ0rW57yrbcNUhFUNaVJMMWDbwm6")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) =
                println(response.body()?.string())
        })
    }

    fun runUploadVideoEndpoint() {

        val stream = contentResolver.openInputStream(videoUri!!)
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

        startTranslation()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful()) println("Unexpected code $response")

                val jsonDataString = response.peekBody(Long.MAX_VALUE).string()

                val result = JSONObject(jsonDataString)

                runOnUiThread {
                    println(jsonDataString)
                    setVideoInvisible()
                    endTranslation()
                    description.text = result.getString("prediction")
                }

            }
        })
    }

    private fun startTranslation() {
        progressBar.visibility = View.VISIBLE
        videoView.visibility = View.INVISIBLE
        traducirButton.isEnabled = false
    }

    private fun endTranslation() {
        progressBar.visibility = View.INVISIBLE
        videoView.visibility = View.INVISIBLE
        traducirButton.isEnabled = true
    }


}