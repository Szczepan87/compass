package com.example.compass.ui

import android.app.Dialog
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.compass.LocationProvider
import com.example.compass.R
import com.example.compass.databinding.DialogCoordinatesBinding
import com.example.compass.vm.CompassActivityViewModel

class CoordinatesDialog(private val compassActivityViewModel: CompassActivityViewModel) :
    DialogFragment() {

    private lateinit var binding: DialogCoordinatesBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_coordinates,
            null,
            false
        )
        setUpBinding(binding)
        return AlertDialog.Builder(requireActivity()).setView(binding.root).create()
    }

    private fun setUpBinding(binding: DialogCoordinatesBinding) {
        binding.coordinateDialogCancelButton.setOnClickListener { this.dismiss() }
        binding.coordinateDialogOkButton.setOnClickListener {
            val destination = Location("").apply {
                longitude = binding.longitudeEditText.text.toString().toDouble()
                latitude = binding.latitudeEditText.text.toString().toDouble()
            }
            compassActivityViewModel.updateDestinationLocation(destination)
            this.dismiss()
        }
    }
}