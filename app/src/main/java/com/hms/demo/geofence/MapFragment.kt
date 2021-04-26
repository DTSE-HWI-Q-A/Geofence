package com.hms.demo.geofence

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hms.demo.geofence.databinding.MapBinding
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.MarkerOptions
import com.huawei.hms.maps.model.PolygonOptions


class MapFragment : Fragment(), OnMapReadyCallback,HuaweiMap.OnMapLongClickListener,DrawableMap {

    var binding:MapBinding?=null
    var hMap:HuaweiMap?=null
    private var mapView:MapView?=null
    var mListener: OnMapFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return MapBinding.inflate(inflater).also { binding=it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mapView=binding?.mapView
        mapView?.apply {
            onCreate(null)
            getMapAsync(this@MapFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnMapFragmentInteractionListener)
            this.mListener=context
    }

    override fun onMapReady(hMap: HuaweiMap?) {
        hMap?.setOnMapLongClickListener(this)
        this.hMap=hMap


    }

    override fun drawMarker(latLng: LatLng?) {
        hMap?.let {
            val options=MarkerOptions()
            options.position(latLng)
            it.addMarker(options)

        }
    }

    override fun draw(points: ArrayList<LatLng>) {
        hMap?.apply {
            clear()
            if (points.size >=3 /*Minimum amount of points to draw a polygon*/) {
                val polygonOptions = PolygonOptions()
                polygonOptions.addAll(points)
                val context=requireContext()
                val colorFill=context.getColor(R.color.darkBlue)
                val colorStroke=context.getColor(R.color.lightBlue)
                polygonOptions.fillColor(colorFill)
                polygonOptions.strokeColor(colorStroke)
                addPolygon(polygonOptions)
            } else {
                for (position in points) {
                    drawMarker(position)
                }
            }
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
        //drawMarker(latLng)
        mListener?.onMapLongClick(latLng)
    }

    interface OnMapFragmentInteractionListener{
        fun onMapLongClick(latLng: LatLng?)
    }


}