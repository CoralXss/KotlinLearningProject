package com.google.sample.sunflower

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.sample.sunflower.adapters.PlantAdapter
import com.google.sample.sunflower.data.Plant
import com.google.sample.sunflower.databinding.FragmentPlantListBinding
import com.google.sample.sunflower.utils.getContentFromAsserts
import com.google.sample.sunflower.viewmodels.PlantListViewModel

class PlantListFragment : Fragment() {

    private val viewModel: PlantListViewModel = PlantListViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPlantListBinding.inflate(inflater, container, false)
        context ?: binding.root

        val adapter = PlantAdapter()
        binding.plantList.adapter = adapter
        subscribeUI(adapter)

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun subscribeUI(adapter: PlantAdapter) {
        // TODO: 2021/2/12 测试数据 
        val plants = Plant.testData()

//        val json = getContentFromAsserts(context, "plants.json")
//        val gsonType = object :TypeToken<List<Plant>>(){}.type
//        val plants = Gson().fromJson<List<Plant>>(json, gsonType)

        adapter.submitList(plants)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_plant_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_zone -> {
                updateData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateData() {

    }

}