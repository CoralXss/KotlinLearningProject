package com.google.sample.sunflower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.google.sample.sunflower.adapters.GardenPlantingAdapter
import com.google.sample.sunflower.adapters.PLANT_LIST_PAGE_INDEX
import com.google.sample.sunflower.databinding.FragmentGardenBinding
import com.google.sample.sunflower.viewmodels.GardenPlantingListViewModel

class GardenFragment : Fragment() {

    private lateinit var binding: FragmentGardenBinding

    // 写法二：此为结合 Navigation 一起使用的
//    val viewModel: GardenPlantingListViewModel by viewModels()

    private lateinit var viewModel: GardenPlantingListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGardenBinding.inflate(inflater, container, false)

        // 写法一：无参构造
//        viewModel = ViewModelProvider(this).get(GardenPlantingListViewModel::class.java)

        // 写法三：有参构造
        viewModel = GardenPlantingListViewModel.create(this)

        val adapter = GardenPlantingAdapter()
        binding.gardenList.adapter = adapter

        binding.addPlant.setOnClickListener {
            navigateToPlantListPage()
        }

        subscribeUI(adapter, binding)

        return binding.root
    }

    // TODO: 2021/2/10 1）使用 Repository & LiveData 管理数据
    private fun subscribeUI(adapter: GardenPlantingAdapter, binding: FragmentGardenBinding) {
        viewModel.plantAndGardenPlantings.observe(viewLifecycleOwner) { result ->
            binding.hasPlantings = !result.isNullOrEmpty()
            adapter.submitList(result)
        }
    }

    private fun navigateToPlantListPage() {
        requireActivity().findViewById<ViewPager2>(R.id.view_pager).currentItem = PLANT_LIST_PAGE_INDEX
    }
}