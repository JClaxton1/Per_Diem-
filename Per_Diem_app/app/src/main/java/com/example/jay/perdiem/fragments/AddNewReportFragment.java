package com.example.jay.perdiem.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.jay.perdiem.R;
import com.example.jay.perdiem.objects.Reports;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import me.riddhimanadib.formmaster.FormBuilder;
import me.riddhimanadib.formmaster.model.BaseFormElement;
import me.riddhimanadib.formmaster.model.FormElementPickerDate;
import me.riddhimanadib.formmaster.model.FormElementTextSingleLine;
import me.riddhimanadib.formmaster.model.FormHeader;

public class AddNewReportFragment extends Fragment {

    public static AddNewReportFragment newInstance() {
        return new AddNewReportFragment();
    }


    private FormBuilder mFormBuilder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_report_new_add, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

     buildForm();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu1, menu);
    }

    //TODO COMPLETE FORM BUILDER
    private void buildForm(){

        Button saveButton = getActivity().findViewById(R.id.save);

        // initialize variables
        RecyclerView mRecyclerView = getActivity().findViewById(R.id.recyclerView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mFormBuilder = new FormBuilder(getActivity(), mRecyclerView);

// declare form elements
        FormHeader header = FormHeader.createInstance("Create A New Report");



        FormElementTextSingleLine element = FormElementTextSingleLine
                .createInstance()
                .setTitle("Report Name")
                .setTag(1)
                .setRequired(true);

        FormElementPickerDate element1 = FormElementPickerDate
                .createInstance()
                .setTitle("Start Date")
                .setDateFormat("MMM dd, yyyy")
                .setTag(2)
                .setRequired(true);

        FormElementPickerDate element2 = FormElementPickerDate
                .createInstance()
                .setTitle("End Date")
                .setDateFormat("MMM dd, yyyy")
                .setTag(3)
                .setRequired(true);


// add them in a list
        List<BaseFormElement> formItems = new ArrayList<>();
        formItems.add(header);
        formItems.add(element);
        formItems.add(element1);
        formItems.add(element2);

// build and display the form
        mFormBuilder.addFormElements(formItems);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveOutToFireBase();

            }
        });

    }



    private void saveOutToFireBase(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());

        if(mFormBuilder.isValidForm()){
            String rName =mFormBuilder.getFormElement(1).getValue();
            String rStartDate =mFormBuilder.getFormElement(2).getValue();
            String rEndDate =mFormBuilder.getFormElement(3).getValue();


            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser fBUser = auth.getCurrentUser();

            Reports report = new Reports(Objects.requireNonNull(fBUser).getUid(),rName,rStartDate,rEndDate);
            report.setIdentifier(rName+currentDateandTime);

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();

            db.child("Reports").child(fBUser.getUid()).child(report.getIdentifier()).setValue(report);
            getActivity().finish();

        }else{
            Toast.makeText(getActivity(),"Please complete Form",Toast.LENGTH_SHORT).show();
        }

    }
}
