package com.example.stalk.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stalk.Model.ApiModel;
import com.example.stalk.Model.Users;
import com.example.stalk.R;
import com.example.stalk.VolleySingleton;
import com.example.stalk.databinding.FragmentAtcoderBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AtcoderFragment extends Fragment {



    public AtcoderFragment() {
        // Required empty public constructor
    }

    FragmentAtcoderBinding binding;
    String url = "https://competitive-coding-api.herokuapp.com/api/atcoder/";
    private RequestQueue requestQueue;
    ProgressDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAtcoderBinding.inflate(inflater, container, false);

        dialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait..", true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_layout);

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        assert users != null;
                        url+=users.getAtcoderId().trim();
                        fetchData();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        requestQueue = VolleySingleton.getmInstance(getContext()).getRequestQueue();



        return binding.getRoot();
    }


    private void fetchData() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.show();
                        try {
                            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#606160"));
                            SpannableString username = new SpannableString("Username: "+response.getString("username"));
                            username.setSpan(colorSpan,10,username.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            binding.userName.setText(username);

                            SpannableString rating = new SpannableString("Rating: "+response.getString("rating"));
                            rating.setSpan(colorSpan,8,rating.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            binding.contestRating.setText(rating);

                            SpannableString maxrating = new SpannableString("Max. Rating: "+response.getString("highest"));
                            maxrating.setSpan(colorSpan,13,maxrating.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            binding.contestMaxRating.setText(rating);

                            SpannableString rank = new SpannableString("Rank : "+response.getString("rank"));
                            rank.setSpan(colorSpan,6,rank.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            binding.contestRank.setText(rank);

                            SpannableString level = new SpannableString("Level: "+response.getString("level"));
                            level.setSpan(colorSpan,7,level.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            binding.contestLevel.setText(level);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Network connection is slow or given id is incorrect/empty ", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                        binding.infoForUpdate.setVisibility(View.VISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.show();
                Toast.makeText(getContext(), "Network connection is slow or given id is incorrect/empty", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                binding.infoForUpdate.setVisibility(View.VISIBLE);
            }
        });

        requestQueue.add(jsonObjectRequest);

    }
}