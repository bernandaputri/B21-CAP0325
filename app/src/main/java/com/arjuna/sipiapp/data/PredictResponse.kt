package com.arjuna.sipiapp.data

import com.google.gson.annotations.SerializedName

data class PredictResponse(
    @field:SerializedName("minor_building")
    val minorBuilding: Double,

    @field:SerializedName("heavy_bridge")
    val heavyBridge: Double,

    @field:SerializedName("moderate_bridge")
    val moderateBride: Double,

    @field:SerializedName("minor_bridge")
    val minorBridge: Double,

    @field:SerializedName("heavy_building")
    val heavyBuilding: Double,

    @field:SerializedName("heavy_road")
    val heavyRoad: Double,

    @field:SerializedName("moderate_road")
    val moderateRoad: Double,

    @field:SerializedName("moderate_building")
    val moderateBuilding: Double,

    @field:SerializedName("minor_road")
    val minorRoad: Double
)