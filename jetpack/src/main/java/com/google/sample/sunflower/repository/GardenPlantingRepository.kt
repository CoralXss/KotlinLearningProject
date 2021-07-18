package com.google.sample.sunflower.repository

import android.content.Context
import com.google.sample.sunflower.MainApplication
import com.google.sample.sunflower.data.AppDatabase
import com.google.sample.sunflower.data.GardenPlanting

class GardenPlantingRepository constructor(private val gardenPlantingDao: GardenPlantingDao) {

    companion object {
        // Double Check
        @Volatile
        private var instance: GardenPlantingRepository? = null

        fun getInstance(): GardenPlantingRepository {
            return instance ?: synchronized(this) {
                GardenPlantingRepository(AppDatabase.getInstance(MainApplication.INSTANCE).gardenPlantingDao())
            }
        }
    }

    suspend fun createGardenPlanting(plantId: String) {
        val gardenPlanting = GardenPlanting(plantId)
        gardenPlantingDao.insertGardenPlanting(gardenPlanting)
    }

    suspend fun removeGardenPlanting(gardenPlanting: GardenPlanting) {
        gardenPlantingDao.deleteGardenPlanting(gardenPlanting)
    }

    fun isPlanted(plantId: String) = gardenPlantingDao.isPlanted(plantId)

    fun getPlantedGardens() = gardenPlantingDao.getPlantedGardens()
}