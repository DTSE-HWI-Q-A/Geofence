package com.hms.demo.geofence

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginStart
import com.hms.demo.geofence.databinding.GeofenceEditorBinding
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Polygon


class GeofenceEditorActivity : AppCompatActivity(),MapFragment.OnMapFragmentInteractionListener {
    var drawableMap: DrawableMap? = null
    lateinit var binding: GeofenceEditorBinding
    lateinit var polygonGeofence: PolygonGeofence

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GeofenceEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar=binding.toolbar.root
        setSupportActionBar(toolbar)
        //Adding a back arrow to the toolbar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
        polygonGeofence=GeofenceEditorContract.parseGeofenceFromExtras(intent.extras)
        val mapFragment = MapFragment().also { drawableMap = it }
        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container_view,
            mapFragment
        ).commit()



    }

    override fun onDrawnPolygon(polygon: Polygon) {


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.geofence_editor,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.done_item ->{onDone()}
            R.id.undo_item ->drawableMap?.undo()
            R.id.redo_item ->drawableMap?.redo()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onDone() {
        val geofencePoints=drawableMap?.getPoints()
        if(geofencePoints.isNullOrEmpty()){
            showErrorDialog(R.string.no_points)
            return
        }
        if(geofencePoints.size<3){
            showErrorDialog(R.string.no_polygon)
            return
        }
        if(polygonGeofence.name=="")
            showNameDialog()
        else {
            polygonGeofence.points.apply {
                clear()
                addAll(geofencePoints)
            }
            returnResult(polygonGeofence)
        }


    }

    private fun showErrorDialog(resourceMessage:Int){
        AlertDialog.Builder(this)
            .setTitle(R.string.alert)
            .setMessage(resourceMessage)
            .setPositiveButton(R.string.ok){dialogInterface,_->
                dialogInterface.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun showNameDialog() {
        val textInput= EditText(this)
        AlertDialog.Builder(this)
            .setTitle(R.string.confirmation)
            .setMessage(R.string.name_your_geofence)
            .setView(textInput)
            .setPositiveButton(R.string.ok){dialogInterface,_->
                if(!textInput.text.isNullOrEmpty()){
                    polygonGeofence.name=textInput.text.toString()
                    returnResult(polygonGeofence)
                    dialogInterface.dismiss()
                }
            }
            .setCancelable(false)
            .create()
            .show()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onMapReady() {
            polygonGeofence.points.let {
                if(it.isNotEmpty()) drawableMap?.drawEditablePolygon(it)
            }
        if(checkLocationPermissions())
            drawableMap?.enableLocation()
        else requestPermissions( arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_REQUEST)
    }

    override fun onMapLongClick(latLng: LatLng?) {
        latLng?.let {
            drawableMap?.drawPolygonMarker(it)
        }
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

    private fun returnResult(polygonGeofence: PolygonGeofence){
        val extras=GeofenceEditorContract.parseGeofenceToExtras(polygonGeofence)
        val intent=Intent().apply {
            putExtras(extras)
        }
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    companion object{
        const val LOCATION_REQUEST=100

    }
}