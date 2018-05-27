package com.example.jay.perdiem.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jay.perdiem.R;
import com.example.jay.perdiem.objects.Expense;
import com.example.jay.perdiem.objects.Reports;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import me.riddhimanadib.formmaster.FormBuilder;
import me.riddhimanadib.formmaster.model.BaseFormElement;
import me.riddhimanadib.formmaster.model.FormElementPickerDate;
import me.riddhimanadib.formmaster.model.FormElementPickerSingle;
import me.riddhimanadib.formmaster.model.FormElementTextMultiLine;
import me.riddhimanadib.formmaster.model.FormElementTextNumber;
import me.riddhimanadib.formmaster.model.FormElementTextSingleLine;

import static android.app.Activity.RESULT_OK;
import static com.example.jay.perdiem.MainActivity.EXTRA_MESSAGE;
import static com.example.jay.perdiem.MainActivity.EXTRA_REPORTS_ARRAY_LIST;


public class AddNewExpenseFragmentCam extends Fragment {

    public static AddNewExpenseFragmentCam newInstance() {
        return new AddNewExpenseFragmentCam();
    }


    private DatabaseReference db;

    private ArrayList<String> titles = new ArrayList<>();

    private final ArrayList<String> cats = new ArrayList<>();
    private final ArrayList<String> titlesCorrectNames = new ArrayList<>();

    private final ArrayList<Reports> report1 = new ArrayList<>();
    private Uri downloadUri;

    private DatabaseReference reportsEndpoint;
    //STATIC FINALS USED FOR REQUESTS
    private static final int REQUEST_IMAGE_CAPTURE = 0x01001;
    private static final int REQUEST_CAMERA_PERMISSION = 0x02001;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 0x02001;
    private static final int REQUEST_BOTH_NEEDED = 0x03001;
    private Boolean photoTaken = false;

    private static final String KEY_TEXT_VALUE = "textValue";


    private FormBuilder mFBnob;
    private FormBuilder mFBdop;
    private FormBuilder mFBmemo;
    private FormBuilder mFBcat;

