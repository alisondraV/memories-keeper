package com.example.memorieskeeper;

import androidx.activity.result.ActivityResultCallback;
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
import android.os.Debug;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memorieskeeper.services.FileService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class AddMemoryActivity extends AppCompatActivity {
    TextView txtName, txtDescription, txtLocation;
    Button btnAddMemory;
    ImageView imgMemoryPicture;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    Uri pickedPhotoUri = null;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                pickedPhotoUri = uri;
                imgMemoryPicture.setImageURI(uri);
            });

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

        // get UI elements
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtLocation = findViewById(R.id.txtLocation);
        btnAddMemory = findViewById(R.id.btnAddMemory);
        imgMemoryPicture = findViewById(R.id.imgMemoryPicture);

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

            try {
                InputStream stream = getContentResolver().openInputStream(pickedPhotoUri);
                byte[] imageBytes = getBytes(stream);
                Intent fileUploadIntent = new Intent(AddMemoryActivity.this, FileService.class);
                fileUploadIntent.putExtra(FileService.BYTES_PARAM, imageBytes);
                fileUploadIntent.putExtra(FileService.IMAGE_NAME_PARAM, pickedPhotoUri.getLastPathSegment());
                startService(fileUploadIntent);
            } catch (FileNotFoundException e) {
                Log.e("FILE_NOT_FOUND", e.getMessage());
            } catch (IOException e) {
                Log.e("IO_EXCEPTION", e.getMessage());
            }
        });

        imgMemoryPicture.setOnClickListener(view -> {
            mGetContent.launch("image/*");
        });
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    protected void onStop() {
        super.onStop();
        databaseReference.removeEventListener(onButtonClickEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGetContent.unregister();
    }
}