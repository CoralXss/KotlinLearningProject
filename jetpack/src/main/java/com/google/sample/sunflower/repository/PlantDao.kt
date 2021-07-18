package com.google.sample.sunflower.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.sample.sunflower.data.Plant
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {

    @Query("select * from plants order by name")
    fun getPlants(): Flow<List<Plant>>

    @Query("select * from plants where growZoneNumber = :growZoneNumber order by name")
    fun getPlantsWithGrowZoneNumber(growZoneNumber: Int): Flow<List<Plant>>

    @Query("select * from plants where id = :plantId")
    fun getPlant(plantId: String): Flow<Plant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<Plant>)
}