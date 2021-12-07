package com.example.memorieskeeper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.memorieskeeper.databinding.FragmentMemoryBinding;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        new DownloadImageTask(imgMemory).execute(memory.getImageUrl());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}