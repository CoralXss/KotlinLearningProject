package com.google.sample.sunflower.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.sample.sunflower.data.Plant

class PlantDetailViewModel : ViewModel() {

    val isPlanted = true
    val plant = Plant("1",
        "Tomato",
        "Tomato with egg tastes Delicious !",
        0,
        3,
        "https://upload.wikimedia.org/wikipedia/commons/1/17/Cherry_tomatoes_red_and_green_2009_16x9.jpg")

    fun addPlantToGarden() {
        Log.e("Sunflower", "addPlantToGarden")
    }
}