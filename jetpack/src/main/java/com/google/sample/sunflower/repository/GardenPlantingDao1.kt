package com.google.sample.sunflower.repository

import androidx.room.*
import com.google.sample.sunflower.data.GardenPlanting
import com.google.sample.sunflower.data.PlantAndGardenPlantings
import kotlinx.coroutines.flow.Flow

@Dao
interface GardenPlantingDao1 {
    // 获取植物列表
    @Query("SELECT * FROM garden_plantings")
    fun getGardenPlantings(): Flow<List<GardenPlanting>>

    // 是否已经种值了某种植物
    @Query("SELECT EXISTS(SELECT 1 FROM garden_plantings WHERE plant_id = :plantId LIMIT 1)")
    fun isPlanted(plantId: String): Flow<Boolean>

    // 获取花园已种值的列表
//    @Transaction
//    @Query("SELECT * FROM plants WHERE id IN (SELECT DISTINCT(plant_id) FROM garden_plantings)")
//    fun getPlantedGardens(): Flow<List<PlantAndGardenPlantings>>

    // 新增某个植物
    @Insert
    suspend fun insertGardenPlanting(gardenPlanting: GardenPlanting): Long

    // 删除某个植物
    @Delete
    suspend fun deleteGardenPlanting(gardenPlanting: GardenPlanting)
}