package com.speakinghands

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.VideoView
import androidx.core.app.ActivityCompat

class VideoRecorder : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_recorder)

        button = findViewById(R.id.floatingActionButton3)
        videoView = findViewById(R.id.videoView)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)

            button.isEnabled = false
        } else {
            button.isEnabled = true
        }

        button.setOnClickListener {
            var i = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(i, 1111)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1111){
            videoView.setVideoURI(data?.data)
            videoView.start()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            button.isEnabled = true
        }
    }
}