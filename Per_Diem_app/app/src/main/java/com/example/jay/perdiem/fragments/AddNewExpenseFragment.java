package com.example.jay.perdiem.fragments;

import android.app.Fragment;
import android.content.Intent;
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

import com.example.jay.perdiem.MainActivity;
import com.example.jay.perdiem.R;
import com.example.jay.perdiem.objects.Expense;
import com.example.jay.perdiem.objects.Reports;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.riddhimanadib.formmaster.FormBuilder;
import me.riddhimanadib.formmaster.model.BaseFormElement;
import me.riddhimanadib.formmaster.model.FormElementPickerDate;
import me.riddhimanadib.formmaster.model.FormElementPickerSingle;
import me.riddhimanadib.formmaster.model.FormElementTextMultiLine;
import me.riddhimanadib.formmaster.model.FormElementTextNumber;
import me.riddhimanadib.formmaster.model.FormElementTextSingleLine;

public class AddNewExpenseFragment extends Fragment {

    public static AddNewExpenseFragment newInstance() {
        return new AddNewExpenseFragment();
    }


    private DatabaseReference db;
    private ArrayList<String> titles = new ArrayList<>();
    private final ArrayList<String> cats = new ArrayList<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final ArrayList<String> titlesCorrectNames = new ArrayList<>();
    // --Commented out by Inspection (5/26/18, 10:58 PM):private ArrayList<Reports> list = new ArrayList<>();
    private final ArrayList<Reports> report1 = new ArrayList<>();

    private DatabaseReference reportsEndpoint;


