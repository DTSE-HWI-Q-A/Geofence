package com.hms.demo.geofence

import androidx.lifecycle.ViewModel
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Marker

class MapViewModel: ViewModel(), HuaweiMap.OnMapLongClickListener,HuaweiMap.OnMarkerDragListener{
    override fun onMapLongClick(p0: LatLng?) {
        TODO("Not yet implemented")
    }

    override fun onMarkerDragStart(p0: Marker?) {
        TODO("Not yet implemented")
    }

    override fun onMarkerDrag(p0: Marker?) {
        TODO("Not yet implemented")
    }

    override fun onMarkerDragEnd(p0: Marker?) {
        TODO("Not yet implemented")
    }


}