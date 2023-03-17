package com.example.googlelens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
private ImageView captureIV;
private Button snapbtn,getresultsbutton;
private RecyclerView resultsRV;
private SearchRVAdapter searchRVAdapter;
private ArrayList<SearchRVModal> searchRVModalArrayList;
int REQUEST_CODE=1;
private ProgressBar loadingPB;
private Bitmap imageBitmap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        captureIV=findViewById(R.id.image);
        snapbtn=findViewById(R.id.idsnapbutton);
        getresultsbutton=findViewById(R.id.idresultsbutton);
        resultsRV=findViewById(R.id.idRVSearchResults);
        loadingPB=findViewById(R.id.idBPloading);
        searchRVModalArrayList=new ArrayList<>();
        searchRVAdapter=new SearchRVAdapter(this,searchRVModalArrayList);
        resultsRV.setAdapter(searchRVAdapter);
        resultsRV.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
        snapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            searchRVModalArrayList.clear();
            searchRVAdapter.notifyDataSetChanged();
            takePictureIntent();
            }
        });
getresultsbutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        searchRVModalArrayList.clear();
        searchRVAdapter.notifyDataSetChanged();
        loadingPB.setVisibility(View.VISIBLE);
        getResults();

    }
});
    }

    private void getResults(){
        FirebaseVisionImage image=FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionImageLabeler labeler= FirebaseVision.getInstance().getOnDeviceImageLabeler();
        labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                String searchQuery = firebaseVisionImageLabels.get(0).getText();
                getSearchResults(searchQuery);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Fail to detect image..",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK){
            Bundle extras=data.getExtras();
            imageBitmap=(Bitmap) extras.get("data");
            captureIV.setImageBitmap(imageBitmap);

        }
    }

    private void getSearchResults(String searchQuery)
    {
        String url="https://serpapi.com/search.json?q=" + searchQuery.trim() + "&location=Delhi,India&hl=en&gl=us&google_domain=google.com&api_key=f967aa5e1d33d6e61e4a5a1b69dbb6bf770c21b1ef5c4026a5ee718669585625";
        //RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        //JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,new Response<>());
    }
    private void takePictureIntent() {
        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(i.resolveActivity(getPackageManager())!=null){
            startActivityForResult(i,REQUEST_CODE);
        }
    }
}