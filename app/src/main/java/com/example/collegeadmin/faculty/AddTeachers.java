package com.example.collegeadmin.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.collegeadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddTeachers extends AppCompatActivity {
    ImageView profile;
    EditText name,position,email;
    Spinner department;
    Button button;
    final int REQ = 1;
    Bitmap bitmap = null;
    String downloadUrl = "";
    String departMent = "";
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teachers);
        name = findViewById(R.id.faculty_name);
        profile = findViewById(R.id.circleImageView);
        position = findViewById(R.id.faculty_position);
        email = findViewById(R.id.faculty_email);
        department = findViewById(R.id.faculty_spinner);
        button = findViewById(R.id.save_faculty_btn);
        pd = new ProgressDialog(this);
        pd.setTitle("Adding faculty information");

        String[] items = new String[]{"Select department","CSE","CIVIL","MECHANICAL","ELECTRICAL","ELECTRONICS"};
        department.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));
        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departMent = department.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                departMent = "Select department";
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()){
                    name.setError("Provide name");
                    name.requestFocus();
                }else if (email.getText().toString().isEmpty()){
                    email.setError("Provide email");
                    email.requestFocus();
                }else if (departMent == "Select department"){
                    Toast.makeText(AddTeachers.this, "Select department", Toast.LENGTH_SHORT).show();
                }else if (bitmap == null){
                    pd.show();
                    uploadData();
                }else {
                    pd.show();
                    uploadTeacherData();
                }
            }
        });
    }

    private void uploadTeacherData() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage = baos.toByteArray();

        StorageReference filePath = storageReference.child("profile_pic").child(finalImage+"-jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(AddTeachers.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                }
            }
        });


    }

    private void uploadData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Faculty");
        String uniqueKey = myRef.push().getKey();
        String fName = name.getText().toString();
        String fEmail = email.getText().toString();
        String fPos = position.getText().toString();

        TeacherModel teacherModel = new TeacherModel(fName,fEmail,fPos,departMent,downloadUrl,uniqueKey);
        myRef.child(departMent).child(uniqueKey).setValue(teacherModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(AddTeachers.this,"Data uploaded",Toast.LENGTH_SHORT).show();
                name.setText("");
                email.setText("");
                position.setText("");
                profile.setImageResource(R.drawable.p_image);
                        finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }profile.setImageBitmap(bitmap);
        }
    }
}