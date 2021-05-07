package com.hms.demo.geofence

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hms.demo.geofence.databinding.MapBinding
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.*
import java.util.*
import kotlin.collections.ArrayList


class MapFragment : Fragment(), OnMapReadyCallback, HuaweiMap.OnMapLongClickListener, DrawableMap, HuaweiMap.OnMarkerDragListener {

    var binding: MapBinding? = null
    var hMap: HuaweiMap? = null
    private var mapView: MapView? = null
    var mListener: OnMapFragmentInteractionListener? = null
    private val markers = ArrayList<Marker>()
    private val removedMarkers:Queue<LatLng> = LinkedList()
    var currentPolygon: Polygon? = null

    val viewmodel:MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        return MapBinding.inflate(inflater).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.mapView = binding?.mapView
        mapView?.apply {
            onCreate(null)
            getMapAsync(this@MapFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMapFragmentInteractionListener)
            this.mListener = context
    }

    override fun onMapReady(hMap: HuaweiMap?) {
        this.hMap = hMap
        hMap?.apply {
            setOnMapLongClickListener(this@MapFragment)
            setMarkersClustering(true)
            setOnMarkerDragListener(this@MapFragment)
        }
        mListener?.onMapReady()
    }

    override fun drawLocationMarker(latLng: LatLng) {

    }

    override fun drawPolygonMarker(latLng: LatLng) {
        hMap?.let { map ->
            val options = MarkerOptions()
            options.position(latLng).clusterable(true)
            map.addMarker(options).also { markers.add(it) }.isDraggable = true
            drawPolygonWithMarkers()

        }
    }

    private fun drawPolygonWithMarkers() {
        if (markers.size >=3) {
            val points:ArrayList<LatLng> =ArrayList()
            for(marker in markers){
                points.add(marker.position)
            }
            drawStaticPolygon(points)
        }
    }

    private fun removePolygon() {
        currentPolygon?.remove()
    }

    override fun drawEditablePolygon(points: ArrayList<LatLng>) {
        for(point in points){
            drawPolygonMarker(point)
        }
    }

    override fun enableLocation() {
        hMap?.isMyLocationEnabled=true
    }

    override fun drawStaticPolygon(points: ArrayList<LatLng>){
        if(points.size<3) return
        hMap?.apply {
            val polygonOptions = PolygonOptions()
            for (point in points) {
                polygonOptions.add(point)
            }
            val context = requireContext()
            val colorFill = context.getColor(R.color.darkBlue)
            val colorStroke = context.getColor(R.color.lightBlue)
            polygonOptions.fillColor(colorFill)
            polygonOptions.strokeColor(colorStroke)
            currentPolygon?.remove()
            val polygon = addPolygon(polygonOptions)
            mListener?.onDrawnPolygon(polygon)
            currentPolygon = polygon
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onMapLongClick(latLng: LatLng?) {
        latLng?.let { drawPolygonMarker(it) }
    }

    override fun onMarkerDragStart(p0: Marker?) {
    }

    override fun onMarkerDrag(p0: Marker?) {
        p0?.let {
            drawPolygonWithMarkers()
        }
    }

    override fun onMarkerDragEnd(p0: Marker?) {
        p0?.let {
            drawPolygonWithMarkers()
        }
    }

    fun undo(){
        markers.last().let {
            markers.remove(it)
            removedMarkers.add(it.position)
            it.remove()
            drawPolygonWithMarkers()
        }

        fun redo(){
            if(removedMarkers.isNotEmpty()){
                drawPolygonMarker(removedMarkers.remove())
            }
        }
    }

    interface OnMapFragmentInteractionListener {
        fun onDrawnPolygon(polygon: Polygon)
        fun onMapReady()
    }
}