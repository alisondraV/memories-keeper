package com.example.memorieskeeper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.memorieskeeper.databinding.FragmentMemoryBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
    This fragment represents information about the selected memory from the memories list.
 */
public class MemoryFragment extends Fragment {
    TextView txtMemoryName, txtMemoryCreated, txtMemoryLocation, txtMemoryDescription;
    ImageView imgMemory;
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
        txtMemoryCreated = view.findViewById(R.id.txtMemoryCreated);
        txtMemoryLocation = view.findViewById(R.id.txtMemoryLocation);
        txtMemoryDescription = view.findViewById(R.id.txtMemoryDescription);
        imgMemory = view.findViewById(R.id.imgMemory);

        if (getArguments() == null) {
            Toast.makeText(MemoryFragment.super.getContext(), "Couldn't get the memory :(", Toast.LENGTH_SHORT).show();
            NavHostFragment
                    .findNavController(MemoryFragment.this)
                    .popBackStack();
            return;
        }

        MemoryModel memory = getArguments().getParcelable("memory");
        String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(memory.getCreatedAt()));

        txtMemoryName.setText(memory.getName());
        txtMemoryCreated.setText(dateString);
        txtMemoryLocation.setText(memory.getLocation());
        txtMemoryDescription.setText(memory.getDescription());
        Glide.with(this).load(memory.getImageUrl()).into(imgMemory);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}