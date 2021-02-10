package com.google.sample.sunflower.data

data class PlantAndGardenPlantings(
    val plant: Plant,

    val gardenPlantings: List<GardenPlanting> = emptyList()
)