package com.example.stalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.stalk.Fragments.AtcoderFragment;
import com.example.stalk.Fragments.CodechefFragment;
import com.example.stalk.Fragments.CodeforcesFragment;
import com.example.stalk.Fragments.HomeFragment;
import com.example.stalk.Fragments.LeetcodeFragment;
import com.example.stalk.databinding.ActivityMainBinding;
import com.iammert.library.readablebottombar.ReadableBottomBar;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1c97d3")));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer,new HomeFragment());
        transaction.commit();

        binding.readableBottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch(i){
                    case 0:
                        transaction.replace(R.id.fragmentContainer,new AtcoderFragment());
                        break;
                    case 1:
                        transaction.replace(R.id.fragmentContainer,new CodechefFragment());
                        break;
                    case 2:
                        transaction.replace(R.id.fragmentContainer,new HomeFragment());
                        break;
                    case 3:
                        transaction.replace(R.id.fragmentContainer,new CodeforcesFragment());
                        break;
                    default:
                        transaction.replace(R.id.fragmentContainer,new LeetcodeFragment());
                        break;
                }
                transaction.commit();

            }
        });




    }
}