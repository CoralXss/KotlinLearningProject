package com.google.sample.sunflower.viewmodels

import androidx.lifecycle.*
import com.google.sample.sunflower.PlantListFragment
import com.google.sample.sunflower.data.Plant
import com.google.sample.sunflower.repository.PlantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class PlantListViewModel internal constructor(
    private var plantRepository: PlantRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    class PlantListViewModelFactory(
        private val plantRepository: PlantRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlantListViewModel::class.java)) {
                return PlantListViewModel(plantRepository, SavedStateHandle()) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        private const val TAG = "GardenPlantingListVM"

        private const val NO_GROW_ZONE = -1
        private const val GROW_ZONE_SAVED_STATE_KEY = "GROW_ZONE_SAVED_STATE_KEY"

        // 方法三：带参数构造
        fun create(fragment: PlantListFragment): PlantListViewModel {
            val repository = PlantRepository.getInstance()
            val factory = PlantListViewModelFactory(repository)
            return ViewModelProvider(fragment, factory).get(PlantListViewModel::class.java)
        }
    }

    private val growZone: MutableStateFlow<Int> = MutableStateFlow(
        savedStateHandle.get(GROW_ZONE_SAVED_STATE_KEY) ?: NO_GROW_ZONE
    )

    val plants: LiveData<List<Plant>> = growZone.flatMapLatest { zone ->
        if (zone == NO_GROW_ZONE) {
            plantRepository.getPlants()
        } else {
            plantRepository.getPlantsWithGrowZoneNumber(zone)
        }
    }.asLiveData()

    init {
        viewModelScope.launch {
            growZone.collect { newGrowZone ->
                savedStateHandle.set(GROW_ZONE_SAVED_STATE_KEY, newGrowZone)
            }
        }
    }

    fun setGrowZoneNumber(num: Int) {
        growZone.value = num
    }

    fun clearGrowZoneNumber() {
        growZone.value = NO_GROW_ZONE
    }

    fun isFiltered() = growZone.value != NO_GROW_ZONE
}