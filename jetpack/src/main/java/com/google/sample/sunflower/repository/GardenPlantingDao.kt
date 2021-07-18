package com.google.sample.sunflower.repository

import androidx.room.*
import com.google.sample.sunflower.data.GardenPlanting
import com.google.sample.sunflower.data.PlantAndGardenPlantings
import kotlinx.coroutines.flow.Flow

@Dao
interface GardenPlantingDao {

    @Query("select * from garden_plantings")
    fun getGardenPlantings(): Flow<List<GardenPlanting>>

    @Query("select exists(select 1 from garden_plantings where plant_id = :plantId limit 1)")
    fun isPlanted(plantId: String): Flow<Boolean>

    @Transaction
    @Query("select * from plants where id in (select distinct(plant_id) from garden_plantings)")
    fun getPlantedGardens(): Flow<List<PlantAndGardenPlantings>>

    @Insert
    suspend fun insertGardenPlanting(gardenPlanting: GardenPlanting): Long

    @Delete
    suspend fun deleteGardenPlanting(gardenPlanting: GardenPlanting)
}