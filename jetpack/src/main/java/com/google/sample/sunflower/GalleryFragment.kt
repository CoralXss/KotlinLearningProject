package com.google.sample.sunflower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.sample.sunflower.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private val args: GalleryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGalleryBinding.inflate(inflater, container, false)
        context ?: return binding.root

        Toast.makeText(context, args.plantName, Toast.LENGTH_SHORT).show()

        return binding.root
    }

    private fun search(query: String) {

    }
}