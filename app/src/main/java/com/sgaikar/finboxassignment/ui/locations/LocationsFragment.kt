package com.sgaikar.finboxassignment.ui.locations

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgaikar.finboxassignment.data.entities.LocationObj
import com.sgaikar.finboxassignment.databinding.LocationsFragmentBinding
import com.sgaikar.finboxassignment.ui.MainActivity
import com.sgaikar.finboxassignment.utils.Utils
import com.sgaikar.finboxassignment.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationsFragment : Fragment(), LocationsAdapter.LocationItemListener {
    private lateinit var mContext: Context
    private var binding: LocationsFragmentBinding by autoCleared()
    private val viewModel: LocationsViewModel by viewModels()
    private lateinit var adapter: LocationsAdapter

    var TAG = MainActivity::class.java.simpleName
    // Used in checking for runtime permissions.
    val  REQUEST_PERMISSIONS_REQUEST_CODE = 34

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
        Utils.createNotificationChannel(view.context)
        val appPerms = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        activityResultLauncher.launch(appPerms)

        if (!checkPermissions()) {
            requestPermissions1();
        }
    }

    /**
     * Returns the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        return PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
    }


    private fun requestPermissions1() {
        val shouldProvideRationale = shouldShowRequestPermissionRationale(
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            Toast.makeText(
                context,
                "Location permission is needed for core functionality",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }



    override fun onResume() {
        super.onResume()
        setupObservers()
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context;
    }

    private var activityResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { result ->
            var allAreGranted = true
            for(value in result.values) {
                allAreGranted = allAreGranted && value
            }

            if(allAreGranted) {
                viewModel.fetchLocationFromDevice()
            }
        }
}
