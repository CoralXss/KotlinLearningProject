package com.google.sample.sunflower

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ShareCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.sample.sunflower.data.Plant
import com.google.sample.sunflower.databinding.FragmentPlantDetailBinding
import com.google.sample.sunflower.viewmodels.PlantDetailViewModel
import kotlinx.android.synthetic.main.fragment_plant_detail.*

class PlantDetailFragment : Fragment() {

    private val plantDetailViewModel = PlantDetailViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPlantDetailBinding>(
            inflater,
            R.layout.fragment_plant_detail,
            container,
            false
        ).apply {
            viewModel = plantDetailViewModel
        }

        binding.galleryNav.setImageResource(R.drawable.ic_photo_library)

        initClickListener(binding)

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun initClickListener(binding: FragmentPlantDetailBinding) {
        binding.fab.setOnClickListener {
            hideAppbarFab(binding.fab)
            plantDetailViewModel.addPlantToGarden()
            Snackbar.make(binding.root, "添加了新植物", Snackbar.LENGTH_LONG).show()
        }

        binding.galleryNav.setOnClickListener {
            navigateToGallery()
        }

        var isToolbarShown = false
        binding.plantDetailScrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            val shouldShowToolbar = scrollY > binding.toolbar.height

            if (isToolbarShown != shouldShowToolbar) {

                isToolbarShown = shouldShowToolbar

                appbar.isActivated = shouldShowToolbar

                binding.toolbarLayout.isTitleEnabled = shouldShowToolbar
            }
        })

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_share -> {
                    true
                }
                else -> false
            }
        }
    }

    private fun hideAppbarFab(fab: FloatingActionButton) {
        val params = fab.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as FloatingActionButton.Behavior
        behavior.isAutoHideEnabled = false
        fab.hide()
    }

    private fun navigateToGallery() {
        plantDetailViewModel.plant.let { plant ->
            val direction = PlantDetailFragmentDirections.actionPlantDetailFragmentToGalleryFragment(plant.name)
            findNavController().navigate(direction)
        }
    }

    private fun createShareIntent() {
        val shareText = plantDetailViewModel.plant.let { plant ->
            if (plant == null) {
                ""
            } else {
                "在安卓 Sunflower APP 上看看这" + plant.name
            }
        }

        val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
            .setText(shareText)
            .setType("text/plain")
            .createChooserIntent()
            .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        startActivity(shareIntent)
    }

    // 函数式 SAM 接口
//    interface Callback {
//        fun add(plant: Plant?)
//    }
}