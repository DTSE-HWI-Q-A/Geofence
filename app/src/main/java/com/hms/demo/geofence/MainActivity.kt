package com.hms.demo.geofence

import android.Manifest
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.hms.demo.geofence.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding=ActivityMainBinding.inflate(layoutInflater)
        mainBinding.floatingActionButton.setOnClickListener{newGeofence()}

    }

    private fun newGeofence() {
        val intent= Intent(this,GeofenceEditorActivity::class.java)

        startActivityForResult(intent,200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }


}