package com.example.stalk;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stalk.Model.ApiModel;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    ArrayList<ApiModel> apiModels;
    Context context;
    String site = "";

    public Adapter(ArrayList<ApiModel> apiModels, Context context,String site) {
        this.apiModels = apiModels;
        this.context = context;
        this.site = site;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_holder,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ApiModel model = apiModels.get(i);
            String contestNameText = model.getContestName();
//        SpannableString spannableString = new SpannableString()
            viewHolder.contestName.setText(model.getContestName());
        ForegroundColorSpan lightText = new ForegroundColorSpan(Color.parseColor("#787878"));
            SpannableString rank = new SpannableString(model.getContestRank());
            rank.setSpan(lightText,0,6,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            rank.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 6, rank.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.contestnewRank.setText(rank);
            SpannableString contestRating = new SpannableString(model.getContestRating());
//        "New Rating: "
            String color = getColor(Integer.parseInt("" + contestRating.subSequence(12, contestRating.length())));

            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(color));
            contestRating.setSpan(lightText,0,12,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            contestRating.setSpan(colorSpan, 12, contestRating.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.contestnewRating.setText(contestRating);

    }

    @Override
    public int getItemCount() {
        return apiModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contestName,contestnewRank,contestnewRating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contestName = itemView.findViewById(R.id.contestName);
            contestnewRank = itemView.findViewById(R.id.contestnewRank);
            contestnewRating = itemView.findViewById(R.id.contestnewRating);
        }
    }

    private String getColor(int rating)
    {
     if(site.equals("Codeforces")) {
         if (rating < 1200) {
             return "#666666";
         } else if (rating < 1400) {
             return "#008000";
         } else if (rating < 1600) {
             return "#03A89E";
         } else if (rating < 1900) {
             return "#0000FF";
         } else if (rating < 2100) {
             return "#AA00AA";
         } else if (rating < 2300) {
             return "#FF8E29";
         } else if (rating < 2400) {
             return "#FF8C00";
         } else if (rating < 2600) {
             return "#FF7777";
         } else if (rating < 3000) {
             return "#FF3333";
         } else {
             return "#FF0000";
         }
     } else {
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
}
