package com.example.jay.perdiem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jay.perdiem.adaptors.ExpenseAdaptor;
import com.example.jay.perdiem.adaptors.ReportAdaptor;
import com.example.jay.perdiem.objects.Expense;
import com.example.jay.perdiem.objects.Reports;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "MainActivity" ;

    private final ArrayList<Reports> reportsArrayList = new ArrayList<>();
    private final ArrayList<String> reportsArrayListTitles = new ArrayList<>();

    private final ArrayList<Expense> expensesArrayList = new ArrayList<>();



    private ReportAdaptor mAdapter;

    private TextView report_count;
    private TextView report_count_label;


    public static final String EXTRA_MESSAGE = "com.example.jay.perdiem.MESSAGE";
    public static final String EXTRA_REPORTS_ARRAY_LIST = "com.example.jay.perdiem.REPORTS_ARRAY_LIST";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // tv = findViewById(R.id.testtext);
        FloatingActionButton mFabCam = findViewById(R.id.menu_item_camera);
        FloatingActionButton mFabEdit = findViewById(R.id.manual_entry);
        FloatingActionButton mFabReport = findViewById(R.id.create_new_report);
        TextView userName = findViewById(R.id.profile_name);
        report_count = findViewById(R.id.report_number1);
        report_count_label = findViewById(R.id.report_number_label);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        if (auth.getCurrentUser() != null) {

            // already signed in
            FirebaseUser fBUser = auth.getCurrentUser();

            userName.setText(fBUser.getDisplayName());

            DatabaseReference reportsEndpoint = mDatabase.child("Reports").child(fBUser.getUid());





            reportsEndpoint.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    reportsArrayList.clear();
                    reportsArrayListTitles.clear();

                    for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()){
                        Reports note = noteSnapshot.getValue(Reports.class);

                        reportsArrayList.add(note);
                        reportsArrayListTitles.add(Objects.requireNonNull(note).getIdentifier());


                        if(note.getExpenses() != null) {
                            for (Expense expense : note.getExpenses()) {
                                expensesArrayList.add(expense);
                            }
                        }





                        mAdapter = new ReportAdaptor(getBaseContext(),reportsArrayList);
                        ArrayAdapter adapter = new ArrayAdapter<>(getBaseContext(),
                                R.layout.activity_listview, reportsArrayList);


                        report_count.setText(adapter.getCount()+"");
                        if(adapter.getCount()>1){
                            report_count_label.setText("Reports");
                        }else{
                            report_count_label.setText("Report");
                        }



                        ListView listView = findViewById(R.id.list);
                        listView.setAdapter(mAdapter);

                    }




                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

          //  Users currentUser = new Users(fBUser.getDisplayName(), fBUser.getEmail(), fBUser.getUid());
            //tv.setText(currentUser.getUid());






        } else {
            // not signed in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false, true)
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                            .build(),
                    RC_SIGN_IN);
        }





        mFabCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(), CreateNewActivity.class);

                String message = "Camera";
                intent.putExtra(EXTRA_MESSAGE, message);
                intent.putStringArrayListExtra(EXTRA_REPORTS_ARRAY_LIST,reportsArrayListTitles);
                startActivity(intent);



            }
        });

        mFabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CreateNewActivity.class);

                String message = "New Expense";
                intent.putExtra(EXTRA_MESSAGE, message);
                intent.putStringArrayListExtra(EXTRA_REPORTS_ARRAY_LIST,reportsArrayListTitles);
                startActivity(intent);
            }
        });

        mFabReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CreateNewActivity.class);

                String message = "New Report";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(getApplicationContext() ,MainActivity.class));
                finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    startActivity(new Intent(getApplicationContext() ,MainActivity.class));
                    finish();
                    return;
                }

                if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    //TODO Create no network OOPs screen directing user to settings to turn on internet
                    Toast.makeText(getApplicationContext(),"Error Network",Toast.LENGTH_SHORT).show();
                    return;
                }


                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.expenses:


                expensesArrayList.clear();
                for (Reports reports : reportsArrayList){
                    expensesArrayList.addAll(reports.getExpenses());
                }

                ExpenseAdaptor mEAdapter = new ExpenseAdaptor(getBaseContext(),expensesArrayList);
                ListView listView = findViewById(R.id.list);
                listView.setAdapter(mEAdapter);
                return true;
            case R.id.reports:
                listView = findViewById(R.id.list);
                listView.setAdapter(mAdapter);

                return true;
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext() ,MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
