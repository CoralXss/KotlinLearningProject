package com.google.sample.sunflower.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.sample.sunflower.data.PlantAndGardenPlantings

class GardenPlantingListViewModel : ViewModel() {

//    val plantAndGardenPlantings: LiveData<List<PlantAndGardenPlantings>> = emptyList<PlantAndGardenPlantings>()

    val plantAndGardenPlantings: List<PlantAndGardenPlantings> = emptyList()
}