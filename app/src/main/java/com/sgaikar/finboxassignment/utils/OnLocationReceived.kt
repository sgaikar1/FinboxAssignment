package com.sgaikar.finboxassignment.utils

import com.sgaikar.finboxassignment.data.entities.LocationObj


interface OnLocationReceived {
    fun onLocation(location: LocationObj)
}