package com.example.nasaapod.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.nasaapod.R
import com.example.nasaapod.databinding.FragmentDetailBinding
import com.example.nasaapod.di.factory.ViewModelFactory
import com.example.nasaapod.ui.data.ApodData
import com.example.nasaapod.ui.viewmodel.HomeViewModel
import com.example.nasaapod.utils.AppConstants.Companion.BUNDLE_KEY_SELECTED_APOD_DETAIL
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class DetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val viewModel by viewModels<HomeViewModel>({this.requireActivity()}) {
        viewModelFactory
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apodDetail = arguments?.getParcelable<ApodData>(BUNDLE_KEY_SELECTED_APOD_DETAIL)
        apodDetail?.let { apodData ->
            binding.textViewPhotoDate.text = getString(R.string.apod_detail_page_photo, apodData.date)
            binding.textViewPhotoExplanationTitle.text = apodData.title
            binding.textViewPhotoExplanation.text = apodData.explanation
            activity?.let {
                it.title = getString(R.string.apod_detail_page_title)
            }
            Glide.with(binding.imageViewDetail).load(apodData.hdUrl).into(binding.imageViewDetail)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}