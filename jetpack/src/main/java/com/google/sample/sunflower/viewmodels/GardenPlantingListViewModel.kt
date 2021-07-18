package com.google.sample.sunflower.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.google.sample.sunflower.GardenFragment
import com.google.sample.sunflower.data.PlantAndGardenPlantings
import com.google.sample.sunflower.repository.GardenPlantingRepository

class GardenPlantingListViewModel internal constructor(
    gardenPlantingRepository: GardenPlantingRepository
): ViewModel() {

    companion object {
        const val TAG = "GardenPlantingListVM"

        // 方法三：带参数构造
        fun create(fragment: GardenFragment): GardenPlantingListViewModel {
            val repository = GardenPlantingRepository.getInstance()
            val factory = GardenPlantingListViewModelFactory(repository)
            return ViewModelProvider(fragment, factory).get(GardenPlantingListViewModel::class.java)
        }
    }

    init {
        Log.i(TAG, "inited")
    }

    val plantAndGardenPlantings: LiveData<List<PlantAndGardenPlantings>> =
        gardenPlantingRepository.getPlantedGardens().asLiveData()

    class GardenPlantingListViewModelFactory(
        private val gardenPlantingRepository: GardenPlantingRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GardenPlantingListViewModel::class.java)) {
                return GardenPlantingListViewModel(gardenPlantingRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}