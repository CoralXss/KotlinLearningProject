package com.google.sample.sunflower.repository

import com.google.sample.sunflower.MainApplication
import com.google.sample.sunflower.data.AppDatabase

class PlantRepository constructor(private val plantDao: PlantDao) {

    companion object {
        // Double Check
        @Volatile
        private var instance: PlantRepository? = null

        fun getInstance(): PlantRepository {
            return instance ?: synchronized(this) {
                PlantRepository(AppDatabase.getInstance(MainApplication.INSTANCE).plantDao())
            }
        }
    }

    fun getPlants() = plantDao.getPlants()

    fun getPlant(plantId: String) = plantDao.getPlant(plantId)

    fun getPlantsWithGrowZoneNumber(growZoneNumber: Int) =
        plantDao.getPlantsWithGrowZoneNumber(growZoneNumber)
}