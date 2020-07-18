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
import com.example.compass.vm.CompassActivityViewModel;

import java.util.Objects;

import static com.example.compass.util.ConstsKt.APP_LOCATION_PROVIDER;

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
        setEditTextFieldsFromViewModel(binding);
        binding.coordinateDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.coordinateDialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = new Location(APP_LOCATION_PROVIDER);
                double latitude = Double.parseDouble(Objects.requireNonNull(binding.latitudeEditText.getText()).toString());
                double longitude = Double.parseDouble(Objects.requireNonNull(binding.longitudeEditText.getText()).toString());
                location.setLatitude(latitude);
                location.setLongitude(longitude);
                compassActivityViewModel.updateDestinationLocation(location);
                dismiss();
            }
        });
    }

    private void setEditTextFieldsFromViewModel(DialogCoordinatesBinding binding) {
        if (compassActivityViewModel.getDestinationLocation().getValue() != null) {
            String longitude = String.valueOf(compassActivityViewModel.getDestinationLocation().getValue().getLongitude());
            String latitude = String.valueOf(compassActivityViewModel.getDestinationLocation().getValue().getLatitude());
            binding.longitudeEditText.setText(longitude);
            binding.latitudeEditText.setText(latitude);
        }
    }
}
