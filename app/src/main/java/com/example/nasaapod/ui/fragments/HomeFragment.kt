package com.example.nasaapod.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nasaapod.R
import com.example.nasaapod.databinding.FragmentHomeBinding
import com.example.nasaapod.di.factory.ViewModelFactory
import com.example.nasaapod.ui.ApodClickListener
import com.example.nasaapod.ui.HomeAdapter
import com.example.nasaapod.ui.data.ApodData
import com.example.nasaapod.ui.data.ApodViewState
import com.example.nasaapod.ui.viewmodel.HomeViewModel
import com.example.nasaapod.utils.AppConstants.Companion.API_DATE_FORMAT
import com.example.nasaapod.utils.AppConstants.Companion.BUNDLE_KEY_SELECTED_APOD_DETAIL
import com.example.nasaapod.utils.AppUtils
import com.example.nasaapod.utils.ErrorType
import com.example.nasaapod.utils.VerticalSpaceItemDecoration
import com.example.nasaapod.utils.network.ConnectionLiveData
import com.example.nasaapod.utils.network.NetworkHelper
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.android.support.DaggerFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HomeFragment : DaggerFragment(), ApodClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val viewModel by viewModels<HomeViewModel>({this.requireActivity()}) {
        viewModelFactory
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeAdapter = HomeAdapter(this)
    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        activity?.let { it.title = getString(R.string.home_fragment) }
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_bar_menu, menu)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_date_picker -> {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText(getString(R.string.select_date))
                        .setCalendarConstraints(
                            CalendarConstraints.Builder()
                                .setValidator(DateValidatorPointBackward.now()).build()
                        )
                        .build()
                datePicker.addOnPositiveButtonClickListener {
                    val formattedDate = AppUtils.convertMillisToStringDate(it)
                    activity?.let { it.title = getString(R.string.home_fragment) }
                    viewModel.getContentByDatePeriodFromDatabase(formattedDate, formattedDate, true)
                }
                datePicker.show(this.childFragmentManager, "datePicker")
            }
            R.id.menu_item_clear_date -> {
                homeAdapter.submitList(null)
                viewModel.getContentByDatePeriodFromDatabase(
                    AppUtils.getSevenDaysBackDate(),
                    AppUtils.getCurrentDate(),
                    true
                )
            }
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleNetworkAvailability()
        configurePullToRefresh()
        setRecyclerView()

        viewModel.datePeriodLiveData.observe(viewLifecycleOwner, Observer { viewState ->
            processApiResponse(viewState)
        })
        viewModel.getContentByDatePeriodFromDatabase( AppUtils.getSevenDaysBackDate(), AppUtils.getCurrentDate())

        binding.buttonStateAction.setOnClickListener {
            viewModel.getContentByDatePeriodFromDatabase(AppUtils.getSevenDaysBackDate(), AppUtils.getCurrentDate())
        }
    }

    /**
     * Allows us to update the network availability of the HomeViewModel thanks to a live data, which
     * listen to network modification.
     */
    private fun handleNetworkAvailability() {
        activity?.let {
            connectionLiveData = ConnectionLiveData(it)
            connectionLiveData.observe(viewLifecycleOwner) { hasNetworkAccess: Boolean ->
                viewModel.setNetworkAvailability(hasNetworkAccess)
            }
            viewModel.setNetworkAvailability(NetworkHelper.isOnline(it))
        }
    }

    private fun configurePullToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getContentByDatePeriodFromDatabase(AppUtils.getSevenDaysBackDate(), AppUtils.getCurrentDate())
        }
    }

    private fun processApiResponse(viewState: ApodViewState) {
        when(viewState) {
            ApodViewState.ShowLoading -> {
                showLoadingViewState()
            }
            is ApodViewState.Success -> {
                hideLoadingViewState()
                showSuccessViewState(viewState.data)
            }
            ApodViewState.NoData -> {
                hideLoadingViewState()
            }
            is ApodViewState.Error -> {
                hideLoadingViewState()
                showErrorViewState(viewState.errorType, viewState.message)
            }
        }

        if (binding.swipeRefreshLayout.isRefreshing) {
            binding.swipeRefreshLayout.isRefreshing = false
        }
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

    private fun showLoadingViewState() {
        with(binding) {
            recyclerViewPhotos.visibility = View.GONE
            linearLayoutStateContainer.visibility = View.VISIBLE
            imageViewState.setAnimation(R.raw.lottile_loading)
            textViewStateTitle.text = getString(R.string.home_activity_loading_title)
            textViewStateDescription.text = getString(R.string.home_activity_loading_description)
            buttonStateAction.visibility = View.GONE
        }
    }

    private fun showErrorViewState(errorType: ErrorType, message: String) {
        with(binding) {
            recyclerViewPhotos.visibility = View.GONE
            linearLayoutStateContainer.visibility = View.VISIBLE

            when(errorType) {
                ErrorType.NO_INTERNET_CONNECTION -> {
                    imageViewState.setAnimation(R.raw.lottie_no_internet)
                    imageViewState.playAnimation()
                    buttonStateAction.text = getString(R.string.button_text_retry)
                }
                else -> {
                    imageViewState.setAnimation(R.raw.lottie_generic_error)
                    imageViewState.playAnimation()
                }
            }
            textViewStateTitle.text = errorType.name
            textViewStateDescription.text = message
            buttonStateAction.visibility = View.VISIBLE
        }
    }

    private fun hideLoadingViewState() {
        binding.recyclerViewPhotos.visibility = View.VISIBLE
        binding.linearLayoutStateContainer.visibility = View.GONE
    }

    private fun showSuccessViewState(data: List<ApodData>) {
        homeAdapter.submitList(data)
    }

    override fun onApodItemClick(apodData: ApodData) {
        val bundle = bundleOf(
            BUNDLE_KEY_SELECTED_APOD_DETAIL to apodData
        )
        findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
    }

    override fun onApodFavouriteIconClick(apodData: ApodData) {
        viewModel.setFavorite(apodData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}