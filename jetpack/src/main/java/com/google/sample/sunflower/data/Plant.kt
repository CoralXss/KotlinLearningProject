package com.google.sample.sunflower.data

import java.util.*

data class Plant(
    val plantId: String,
    val name: String,
    val description: String,
    val growZoneNumber: Int,
    val wateringInterval: Int = 7,
    val imageUrl: String = ""
) {

    /**
     * Determines if the plant should be watered.  Returns true if [since]'s date > date of last
     * watering + watering Interval; false otherwise.
     */
    fun shouldBeWatered(since: Calendar, lastWateringDate: Calendar) =
        since > lastWateringDate.apply { add(Calendar.DAY_OF_YEAR, wateringInterval) }

    override fun toString(): String = name

    companion object {
        fun testData(): List<Plant> = listOf<Plant>(
            Plant("1",
                "Tomato",
                "Tomato with egg tastes Delicious !",
                0,
                3,
                "https://upload.wikimedia.org/wikipedia/commons/1/17/Cherry_tomatoes_red_and_green_2009_16x9.jpg")
        )
    }
}