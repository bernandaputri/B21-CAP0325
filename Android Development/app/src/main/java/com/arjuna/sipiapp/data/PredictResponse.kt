package com.arjuna.sipiapp.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PredictResponse(
    @field:SerializedName("minor_building")
    @Expose
    val minorBuilding: Double,

    @field:SerializedName("heavy_bridge")
    @Expose
    val heavyBridge: Double,

    @field:SerializedName("moderate_bridge")
    @Expose
    val moderateBridge: Double,

    @field:SerializedName("minor_bridge")
    @Expose
    val minorBridge: Double,

    @field:SerializedName("heavy_building")
    @Expose
    val heavyBuilding: Double,

    @field:SerializedName("heavy_road")
    @Expose
    val heavyRoad: Double,

    @field:SerializedName("moderate_road")
    @Expose
    val moderateRoad: Double,

    @field:SerializedName("moderate_building")
    @Expose
    val moderateBuilding: Double,

    @field:SerializedName("minor_road")
    @Expose
    val minorRoad: Double
)