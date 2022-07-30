package com.example.stalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.stalk.Model.Users;
import com.example.stalk.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");



        binding.signInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,SignInActivity.class));
                finish();
            }
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        if(auth.getCurrentUser()!=null)
        {
            if(auth.getCurrentUser().isEmailVerified()) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(RegisterActivity.this, SignInActivity.class));
                finish();
            }
        }

    }

    private void registerUser()
    {
        if(binding.textInputEmail.getText().toString().isEmpty())
        {
            binding.textInputEmail.setError("Email is Required");
            binding.textInputEmail.requestFocus();
            return ;
        }
        if(binding.textInputFirstName.getText().toString().isEmpty())
        {
            binding.textInputFirstName.setError("Name is Required");
            binding.textInputFirstName.requestFocus();
            return ;
        }
        if(binding.textInputPassword.getText().toString().isEmpty())
        {
            binding.textInputPassword.setError("Password is Required");
            binding.textInputPassword.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(binding.textInputEmail.getText().toString().trim()).matches())
        {

            binding.textInputEmail.setError("Please Provide Valid Email");
            binding.textInputEmail.requestFocus();
            return ;
        }
        progressDialog.show();
        auth.createUserWithEmailAndPassword(binding.textInputEmail.getText().toString().trim(),binding.textInputPassword.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Users users = new Users(binding.textInputEmail.getText().toString().trim(),
                                    binding.textInputFirstName.getText().toString()+" "+binding.textInputLastName.getText().toString(),
                                    binding.textInputPassword.getText().toString());

                            database.getReference("Users").child(auth.getCurrentUser().getUid())
                                    .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).sendEmailVerification();
                                        Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this,SignInActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this,task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}