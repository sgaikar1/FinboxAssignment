package com.sgaikar.finboxassignment.ui.locations

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.sgaikar.finboxassignment.data.entities.LocationObj
import com.sgaikar.finboxassignment.databinding.LocationsFragmentBinding
import com.sgaikar.finboxassignment.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationsFragment : Fragment(), LocationsAdapter.LocationItemListener {
    private var binding: LocationsFragmentBinding by autoCleared()
    private val viewModel: LocationsViewModel by viewModels()
    private lateinit var adapter: LocationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocationsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        if (ActivityCompat.checkSelfPermission(view.context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getFLLocation()
        }
    }

    override fun onResume() {
        super.onResume()
        setupObservers()
    }

    private fun getFLLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null){
                 var location = LocationObj(System.currentTimeMillis(), location.latitude, location.longitude)
                viewModel.insertLocationInfo(location)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = LocationsAdapter(this)
        binding.locationRv.layoutManager = LinearLayoutManager(requireContext())
        binding.locationRv.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.fetchLocationData()
        viewModel.locationFinalList.observe(viewLifecycleOwner,
            Observer<MutableList<LocationObj>> { it ->
                binding.progressBar.visibility = View.GONE
                if (!it.isNullOrEmpty()) adapter.setItems(ArrayList(it))
            }
        )
    }

    override fun onClickedLocation(location: LocationObj) {

    }

}
