package com.example.stalk.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.stalk.MainActivity;
import com.example.stalk.Model.Users;
import com.example.stalk.R;
import com.example.stalk.databinding.FragmentEditProfileBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class EditProfileFragment extends Fragment {


    public EditProfileFragment() {
        // Required empty public constructor
    }

    FragmentEditProfileBinding binding;
    FirebaseUser user;
    DatabaseReference reference;
    String uid;
    FirebaseDatabase database;
    String password;
    Uri profilrPicUri ;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        uid = user.getUid();
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("profile_img").child(uid);

        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_layout);

        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.show();
                Users users = snapshot.getValue(Users.class);

                Picasso.get().load(users.getProfileImg()).placeholder(R.drawable.profile)
                        .into(binding.profileImageUser);
                binding.email.setText(users.getEmaill());
                binding.userName.setText(users.getFullName());
                binding.codechefId.setText(users.getCodechefId());
                binding.codeforcesId.setText(users.getCodeforceId());
                binding.atcoderId.setText(users.getAtcoderId());
                binding.leetcodeId.setText(users.getLeetcodeId());
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        binding.updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });


        binding.pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(EditProfileFragment.this).crop()
                        .start();
            }
        });

        return binding.getRoot();
    }


    private void updateUserProfile()
    {
        if(binding.userName.getText().toString().isEmpty())
        {
            binding.userName.setError("Name is required");
            binding.userName.requestFocus();
            return;
        }

        Map<String,Object> map = new HashMap<>();
        map.put("fullName",binding.userName.getText().toString());
        map.put("codechefId",binding.codechefId.getText().toString());
        map.put("codeforceId",binding.codeforcesId.getText().toString());
        map.put("atcoderId",binding.atcoderId.getText().toString());
        map.put("leetcodeId",binding.leetcodeId.getText().toString());
        reference.child(FirebaseAuth.getInstance().getUid())
                .updateChildren(map);

        Toast.makeText(getContext(), "Updated Profile Successfully", Toast.LENGTH_SHORT).show();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer,new HomeFragment());
        transaction.commit();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            assert data != null;
            if(!data.getData().equals("")) {
                Uri uri = data.getData();
                binding.profileImageUser.setImageURI(uri);
                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                reference.child(FirebaseAuth.getInstance().getUid())
                                        .child("profileImg").setValue(uri.toString());
                            }
                        });
                    }
                });
            }
        } catch (Exception e)
        {
            Toast.makeText(getActivity(), "Select Image", Toast.LENGTH_SHORT).show();
        }
    }



}