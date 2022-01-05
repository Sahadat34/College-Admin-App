package com.example.collegeadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;

public class UploadPdf extends AppCompatActivity {
    CardView pdf_picker;
    EditText pdf_title;
    Uri pdfData;
    String downloadUrl = "";
    Button button;
    ProgressDialog pd;
    int REQ = 1;
    TextView pdfName;
    String fileName,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        pdf_picker = findViewById(R.id.pdf_picker);
        pdf_title = findViewById(R.id.pdf_title);
        button = findViewById(R.id.upload_pdf_btn);
        pdfName = findViewById(R.id.pdfName);

        pd = new ProgressDialog(this);
        pd.setTitle("Pdf uploading...");

        pdf_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStorage();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = pdf_title.getText().toString();
                if (title.isEmpty()){
                    pdf_title.setError("Please provide pdf title");
                }else if (pdfData == null){
                    Toast.makeText(UploadPdf.this, "Please select a pdf file", Toast.LENGTH_SHORT).show();
                }else {
                    pd.show();
                    uploadPdf();
                }
            }
        });
    }

    private void uploadPdf() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference filePath = storageReference.child("pdf/"+fileName+"_"+System.currentTimeMillis()+".pdf");
        filePath.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri uri = uriTask.getResult();
                uploadData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadPdf.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData(String downloadUrl) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        String uniqueKey = myRef.push().getKey();
        HashMap data = new HashMap();
        data.put("pdfTitle",title);
        data.put("pdfUrl",downloadUrl);
        myRef.child("pdf").child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UploadPdf.this, "Pdf Uploaded Successfully", Toast.LENGTH_SHORT).show();
                pdf_title.setText("");
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadPdf.this,"Failed to upload Pdf",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openStorage() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Chose pdf file"),REQ);
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK){
            pdfData = data.getData();
            if (pdfData.toString().startsWith("content://")){
                Cursor cursor = UploadPdf.this.getContentResolver().query(pdfData,null,null,null);
                if (cursor!=null && cursor.moveToFirst()){
                    fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }

            }else if(pdfData.toString().startsWith("file://")) {
                fileName = new File(pdfData.toString()).getName();

            }
            pdfName.setText(fileName);
        }
    }
}