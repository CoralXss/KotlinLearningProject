package com.google.sample.sunflower.data

import java.util.*

data class GardenPlanting(
    val plantId: String,
    val plantDate: Calendar = Calendar.getInstance(),
    val lastWateringDate: Calendar = Calendar.getInstance()
) {
    var gardenPlantingId: Long = 0
}