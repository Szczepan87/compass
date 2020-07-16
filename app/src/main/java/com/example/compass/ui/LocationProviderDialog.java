package com.example.compass.ui;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.compass.R;
import com.example.compass.databinding.DialogCoordinatesBinding;
import com.example.compass.util.providers.LocationProvider;
import com.example.compass.vm.CompassActivityViewModel;

import java.util.Objects;

public class LocationProviderDialog extends DialogFragment {
    private CompassActivityViewModel compassActivityViewModel;
    private DialogCoordinatesBinding binding;

    public LocationProviderDialog(CompassActivityViewModel compassActivityViewModel) {
        this.compassActivityViewModel = compassActivityViewModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.dialog_coordinates, null, false);
        setUpBinding(binding);
        return new AlertDialog.Builder(requireActivity()).setView(binding.getRoot()).create();
    }

    private void setUpBinding(final DialogCoordinatesBinding binding) {
        binding.coordinateDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.coordinateDialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = new Location("");
                double latitude = Double.parseDouble(Objects.requireNonNull(binding.latitudeEditText.getText()).toString());
                double longitude = Double.parseDouble(Objects.requireNonNull(binding.longitudeEditText.getText()).toString());
                location.setLatitude(latitude);
                location.setLongitude(longitude);
                compassActivityViewModel.updateDestinationLocation(location);
                dismiss();
            }
        });
    }
}
