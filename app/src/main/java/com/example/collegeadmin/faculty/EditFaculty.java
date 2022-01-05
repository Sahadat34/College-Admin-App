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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.HashMap;

public class EditFaculty extends AppCompatActivity {
    ImageView imagePicker;
    EditText edit_name,edit_email,edit_position;
    Button save,delete;
    final int REQ = 1;
    Bitmap bitmap = null;
    String key,department,name,email,position,downloadUrl,image;
    ProgressDialog pd;

    StorageReference storageReference;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_faculty);
        imagePicker = findViewById(R.id.edit_image_picker);
        edit_name = findViewById(R.id.edit_faculty_name);
        edit_email = findViewById(R.id.edit_faculty_email);
        edit_position = findViewById(R.id.edit_faculty_position);
        save = findViewById(R.id.save_faculty_btn);
        delete = findViewById(R.id.delete_faculty_btn);
        pd = new ProgressDialog(this);
        pd.setTitle("Updating details...");

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        position = getIntent().getStringExtra("position");
        image = getIntent().getStringExtra("image");
        key = getIntent().getStringExtra("key");
        department = getIntent().getStringExtra("department");

        edit_name.setText(name);
        edit_email.setText(email);
        edit_position.setText(position);
        try {
            Picasso.get().load(image).into(imagePicker);
        } catch (Exception e) {
            e.printStackTrace();
        }

        imagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!image.isEmpty()){
                    deleteImage();
                }
                reference = FirebaseDatabase.getInstance().getReference("Faculty");
                DatabaseReference myRef = reference.child(department).child(key);
                myRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditFaculty.this, "Record has been removed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditFaculty.this, "Failed to remove record", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit_name.getText().toString();
                String email = edit_email.getText().toString();
                String position = edit_position.getText().toString();
                checkValidation(name,email,position);
            }
        });

    }

    private void deleteImage() {
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(image);
        image = "";
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(EditFaculty.this, "Removing image", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void checkValidation(String name, String email, String position) {
        if (this.name.isEmpty()){
            edit_name.setError("Please provide name");
            edit_name.requestFocus();
        }else if (this.email.isEmpty()) {
            edit_email.setError("Please provide name");
            edit_email.requestFocus();
        }else if (this.position.isEmpty()){
            edit_position.setError("Please provide name");
            edit_position.requestFocus();
        }if (bitmap == null){
            pd.show();
            updateData();
        }else{
            pd.show();
            UpdateDetails();
        }
    }

    private void UpdateDetails() {
        if (!image.isEmpty()){
            deleteImage();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage = baos.toByteArray();
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference filePath = storageReference.child("profile_pic").child(finalImage+"-jpg");
        final  UploadTask uploadTask = filePath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    updateData();
                                }
                            });
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void updateData() {
        reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = reference.child("Faculty");
        HashMap hashMap = new HashMap<>();
        hashMap.put("name",edit_name.getText().toString());
        hashMap.put("email",edit_email.getText().toString());
        hashMap.put("position",edit_position.getText().toString());
        if (image.isEmpty()){
            hashMap.put("image",downloadUrl);
        }

        myRef.child(department).child(key).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(EditFaculty.this, "Details Updated Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditFaculty.this, "An Error occured!", Toast.LENGTH_SHORT).show();
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
            } catch (IOException e){
                e.printStackTrace();
            }imagePicker.setImageBitmap(bitmap);

        }
    }
}