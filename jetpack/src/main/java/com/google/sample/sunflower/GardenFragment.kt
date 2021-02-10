package com.google.sample.sunflower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.sample.sunflower.adapters.GardenPlantingAdapter
import com.google.sample.sunflower.adapters.PLANT_LIST_PAGE_INDEX
import com.google.sample.sunflower.databinding.FragmentGardenBinding
import com.google.sample.sunflower.viewmodels.GardenPlantingListViewModel

class GardenFragment : Fragment() {

    private lateinit var binding: FragmentGardenBinding

    private val viewModel: GardenPlantingListViewModel = GardenPlantingListViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGardenBinding.inflate(inflater, container, false)

        val adapter = GardenPlantingAdapter()
        binding.gardenList.adapter = adapter

        binding.addPlant.setOnClickListener {
            navigateToPlantListPage()
        }

        subscribeUI(adapter, binding)

        return binding.root
    }

    private fun subscribeUI(adapter: GardenPlantingAdapter, binding: FragmentGardenBinding) {
        binding.hasPlantings = !viewModel.plantAndGardenPlantings.isEmpty()

        adapter.submitList(viewModel.plantAndGardenPlantings)
    }

    private fun navigateToPlantListPage() {
        requireActivity().findViewById<ViewPager2>(R.id.view_pager).currentItem = PLANT_LIST_PAGE_INDEX
    }
}