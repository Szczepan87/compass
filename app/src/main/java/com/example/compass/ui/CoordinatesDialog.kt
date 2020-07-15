package com.example.compass.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
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
        return AlertDialog.Builder(requireActivity()).setView(binding.root).create()
    }
}