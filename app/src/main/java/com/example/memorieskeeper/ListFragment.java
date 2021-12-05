package com.example.memorieskeeper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.memorieskeeper.databinding.FragmentListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class ListFragment extends androidx.fragment.app.ListFragment {
    private FragmentListBinding binding;

    Query memoriesQuery;
    FirebaseUser user;
    ArrayAdapter<MemoryModel> arrayAdapter;
    ArrayList<MemoryModel> arrayList = new ArrayList<>();

    @Override
    public void onStart() {
        super.onStart();

        memoriesQuery = FirebaseDatabase.getInstance().getReference(getString(R.string.memories_collection_name)).orderByChild("createdAt");
        user = FirebaseAuth.getInstance().getCurrentUser();
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arrayList);
        setListAdapter(arrayAdapter);

        memoriesQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MemoryModel memory = snapshot.getValue(MemoryModel.class);
                if (memory.getUserId().equals(user.getUid())) {
                    arrayList.add(memory);
                }
                arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arrayList);
                setListAdapter(arrayAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        NavHostFragment
                .findNavController(ListFragment.this)
                .navigate(R.id.action_ListFragment_to_MemoryFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}