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

class MainActivity : AppCompatActivity() {

    private lateinit var grabarButton: Button
    private lateinit var cancelarButton: Button
    private lateinit var traducirButton: Button
    private lateinit var videoView: VideoView
    private lateinit var description: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        grabarButton = findViewById(R.id.button)
        cancelarButton = findViewById(R.id.cancelButton)
        traducirButton = findViewById(R.id.translateButton)
        videoView = findViewById(R.id.videoView)
        description = findViewById(R.id.appDescription)

        grabarButton.isEnabled = false

        setVideoInvisible()

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)
        } else {
            grabarButton.isEnabled = true
        }

        grabarButton.setOnClickListener {
            val i = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(i, 1111)
        }

        cancelarButton.setOnClickListener {
            setVideoInvisible()
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

        videoView.setVideoURI(data?.data)
        videoView.start()
    }

    private fun setVideoInvisible() {
        videoView.visibility = View.INVISIBLE
        cancelarButton.visibility = View.INVISIBLE
        traducirButton.visibility = View.INVISIBLE

        description.visibility = View.VISIBLE
        grabarButton.visibility = View.VISIBLE

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
}