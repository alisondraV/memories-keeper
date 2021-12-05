package com.example.memorieskeeper;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memorieskeeper.services.FileService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
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
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    boolean boundToFileService = false;
    FileService fileService;
    ActivityResultLauncher<Intent> pickFileLauncher;
    Uri pickedPhotoUri = null;

    ValueEventListener onButtonClickEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Toast.makeText(AddMemoryActivity.this, "Memory has been successfully created!", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(AddMemoryActivity.this, "An error occurred while creating a memory :(", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);

        // bind to services
        Intent fileServiceIntent = new Intent(this, FileService.class);
        bindService(fileServiceIntent, fileServiceConnection, Context.BIND_AUTO_CREATE);

        pickFileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent resultData = result.getData();
                        if (resultData != null) {
                            pickedPhotoUri = resultData.getData();
                        }
                    }
                }
            );

        // get UI elements
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtLocation = findViewById(R.id.txtLocation);
        btnAddMemory = findViewById(R.id.btnAddMemory);
        Button btnUploadPhoto = findViewById(R.id.btnUploadPhoto);

        // get database and auth reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(getString(R.string.memories_collection_name));
        user = FirebaseAuth.getInstance().getCurrentUser();

        // configure UI elements
        btnAddMemory.setOnClickListener(view -> {
            MemoryModel newMemory = new MemoryModel(
                    user.getUid(),
                    String.valueOf(txtName.getText()),
                    String.valueOf(txtDescription.getText()),
                    String.valueOf(txtLocation.getText()));
            databaseReference.child(UUID.randomUUID().toString()).setValue(newMemory);

            databaseReference.addValueEventListener(onButtonClickEventListener);
        });

        btnUploadPhoto.setOnClickListener(view -> {
            Intent openFileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            openFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
            openFileIntent.setType("image/*");
            pickFileLauncher.launch(openFileIntent);

//            Thread thread = new Thread(() -> {
//                try {
//                    Thread.sleep(5000);
//                    String imageUrl = fileService.uploadFile();
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            });
//            thread.start();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(fileServiceConnection);
        databaseReference.removeEventListener(onButtonClickEventListener);
    }

    private final ServiceConnection fileServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            FileService.FileServiceBinder binder = (FileService.FileServiceBinder) service;
            fileService = binder.getService();
            boundToFileService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            boundToFileService = false;
        }
    };
}