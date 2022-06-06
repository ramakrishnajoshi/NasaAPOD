package com.example.nasaapod.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.example.nasaapod.R
import com.example.nasaapod.databinding.FragmentDetailBinding
import com.example.nasaapod.di.factory.ViewModelFactory
import com.example.nasaapod.ui.data.ApodData
import com.example.nasaapod.ui.repo.MediaType
import com.example.nasaapod.ui.viewmodel.HomeViewModel
import com.example.nasaapod.utils.AppConstants.Companion.BUNDLE_KEY_SELECTED_APOD_DETAIL
import com.example.nasaapod.utils.AppConstants.Companion.YOUTUBE_PACKAGE_NAME
import com.example.nasaapod.utils.AppUtils
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
            if (apodData.mediaType == MediaType.VIDEO.type) {
                binding.imageViewMediaType.setImageResource(R.drawable.ic_type_video)
                binding.imageViewMediaType.setOnClickListener {
                    activity?.packageManager?.let {
                        val isYoutubeInstalled = AppUtils.isAppInstalled(it, YOUTUBE_PACKAGE_NAME)
                        if (isYoutubeInstalled) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(apodData.hdUrl)
                            intent.setPackage(YOUTUBE_PACKAGE_NAME)
                            startActivity(intent)
                        }
                    }
                }
                Glide
                    .with(binding.imageViewDetail)
                    .load(apodData.thumbnailUrl)
                    .placeholder(R.drawable.ic_type_video)
                    .priority(Priority.HIGH)
                    .into(binding.imageViewDetail)
            } else {
                Glide
                    .with(binding.imageViewDetail)
                    .load(apodData.hdUrl)
                    .placeholder(R.drawable.ic_type_image)
                    .priority(Priority.HIGH)
                    .into(binding.imageViewDetail)
                binding.imageViewMediaType.setImageResource(R.drawable.ic_type_image)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}