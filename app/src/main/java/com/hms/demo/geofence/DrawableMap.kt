package com.hms.demo.geofence

import com.huawei.hms.maps.model.LatLng

interface DrawableMap {
    fun drawMarker(latLng: LatLng?)
    fun draw(points: ArrayList<LatLng>)
}