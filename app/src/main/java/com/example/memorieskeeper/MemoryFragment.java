package com.example.memorieskeeper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.memorieskeeper.databinding.FragmentMemoryBinding;

public class MemoryFragment extends Fragment {
    TextView txtMemoryName;
    private FragmentMemoryBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentMemoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtMemoryName = view.findViewById(R.id.txtMemoryName);
        if (getArguments() == null) {
            Toast.makeText(MemoryFragment.super.getContext(), "Couldn't get the memory :(", Toast.LENGTH_SHORT).show();
            NavHostFragment
                    .findNavController(MemoryFragment.this)
                    .popBackStack();
            return;
        }

        MemoryModel memory = getArguments().getParcelable("memory");
        txtMemoryName.setText(memory.getName());

        binding.buttonPrevious.setOnClickListener(view1 -> NavHostFragment
                .findNavController(MemoryFragment.this)
                .popBackStack()
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}