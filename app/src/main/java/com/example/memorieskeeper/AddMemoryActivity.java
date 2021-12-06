package com.example.memorieskeeper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memorieskeeper.services.FileService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class AddMemoryActivity extends AppCompatActivity {
    TextView txtName, txtDescription, txtLocation;
    Button btnAddMemory;
    ImageView imgMemoryPicture;
    Uri pickedPhotoUri = null;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                pickedPhotoUri = uri;
                imgMemoryPicture.setImageURI(uri);
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);

        pickedPhotoUri = null;

        // get UI elements
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtLocation = findViewById(R.id.txtLocation);
        btnAddMemory = findViewById(R.id.btnAddMemory);
        imgMemoryPicture = findViewById(R.id.imgMemoryPicture);

        // get database and auth reference
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // configure UI elements
        btnAddMemory.setOnClickListener(view -> {
            MemoryModel newMemory = new MemoryModel(
                    user == null ? "Anonymous" : user.getDisplayName(),
                    String.valueOf(txtName.getText()),
                    String.valueOf(txtDescription.getText()),
                    String.valueOf(txtLocation.getText()));

            Intent fileUploadIntent = new Intent(AddMemoryActivity.this, FileService.class);
            fileUploadIntent.putExtra("memory", newMemory);
            fileUploadIntent.setData(pickedPhotoUri);
            fileUploadIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startService(fileUploadIntent);
            finish();
        });

        imgMemoryPicture.setOnClickListener(view -> mGetContent.launch("image/*"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGetContent.unregister();
    }
}