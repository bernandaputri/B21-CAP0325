package com.arjuna.sipiapp.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PredictResponse(
    @field:SerializedName("minor_building")
    @Expose
    var minorBuilding: String,

    @field:SerializedName("heavy_bridge")
    @Expose
    var heavyBridge: String,

    @field:SerializedName("moderate_bridge")
    @Expose
    var moderateBridge: String,

    @field:SerializedName("minor_bridge")
    @Expose
    var minorBridge: String,

    @field:SerializedName("heavy_building")
    @Expose
    var heavyBuilding: String,

    @field:SerializedName("heavy_road")
    @Expose
    var heavyRoad: String,

    @field:SerializedName("moderate_road")
    @Expose
    var moderateRoad: String,

    @field:SerializedName("moderate_building")
    @Expose
    var moderateBuilding: String,

    @field:SerializedName("minor_road")
    @Expose
    var minorRoad: String
)