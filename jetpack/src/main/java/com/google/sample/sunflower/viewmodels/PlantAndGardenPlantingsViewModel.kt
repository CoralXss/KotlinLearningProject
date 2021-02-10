package com.google.sample.sunflower.viewmodels

import com.google.sample.sunflower.data.PlantAndGardenPlantings
import java.text.SimpleDateFormat
import java.util.*

class PlantAndGardenPlantingsViewModel(plantings: PlantAndGardenPlantings) {
    private val plant = checkNotNull(plantings.plant)

    private val gardenPlanting = plantings.gardenPlantings[0]

    val waterDateString: String = dataFormat.format(gardenPlanting.lastWateringDate.time)

    val wateringInterval
        get() = plant.wateringInterval

    val imageUrl
        get() = plant.imageUrl

    val plantName
        get() = plant.name

    val plantDateString: String
        get() = dataFormat.format(gardenPlanting.plantDate.time)

    val plantId
        get() = plant.plantId

    companion object {
        private val dataFormat = SimpleDateFormat("MMM d, yyyy", Locale.CHINA)
    }
}