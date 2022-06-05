package com.example.nasaapod.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.nasaapod.R
import com.example.nasaapod.di.factory.ViewModelFactory
import com.example.nasaapod.ui.data.ApodViewState
import com.example.nasaapod.ui.viewmodel.HomeViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val viewModel by viewModels<HomeViewModel>({this.requireActivity()}) {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTodayContent().observe(viewLifecycleOwner, Observer { viewState ->
            Log.e("DATA", viewState.toString())
            when(viewState) {
                ApodViewState.ShowLoading -> {

                }
                is ApodViewState.Success -> {

                }
                ApodViewState.NoData -> {

                }
                is ApodViewState.Error -> {

                }
            }
        })
    }
}