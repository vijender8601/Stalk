package com.example.stalk.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.stalk.Adapter;
import com.example.stalk.Model.ApiModel;
import com.example.stalk.Model.Users;
import com.example.stalk.R;
import com.example.stalk.VolleySingleton;
import com.example.stalk.databinding.FragmentCodechefBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CodechefFragment extends Fragment {


    public CodechefFragment() {
        // Required empty public constructor
    }
    FragmentCodechefBinding binding;
    private RequestQueue requestQueue;
    String url = "https://competitive-coding-api.herokuapp.com/api/codechef/";
    RecyclerView recyclerView;
    ArrayList<ApiModel> apiModels;
    Adapter adapter;
   ProgressDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCodechefBinding.inflate(inflater, container, false);
        recyclerView = binding.codechefRecyclerView;
        apiModels = new ArrayList<>();
        adapter = new Adapter(apiModels,getContext(),"Codechef");
        recyclerView.setAdapter(adapter);

         dialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait..", true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_layout);

//        customProgressDialog = new CustomProgressDialog(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        assert users != null;
                        url+=users.getCodechefId().trim();
                        fetchData();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        requestQueue = VolleySingleton.getmInstance(getContext()).getRequestQueue();
        return binding.getRoot();
    }

    private void fetchData(){
//        customProgressDialog.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.show();
                        try {

                            String color = getColor(response.getInt("rating"));
                            ForegroundColorSpan lightText = new ForegroundColorSpan(Color.parseColor("#787878"));
                            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(color));

                            if(response.getInt("rating")==0)
                            {
                                Toast.makeText(getContext(), "No Given Contest Found", Toast.LENGTH_SHORT).show();
                                binding.userName.setText(response.getJSONObject("user_details").getString("username"));
//                                return ;
                            }else {
                                //Username
                                SpannableString username = new SpannableString("Username: " + response.getJSONObject("user_details").getString("username"));
                                username.setSpan(lightText,0,10,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                username.setSpan(colorSpan, 10, username.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                binding.userName.setText(username);
                            }

                            //Contest rating
                            SpannableString contestRating = new SpannableString("Contest Rating: "+response.getString("rating"));
                            contestRating.setSpan(lightText,0,16,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            contestRating.setSpan(colorSpan,16,contestRating.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            binding.contestRating.setText(contestRating);

                            //Star
                            SpannableString star = new SpannableString("Star: "+response.getString("stars"));
                            star.setSpan(lightText,0,6,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            star.setSpan(colorSpan,6,star.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            binding.contestStar.setText(star);

                            color = getColor(response.getInt("highest_rating"));
                            colorSpan = new ForegroundColorSpan(Color.parseColor(color));
                            //Max rating
                            SpannableString maxRating = new SpannableString("Max. Rating: "+response.getString("highest_rating"));
                            maxRating.setSpan(lightText,0,12,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            maxRating.setSpan(colorSpan,13,maxRating.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            binding.contestMaxRating.setText(maxRating);

                            //Global rank
                            SpannableString globalRank = new SpannableString("Global Rank: "+response.getString("global_rank"));
                            globalRank.setSpan(lightText,0,13,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            globalRank.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")),13,globalRank.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            binding.contestGlobalRank.setText(globalRank);

                            JSONArray jsonArray = response.getJSONArray("contest_ratings");
                            for(int i=jsonArray.length()-1;i>=0;i--)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ApiModel model = new ApiModel();
                                model.setContestName(jsonObject.getString("name"));
                                model.setContestRank("Rank : "+jsonObject.getString("rank"));
                                model.setContestRating("New Rating: "+jsonObject.getString("rating"));
                                apiModels.add(model);
                            }

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Network connection is slow or given id is incorrect/empty", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.show();
                Toast.makeText(getContext(), "Network connection is slow or given id is incorrect/empty", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        requestQueue.add(jsonObjectRequest);

//        customProgressDialog.dismiss();
    }

    private String getColor(int rating)
    {

        if(rating<1400)
        {
            return "#666666";
        } else if(rating<1600)
        {
            return "#1E7D22";
        } else if(rating<1800)
        {
            return "#3366CC";
        } else if(rating<2000)
        {
            return "#684273";
        } else if(rating<2200)
        {
            return "#FFBF00";
        }
        else if(rating<2500)
        {
            return "#FF7F00";
        } else {
            return "#D0011B";
        }

    }
}