package com.era.checkmelanoma.utils;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.era.checkmelanoma.R;
import com.era.checkmelanoma.databinding.FragmentBottomGenderBinding;

public class BottomGenderFragment extends BottomSheetDialogFragment {

    private FragmentBottomGenderBinding binding;
    private static IOnBtnPressed onBtnPressed;

    public static void setBtnClickListener(IOnBtnPressed onBtnPressed) {
        BottomGenderFragment.onBtnPressed = onBtnPressed;
    }

    public static BottomGenderFragment getInstance() {
        return new BottomGenderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_gender, container, false);

        binding.man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnPressed.onManClicked();
            }
        });

        binding.woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnPressed.onWomanClicked();
            }
        });

        return binding.getRoot();
    }
}
