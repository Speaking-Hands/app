package com.speakinghands

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.speakinghands.databinding.ActivityInicioBinding

class Inicio : AppCompatActivity() {

    private lateinit var binding: ActivityInicioBinding

    private lateinit var grabarButton: Button
    private lateinit var galeriaButton: Button
    private lateinit var infoButton: Button

    private val GALLERY = 1
    private val CAMERA = 2
    private val REQUEST_PERMISIONS = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        grabarButton = binding.buttonGrabar
        galeriaButton = binding.buttonGaleria
        infoButton = binding.info

        checkPermissions()

        grabarButton.setOnClickListener {
            val i = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(i, CAMERA)
        }

        infoButton.setOnClickListener {
            val i = Intent(this@Inicio, Info::class.java)
            startActivity(i)
        }

        galeriaButton.setOnClickListener {
            val i = Intent(
                Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(i, GALLERY)
        }

    }

    private fun checkPermissions() {
        galeriaButton.isEnabled = false
        grabarButton.isEnabled = false

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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == CAMERA || requestCode == GALLERY) && data?.data != null) {
            val i = Intent(this@Inicio, MostrarVideo::class.java)
            i.putExtra("videoUri", data.data)
            startActivity(i)
        }
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


}