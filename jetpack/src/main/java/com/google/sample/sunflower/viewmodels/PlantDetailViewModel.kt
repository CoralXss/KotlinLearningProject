package com.google.sample.sunflower.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.sample.sunflower.PlantDetailFragment
import com.google.sample.sunflower.repository.GardenPlantingRepository
import com.google.sample.sunflower.repository.PlantRepository
import kotlinx.coroutines.launch

class PlantDetailViewModel constructor(
    plantRepository: PlantRepository,
    private val gardenPlantingRepository: GardenPlantingRepository,
    private val plantId: String
) : ViewModel() {

    val isPlanted = gardenPlantingRepository.isPlanted(plantId).asLiveData()

    val plant = plantRepository.getPlant(plantId).asLiveData()

    fun addPlantToGarden() {
        Log.e("Sunflower", "addPlantToGarden")
        viewModelScope.launch {
            gardenPlantingRepository.createGardenPlanting(plantId)
        }
    }

    companion object {
        private const val TAG = "PlantDetailVM"

        // 方法三：带参数构造
        fun create(fragment: PlantDetailFragment, plantId: String): PlantDetailViewModel {
            val plantRepository = PlantRepository.getInstance()
            val gardenRepository = GardenPlantingRepository.getInstance()
            val factory = PlantDetailViewModelFactory(plantRepository, gardenRepository, plantId)
            return ViewModelProvider(fragment, factory).get(PlantDetailViewModel::class.java)
        }
    }

    class PlantDetailViewModelFactory(
        private val plantRepository: PlantRepository,
        private val gardenPlantingRepository: GardenPlantingRepository,
        private val pantId: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlantDetailViewModel::class.java)) {
                return PlantDetailViewModel(
                    plantRepository,
                    gardenPlantingRepository,
                    pantId
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}