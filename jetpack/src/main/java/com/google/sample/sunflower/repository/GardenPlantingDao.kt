package com.google.sample.sunflower.repository

import com.google.sample.sunflower.data.GardenPlanting
import com.google.sample.sunflower.data.PlantAndGardenPlantings
import kotlinx.coroutines.flow.Flow

class GardenPlantingDao {

    fun getGardenPlantings(): Flow<List<GardenPlanting>> {
        TODO()
    }

    fun isPlanted(plantId: String): Flow<Boolean> {
        TODO()
    }

    fun getPlantedGardens(): Flow<List<PlantAndGardenPlantings>> {
        TODO()
    }

    suspend fun insertGardenPlanting(gardenPlanting: GardenPlanting): Long {
        TODO()
    }

    suspend fun deleteGardenPlanting(gardenPlanting: GardenPlanting) {
        TODO()
    }
}