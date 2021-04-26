package com.hms.demo.geofence

import android.app.Application
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.common.util.AGCUtils
import com.huawei.hms.maps.MapsInitializer

class MyApplication: Application() {
    override fun onCreate() {
        AGConnectServicesConfig.fromContext(this).getString("client/api_key").let {
            MapsInitializer.setApiKey(it)
        }
        super.onCreate()
    }
}