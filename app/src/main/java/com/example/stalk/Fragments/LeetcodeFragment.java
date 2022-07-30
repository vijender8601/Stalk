package com.example.stalk.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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
import com.example.stalk.databinding.FragmentLeetcodeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LeetcodeFragment extends Fragment {



    public LeetcodeFragment() {
        // Required empty public constructor
    }

    FragmentLeetcodeBinding binding;
    String url = "https://competitive-coding-api.herokuapp.com/api/leetcode/";
    private RequestQueue requestQueue;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentLeetcodeBinding.inflate(inflater, container, false);

        dialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait..", true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_layout);

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        assert users != null;
                        url+=users.getLeetcodeId().trim();
                        String username = "Username: "+users.getLeetcodeId();
                        binding.userName.setText(username);
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
                            String totalSolved = "Total Solved Problem: "+response.getString("total_problems_solved");
                            binding.totalSolved.setText(totalSolved);
                            String reputation = "Reputation: "+response.getString("reputation");
                            binding.reputation.setText(reputation);
                            String points = "Points: "+response.getString("contribution_points");
                            binding.contributionPoint.setText(points);

                            String easySolved = response.getString("easy_questions_solved")+"/"+response.getString("total_easy_questions");
                            binding.easySolved.setText(easySolved);
                            String easyAccept = response.getString("easy_acceptance_rate");
                            binding.easyAccepptanceRate.setText(easyAccept);

                            String mediumSolved = response.getString("medium_questions_solved")+"/"+response.getString("total_medium_questions");
                            binding.mediumSolved.setText(mediumSolved);
                            String mediumAccept = response.getString("medium_acceptance_rate");
                            binding.mediumAccepptanceRate.setText(mediumAccept);

                            String hardSolved = response.getString("hard_questions_solved")+"/"+response.getString("total_hard_questions");
                            binding.hardSolved.setText(hardSolved);
                            String hardAccept = response.getString("hard_acceptance_rate");
                            binding.hardAccepptanceRate.setText(hardAccept);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Network connection is slow or given id is incorrect/empty", Toast.LENGTH_SHORT).show();
                        }
                        binding.infoForUpdate.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.show();
                Toast.makeText(getContext(), "Network connection is slow or given id is incorrect/empty", Toast.LENGTH_SHORT).show();
                binding.infoForUpdate.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}