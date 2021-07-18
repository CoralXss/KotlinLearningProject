package com.google.sample.sunflower

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import com.google.sample.sunflower.adapters.PlantAdapter
import com.google.sample.sunflower.databinding.FragmentPlantListBinding
import com.google.sample.sunflower.viewmodels.PlantListViewModel

class PlantListFragment : Fragment() {

    private lateinit var viewModel: PlantListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPlantListBinding.inflate(inflater, container, false)
        context ?: binding.root

//        viewModel = ViewModelProvider(this,
//            SavedStateViewModelFactory(activity!!.application, this))
//            .get(PlantListViewModel::class.java)

        viewModel = PlantListViewModel.create(this)

        val adapter = PlantAdapter()
        binding.plantList.adapter = adapter
        subscribeUI(adapter)

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun subscribeUI(adapter: PlantAdapter) {
        // TODO: 2021/2/12 测试数据 
//        val plants = Plant.testData()
//        adapter.submitList(plants)

        viewModel.plants.observe(viewLifecycleOwner) { plants ->
            adapter.submitList(plants)
        }
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
        with(viewModel) {
            if (isFiltered()) {
                clearGrowZoneNumber()
            } else {
                setGrowZoneNumber(9)  // PlantListViewModel 类的方法
            }
        }
    }

}