package com.hms.demo.geofence

import com.huawei.hms.maps.model.LatLng

interface DrawableMap {
    fun enableLocation()
    fun drawLocationMarker(latLng: LatLng)
    fun drawPolygonMarker(latLng: LatLng)
    fun drawStaticPolygon(points: ArrayList<LatLng>)
    fun drawEditablePolygon(points: ArrayList<LatLng>)

}