    private FormBuilder mFBnob;
    private FormBuilder mFBdop;
    private FormBuilder mFBmemo;
    private FormBuilder mFBcat;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_expense_new_add, container, false);



    }

    @Override
    public void onStart() {
        super.onStart();






        getCats();
     buildForm();
     registerListener();
    }

    private void getCats(){
        Intent intent = getActivity().getIntent();




        if(!titles.isEmpty()){
            titles.clear();
        }
        titles = intent.getStringArrayListExtra(MainActivity.EXTRA_REPORTS_ARRAY_LIST);
        for (String temp : titles) {
            String newTemp;
            newTemp = removeLastChar(temp);
            titlesCorrectNames.add(newTemp);
        }
        cats.add("Computer Related Equipment");
        cats.add("Employee Incentives & Gifts");
        cats.add("Lodging");
        cats.add("Marketing Expense");
        cats.add("Meals");
        cats.add("Gas");
        cats.add("Transportation");





    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu1, menu);
    }

    //TODO COMPLETE FORM BUILDER
    private void buildForm(){





        final LinearLayoutManager lMnob = new LinearLayoutManager(getActivity());
        lMnob.setOrientation(LinearLayoutManager.VERTICAL);

        final LinearLayoutManager lMdop = new LinearLayoutManager(getActivity());
        lMdop.setOrientation(LinearLayoutManager.VERTICAL);

        final LinearLayoutManager lMmemo = new LinearLayoutManager(getActivity());
        lMmemo.setOrientation(LinearLayoutManager.VERTICAL);

        final LinearLayoutManager lMcat = new LinearLayoutManager(getActivity());
        lMcat.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView mRVnob = getActivity().findViewById(R.id.rv_nob);
        RecyclerView mRVdop = getActivity().findViewById(R.id.rv_dop_total);
        RecyclerView mRVmemo = getActivity().findViewById(R.id.rv_memo_report);
        RecyclerView mRVcat = getActivity().findViewById(R.id.rv_cat);
        Button saveButton = getActivity().findViewById(R.id.save);

        // initialize variables
        mRVnob.setLayoutManager(lMnob);
        mRVdop.setLayoutManager(lMdop);
        mRVmemo.setLayoutManager(lMmemo);
        mRVcat.setLayoutManager(lMcat);

        mFBnob = new FormBuilder(getActivity(), mRVnob);
        mFBdop = new FormBuilder(getActivity(), mRVdop);
        mFBmemo = new FormBuilder(getActivity(), mRVmemo);
        mFBcat = new FormBuilder(getActivity(), mRVcat);



// declare form elements
       // FormHeader header = FormHeader.createInstance("Create A New Expense");

        FormElementTextSingleLine name_of_business = FormElementTextSingleLine
                .createInstance()
                .setTitle("Name Of Business")
                .setHint("")
                .setTag(1)
                .setRequired(true);

        FormElementPickerDate date_of_purchase = FormElementPickerDate
                .createInstance()
                .setTitle("Date")
                .setDateFormat("MMM dd, yyyy")
                .setTag(2)
                .setRequired(true);

        FormElementTextNumber total = FormElementTextNumber
                .createInstance()
                .setTitle("Total")
                .setTag(3)
                .setRequired(true);

        FormElementTextMultiLine memo_line = FormElementTextMultiLine
                .createInstance()
                .setTitle("Memo")
                .setTag(4)
                .setRequired(true);

        FormElementPickerSingle report_name = FormElementPickerSingle
                .createInstance()
                .setRequired(true)
                .setOptions(titles)
                .setTitle("Report Name")
                .setTag(5);

        FormElementPickerSingle report_category = FormElementPickerSingle
                .createInstance()
                .setRequired(true)
                .setOptions(cats)
                .setTag(6)
                .setTitle("Report Category");

// add them in a list
        List<BaseFormElement> formItemsNob = new ArrayList<>();
        List<BaseFormElement> formItemsDop = new ArrayList<>();
        List<BaseFormElement> formItemsMemo = new ArrayList<>();
        List<BaseFormElement> formItemsCats = new ArrayList<>();


        formItemsNob.add(name_of_business);
        formItemsDop.add(date_of_purchase);
        formItemsDop.add(total);
        formItemsMemo.add(memo_line);
        formItemsMemo.add(report_name);
        formItemsCats.add(report_category);



// build and display the form
        mFBnob.addFormElements(formItemsNob);
        mFBdop.addFormElements(formItemsDop);
        mFBmemo.addFormElements(formItemsMemo);
        mFBcat.addFormElements(formItemsCats);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveOutToFireBase();

            }
        });

    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 15);
    }


    private void registerListener(){
        db = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser fBUser = auth.getCurrentUser();
        reportsEndpoint = db.child("Reports").child(Objects.requireNonNull(fBUser).getUid());
        reportsEndpoint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    Reports note = noteSnapshot.getValue(Reports.class);
                        report1.add(note);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        db.child("Reports").child("Temp").setValue("");
    }

    private void saveOutToFireBase(){
        db = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser fBUser = auth.getCurrentUser();
        reportsEndpoint = db.child("Reports").child(Objects.requireNonNull(fBUser).getUid());


        db.child("Reports").child("Temp").setValue("");



        if(mFBnob.isValidForm() && mFBdop.isValidForm() && mFBmemo.isValidForm() && mFBcat.isValidForm()){

            Reports currentReport;
            String rName = mFBnob.getFormElement(1).getValue();
            String rDate = mFBdop.getFormElement(2).getValue();
            String rTotal = mFBdop.getFormElement(3).getValue();
            String rCat = mFBcat.getFormElement(6).getValue();
            String rMemo = mFBmemo.getFormElement(4).getValue();
            final String rReportName = mFBmemo.getFormElement(5).getValue();


            for (Reports reports : report1){
                if (reports.getIdentifier().equals(rReportName)){
                    currentReport = reports;

                    Expense expense = new Expense(rName,rDate,rTotal);
                    expense.setID(currentReport.getIdentifier());
                    expense.setMemo(rMemo);
                    expense.setCat(rCat);

                    currentReport.addExpense(expense);
                    db.child("Reports").child(fBUser.getUid()).child(rReportName).child("expenses").setValue(currentReport.getExpenses());
                    getActivity().finish();



                }
            }














        }else{
            Toast.makeText(getActivity(),"Please complete Form",Toast.LENGTH_SHORT).show();
        }

    }
}

