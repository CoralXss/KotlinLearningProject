package com.google.sample.sunflower.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.sample.sunflower.data.PlantAndGardenPlantings
import com.google.sample.sunflower.repository.GardenPlantingRepository

class GardenPlantingListViewModel internal constructor(gardenPlantingRepository: GardenPlantingRepository): ViewModel() {

    val plantAndGardenPlantings: List<PlantAndGardenPlantings> = emptyList()

//    val plantAndGardenPlantings: LiveData<List<PlantAndGardenPlantings>> =
//        gardenPlantingRepository.getPlantedGardens().asLiveData()

}