package com.example.jay.perdiem;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.jay.perdiem.fragments.AddNewExpenseFragment;
import com.example.jay.perdiem.fragments.AddNewExpenseFragmentCam;
import com.example.jay.perdiem.fragments.AddNewReportFragment;

public class

CreateNewActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_create);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        switch (message){
            case "New Report":
                AddNewReportFragment report = AddNewReportFragment.newInstance();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, report, "test")
                        .commit();
                break;
            case "New Expense":
                AddNewExpenseFragment expense = AddNewExpenseFragment.newInstance();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, expense, "test")
                        .commit();
                break;
            case "Camera":
                AddNewExpenseFragmentCam cam = AddNewExpenseFragmentCam.newInstance();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, cam, "test")
                        .commit();
                break;

            default:
                loadFragment(new AddNewReportFragment());
                break;

        }

    }



    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
