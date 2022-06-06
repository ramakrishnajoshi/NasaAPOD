package com.example.nasaapod.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nasaapod.R
import com.example.nasaapod.databinding.FragmentFavouriteBinding
import com.example.nasaapod.di.factory.ViewModelFactory
import com.example.nasaapod.ui.ApodClickListener
import com.example.nasaapod.ui.HomeAdapter
import com.example.nasaapod.ui.data.ApodData
import com.example.nasaapod.ui.viewmodel.HomeViewModel
import com.example.nasaapod.utils.AppConstants.Companion.BUNDLE_KEY_SELECTED_APOD_DETAIL
import com.example.nasaapod.utils.AppUtils
import com.example.nasaapod.utils.VerticalSpaceItemDecoration
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class FavouriteFragment : DaggerFragment(), ApodClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val viewModel by viewModels<HomeViewModel>({this.requireActivity()}) {
        viewModelFactory
    }

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private val homeAdapter = HomeAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val view = binding.root
        activity?.let {
            it.title = getString(R.string.favorite_fragment_toolbar_title)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        viewModel.favouriteApodsLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                showNoFavouritesViewState()
            } else {
                homeAdapter.submitList(it)
            }
        })
        viewModel.getFavoriteApodList()
    }

    private fun setRecyclerView() {
        binding.recyclerViewPhotos.apply{
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = homeAdapter
            addItemDecoration(
                VerticalSpaceItemDecoration(
                    AppUtils.dp(requireActivity().applicationContext, 5f)
                )
            )
        }
    }

    private fun showNoFavouritesViewState() {
        with(binding) {
            recyclerViewPhotos.visibility = View.GONE
            linearLayoutStateContainer.visibility = View.VISIBLE
            imageViewState.setAnimation(R.raw.lottie_no_favourites_found)
            imageViewState.playAnimation()
            textViewStateTitle.text = getString(R.string.no_favourites_found)
            textViewStateDescription.visibility = View.GONE
            buttonStateAction.visibility = View.GONE
        }
    }

    override fun onApodItemClick(apodData: ApodData) {
        val bundle = bundleOf(
            BUNDLE_KEY_SELECTED_APOD_DETAIL to apodData
        )
        findNavController().navigate(R.id.action_favouriteFragment_to_detailFragment, bundle)
    }

    override fun onApodFavouriteIconClick(apodData: ApodData) {
        viewModel.setFavorite(apodData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}