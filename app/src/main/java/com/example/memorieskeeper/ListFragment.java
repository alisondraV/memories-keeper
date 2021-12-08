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

/*
    This fragment displays the list of the memories
    (documents pulled from the firebase real-time database)
 */
public class ListFragment extends androidx.fragment.app.ListFragment {
    private FragmentListBinding binding;

    Query memoriesQuery;
    FirebaseUser user;
    ArrayAdapter<MemoryModel> arrayAdapter;

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            /*
                Create memory object using MemoryModel and add it to the array adapter
             */
            MemoryModel memory = snapshot.getValue(MemoryModel.class);
            arrayAdapter.add(memory);
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
    };

    @Override
    public void onStart() {
        super.onStart();

        memoriesQuery = FirebaseDatabase.getInstance().getReference(getString(R.string.memories_collection_name)).orderByChild("createdAt");
        user = FirebaseAuth.getInstance().getCurrentUser();
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        setListAdapter(arrayAdapter);

        memoriesQuery.addChildEventListener(childEventListener);
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

        /*
            Navigate back to the memories list
         */
        NavHostFragment
                .findNavController(ListFragment.this)
                .navigate(R.id.action_ListFragment_to_MemoryFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        memoriesQuery.removeEventListener(childEventListener);
    }
}