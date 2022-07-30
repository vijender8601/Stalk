package com.example.stalk.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stalk.Adapter;
import com.example.stalk.Model.ApiModel;
import com.example.stalk.Model.Users;
import com.example.stalk.R;
import com.example.stalk.VolleySingleton;
import com.example.stalk.databinding.FragmentCodeforcesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CodeforcesFragment extends Fragment {


    public CodeforcesFragment() {
        // Required empty public constructor
    }

    FragmentCodeforcesBinding binding;
    String url = "https://competitive-coding-api.herokuapp.com/api/codeforces/";
    private RequestQueue requestQueue;
    RecyclerView recyclerView;
    ArrayList<ApiModel> apiModels;
    Adapter adapter;
    int rating = 0;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCodeforcesBinding.inflate(inflater, container, false);
        recyclerView = binding.codeforcesRecyclerView;
        apiModels = new ArrayList<>();
         adapter = new Adapter(apiModels,getContext(),"Codeforces");
        recyclerView.setAdapter(adapter);

        dialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait..", true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_layout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        assert users != null;
                        url+=users.getCodeforceId().trim();
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
//                                String username = "Username: "+response.getString("username");
//                                binding.userName.setText(username);
                                if(response.getString("rating").equals("Unrated"))
                                {
                                    String username = "Username: "+response.getString("username");
                                    binding.userName.setText(username);
                                    Toast.makeText(getContext(), "No Given Contest Found", Toast.LENGTH_SHORT).show();
                                    return ;
                                }
                                rating = response.getInt("rating");
                                String color = getColor(rating);
                                ForegroundColorSpan lightText = new ForegroundColorSpan(Color.parseColor("#787878"));
                                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(color));

                                // username
                                SpannableString username = new SpannableString("Username: "+response.getString("username"));
                                username.setSpan(lightText,0,10,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                username.setSpan(colorSpan,10,username.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                binding.userName.setText(username);
                                // contestRating
                                SpannableString contestRating = new SpannableString("Contest Rating : "+response.getString("rating"));
                                contestRating.setSpan(lightText,0,17,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                contestRating.setSpan(colorSpan,17,contestRating.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                binding.contestRating.setText(contestRating);
                                //ContestRank
                                SpannableString contestRank = new SpannableString("Contest Rank: "+response.getString("rank"));
                                contestRank.setSpan(lightText,0,14,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                contestRank.setSpan(colorSpan,14,contestRank.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                binding.contestRank.setText(contestRank);

                                color = getColor(response.getInt("max rating"));
                                colorSpan = new ForegroundColorSpan(Color.parseColor(color));
                                //maxRating
                                SpannableString contestMaxRating = new SpannableString("Max. Rating: "+response.getString("max rating"));
                                contestMaxRating.setSpan(lightText,0,13,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                contestMaxRating.setSpan(colorSpan,13,contestMaxRating.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                binding.contestMaxRating.setText(contestMaxRating);
                                //maxRank
                                SpannableString contestMaxRank = new SpannableString("Max. Rank: "+response.getString("max rank"));
                                contestMaxRank.setSpan(lightText,0,11,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                contestMaxRank.setSpan(colorSpan,11,contestMaxRank.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                binding.contestMaxRank.setText(contestMaxRank);
//                                Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();

                                JSONArray jsonArray = response.getJSONArray("contests");

                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    ApiModel model = new ApiModel();
                                    model.setContestName(jsonObject.getString("Contest"));
                                    model.setContestRank("Rank : "+jsonObject.getString("Rank"));
                                    model.setContestRating("New Rating: "+jsonObject.getString("New Rating"));
                                    apiModels.add(model);
                                }

                                adapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Network connection is slow or given id is incorrect/empty ", Toast.LENGTH_SHORT).show();
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

    }

    private String getColor(int rating)
    {

        if(rating<1200)
        {
            return "#666666";
        } else if(rating<1400)
        {
            return "#008000";
        } else if(rating<1600)
        {
            return "#03A89E";
        } else if(rating<1900)
        {
            return "#0000FF";
        } else if(rating<2100)
        {
            return "#AA00AA";
        } else if(rating<2300)
        {
            return "#FF8E29";
        } else if(rating<2400)
        {
            return "#FF8C00";
        } else if(rating<2600)
        {
           return "#FF7777";
        } else if(rating<3000)
        {
            return "#FF3333";
        } else
        {
            return "#FF0000";
        }


    }
}