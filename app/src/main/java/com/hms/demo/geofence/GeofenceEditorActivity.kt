package com.hms.demo.geofence

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.commit
import com.hms.demo.geofence.databinding.GeofenceEditorBinding
import com.huawei.hms.maps.model.LatLng


class GeofenceEditorActivity : AppCompatActivity(), MapFragment.OnMapFragmentInteractionListener{
    var drawableMap: DrawableMap? = null
    val points: ArrayList<LatLng> = ArrayList()
    lateinit var binding:GeofenceEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= GeofenceEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment=MapFragment().also { drawableMap=it }
        supportFragmentManager.beginTransaction().add(R.id.fragment_container_view,
            mapFragment).commit()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //menuInflater.inflate(R.menu.geofence_editor, menu)
        return true
    }

    override fun onMapLongClick(latLng: LatLng?) {
        latLng?.let { points.add(it) }

        drawableMap?.apply {
            draw(points)
            }


    }

    /*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        when (id) {
            R.id.undo_item -> if (count > 0) undoLast()
            R.id.ok_item -> if (count >= 4) {
                if (editing) {
                    val bundle = Bundle()
                    bundle.putInt(POSITION, position)
                    bundle.putParcelableArrayList(GEOFENCE, points)
                    val i = Intent()
                    i.putExtra(DATA, bundle)
                    setResult(ServerUtilities.SERVER_OK, i)
                    finish()
                } else {
                    showNameDialog()
                }
            } else {
                val b: AlertDialog.Builder = Builder(this)
                b.setTitle(R.string.attention)
                b.setMessage(R.string.min_points_message)
                b.create().show()
            }
        }
        return true
    }

    private fun showNameDialog() {
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        val b: AlertDialog.Builder = Builder(this)
        b.setTitle(R.string.name)
        b.setView(input)
        b.setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, which ->
            val bundle = Bundle()
            bundle.putString(NAME, input.text.toString())
            bundle.putParcelableArrayList(GEOFENCE, points)
            val i = Intent()
            i.putExtra(DATA, bundle)
            setResult(ServerUtilities.SERVER_OK, i)
            dialog.dismiss()
            finish()
        })
        b.create().show()
    }

    fun onMapLongClick(latLng: LatLng) {
        points!!.add(latLng)
        count++
        mapFragment.drawMarkers(points)
    }

    fun onMapReady() {
        val b = intent.extras
        if (b != null) {
            editing = true
            points = b.getParcelableArrayList<Parcelable>(GEOFENCE)
            position = b.getInt(POSITION)
            mapFragment.goToLocation(points!![0])
            mapFragment.drawMarkers(points)
        } else {
            editing = false
            points = ArrayList()
        }
        count = points!!.size()
        gps = GPSTracker(this, this)
        gps.startLocationRequests()
        fab = findViewById(R.id.gps_btn)
        fab.setOnClickListener(this)
    }

    fun undoLast() {
        if (count > 0) {
            points!!.remove(points!!.size() - 1)
            count--
            mapFragment.drawMarkers(points)
        }
    }


    override fun onClick(v: View?) {
        if (mapFragment != null) {
            mapFragment.goToLocation(currentLocation)
        }
    }

    override fun onBackPressed() {
        setResult(0)
        finish()
    }

    fun onLocationChanged(location: Location) {
        currentLocation = LatLng(location.getLatitude(), location.getLongitude())
    }*/
}