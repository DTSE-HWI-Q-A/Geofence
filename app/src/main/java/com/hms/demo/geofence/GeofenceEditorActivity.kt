package com.hms.demo.geofence

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.commit
import com.hms.demo.geofence.databinding.GeofenceEditorBinding
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Marker
import com.huawei.hms.maps.model.Polygon


class GeofenceEditorActivity : AppCompatActivity(),MapFragment.OnMapFragmentInteractionListener {
    var drawableMap: DrawableMap? = null
    lateinit var binding: GeofenceEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GeofenceEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = MapFragment().also { drawableMap = it }
        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container_view,
            mapFragment
        ).commit()
    }

    override fun onDrawnPolygon(polygon: Polygon) {

    }

    override fun onMapReady() {
        Toast.makeText(this,"OnMapReady",Toast.LENGTH_SHORT).show()
        if(checkLocationPermissions())
            drawableMap?.enableLocation()
        else requestPermissions( arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_REQUEST)
    }

    private fun checkLocationPermissions(): Boolean {
        val acl= checkSelfPermission( Manifest.permission.ACCESS_FINE_LOCATION)
        val afl= checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        return acl== PackageManager.PERMISSION_GRANTED||afl== PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== LOCATION_REQUEST){
            if(checkLocationPermissions())
                drawableMap?.enableLocation()
        }
    }

    companion object{
        const val LOCATION_REQUEST=100
    }
}