    private File image;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_expense_new_add, container, false);



    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            titles = savedInstanceState.getStringArrayList(KEY_TEXT_VALUE);
        }
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        getCats();

     buildForm();
     registerListener();



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(KEY_TEXT_VALUE, titles);
    }

    private void getCats(){
        Intent intent = getActivity().getIntent();




        if (!photoTaken) {
            if (!titles.isEmpty()) {
                titles.clear();
            }
            titles = intent.getStringArrayListExtra(EXTRA_REPORTS_ARRAY_LIST);
            titlesCorrectNames.addAll(titles);
            cats.clear();
            cats.add("Computer Related Equipment");
            cats.add("Employee Incentives & Gifts");
            cats.add("Lodging");
            cats.add("Marketing Expense");
            cats.add("Meals");
            cats.add("Gas");
            cats.add("Transportation");


        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu1, menu);
    }

    @SuppressWarnings("unused")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        photoTaken = true;
        Bitmap cameraImage;
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null && data.hasExtra("data")){


            cameraImage = data.getParcelableExtra("data");
            ImageView cameraImageView = getActivity().findViewById(R.id.imageView);
            cameraImageView.setImageBitmap(cameraImage);




        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && (data == null || !data.hasExtra("data"))){

            File outPutFile = image; // Get our output file where the image should be stored.


            Uri contentUri = FileProvider.getUriForFile(getActivity(),"com.example.jay.perdiem",outPutFile);
            addImageToGallery(contentUri);

            FirebaseVisionCloudDetectorOptions options =
                    new FirebaseVisionCloudDetectorOptions.Builder()
                            .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                            .setMaxResults(15)
                            .build();

            FirebaseVisionImage image;
            try {
                image = FirebaseVisionImage.fromFilePath(getActivity(), contentUri);

                FirebaseVisionTextDetector detector = FirebaseVision.getInstance()
                        .getVisionTextDetector();

                Task<FirebaseVisionText> result =
                        detector.detectInImage(image)
                                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                    @Override
                                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                        // Task completed successfully
                                        // ...

                                        for (FirebaseVisionText.Block block: firebaseVisionText.getBlocks()) {
                                            //noinspection unused
                                            Rect boundingBox = block.getBoundingBox();
                                            Point[] cornerPoints = block.getCornerPoints();
                                            String text = block.getText();

                                            //StringBuilder t = new StringBuilder();



//                                            for (FirebaseVisionText.Line line: block.getLines()) {
//
//                                             //   t.append(line.getText());
//
//                                                for (FirebaseVisionText.Element element: line.getElements()) {
//                                                    // ...
////                                                    t.append(element.getText());
//                                                }
//                                            }

                                        }


                                    }
                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                                Toast.makeText(getActivity(),"None found"+ e,Toast.LENGTH_SHORT).show();
                                            }
                                        });

            } catch (IOException e) {
                e.printStackTrace();
            }



            cameraImage = BitmapFactory.decodeFile(outPutFile.getAbsolutePath());   // Decode that file into a bitmap object.

            // Get our ImageView and display the image.
            ImageView cameraImageView = getActivity().findViewById(R.id.imageView);
            cameraImageView.setImageBitmap(cameraImage);

        }
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
        final Button saveButton = getActivity().findViewById(R.id.save);

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
                .setOptions(titlesCorrectNames)
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
                saveButton.setClickable(false);

                uploadImages();


            }
        });

        if(!photoTaken){
            takePhoto();
            photoTaken = true;
        }
    }




    private void registerListener(){
        db = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser fBUser = auth.getCurrentUser();
        reportsEndpoint = db.child("Reports").child(Objects.requireNonNull(fBUser).getUid());
        reportsEndpoint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                titles.clear();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    Reports note = noteSnapshot.getValue(Reports.class);
                        report1.add(note);
                        titles.add(Objects.requireNonNull(note).getIdentifier());
                        getCats();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        db.child("Reports").child("Temp").setValue("");
    }

    @SuppressWarnings("unused")
    private void saveOutToFireBase(){

        db = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser fBUser = auth.getCurrentUser();
        reportsEndpoint = db.child("Reports").child(Objects.requireNonNull(fBUser).getUid());


        db.child("Reports").child("Temp").setValue("");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");


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

                    if(downloadUri != null){
                        expense.setURL(downloadUri.toString());
                    }

                    currentReport.addExpense(expense);
                    db.child("Reports").child(fBUser.getUid()).child(rReportName).child("expenses").setValue(currentReport.getExpenses());
                    getActivity().finish();



                }
            }

















        }else{
            Toast.makeText(getActivity(),"Please complete Form",Toast.LENGTH_SHORT).show();
        }

    }





    private void uploadImages (){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        UploadTask uploadTask;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser fBUser = auth.getCurrentUser();

        Uri file = Uri.fromFile(image);
        StorageReference riversRef = storageRef.child(Objects.requireNonNull(fBUser).getUid()).child(image.getName());
        uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                downloadUri = taskSnapshot.getDownloadUrl();
                saveOutToFireBase();
            }
        });

    }


    private Uri getOutputUri(){
        //Using the current date/time for our file name since it is always unique.
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy_HHmmss");
        Date today = new Date(System.currentTimeMillis());

        String imgName = sdf.format(today);

        //Creating a folder in the public directory for our images.
        File imgDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File appDir = new File(imgDir, "Markers");
        //noinspection ResultOfMethodCallIgnored
        appDir.mkdirs();

        image = new File(appDir, "I-"+imgName + ".jpg");
        try {
            //noinspection ResultOfMethodCallIgnored
            image.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Failed to create file. Do not Start Camera
        }

        return FileProvider.getUriForFile(getActivity(),"com.example.jay.perdiem",image);
    }

    //Add Image to Gallery
    private void addImageToGallery(Uri image){
        //Creating a broadcast to scan the image
        //so that it will show in the gallery
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(image);
        getActivity().sendBroadcast(scanIntent);
    }

    private void permissionCheck(){


        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA },REQUEST_BOTH_NEEDED);
        }else if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.CAMERA},REQUEST_CAMERA_PERMISSION);
        }else if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_EXTERNAL_STORAGE_PERMISSION);

        }
    }

    private void takePhoto(){
        // Check if we've been granted the CAMERA permission.
        permissionCheck();

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED){

            // Open the camera if the permission has been granted.
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //Add our content Uri to our intent as our Output Location
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputUri());
            String message = "Camera";
            cameraIntent.putExtra(EXTRA_MESSAGE, message);
            cameraIntent.putStringArrayListExtra(EXTRA_REPORTS_ARRAY_LIST,titles);

            startActivityForResult(cameraIntent,REQUEST_IMAGE_CAPTURE);

        }else{
            // Request the permission if it hasn't been granted.
            permissionCheck();

            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED){

                // Open the camera if the permission has been granted.
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //Add our content Uri to our intent as our Output Location
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputUri());
                String message = "Camera";
                cameraIntent.putExtra(EXTRA_MESSAGE, message);
                cameraIntent.putStringArrayListExtra(EXTRA_REPORTS_ARRAY_LIST,titles);

                startActivityForResult(cameraIntent,REQUEST_IMAGE_CAPTURE);

            }else{
                Toast.makeText(getActivity(), "Insufficient",Toast.LENGTH_SHORT).show();
            }
        }
    }

}

