package com.google.sample.sunflower.repository

import com.google.sample.sunflower.data.GardenPlanting

class GardenPlantingRepository constructor(private val gardenPlantingDao: GardenPlantingDao) {

    suspend fun createGardenPlanting(plantId: String) {
        val gardenPlanting = GardenPlanting(plantId)
        gardenPlantingDao.insertGardenPlanting(gardenPlanting)
    }

    suspend fun removeGardenPlanting(gardenPlanting: GardenPlanting) {
        gardenPlantingDao.deleteGardenPlanting(gardenPlanting)
    }

    fun isPlanted(plantId: String) = gardenPlantingDao.isPlanted(plantId)

    fun getPlantedGardens() = gardenPlantingDao.getPlantedGardens()


    companion object {
        // 单例模式
        private var sInstance: GardenPlantingRepository? = null
            get() {
                return field ?: GardenPlantingRepository(GardenPlantingDao())
            }

        @JvmStatic
        @Synchronized
        fun getInstance(): GardenPlantingRepository {
            return requireNotNull(sInstance)
        }
    }

}