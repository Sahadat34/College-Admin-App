package com.example.collegeadmin.notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collegeadmin.R;
import com.example.collegeadmin.faculty.TeacherModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DeleteNotice extends AppCompatActivity {
    RecyclerView recyclerView;
    public static ArrayList<NoticeModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice);
        recyclerView = findViewById(R.id.delete_notice_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DeleteNotice.this));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notice");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    Toast.makeText(DeleteNotice.this, "No data found", Toast.LENGTH_SHORT).show();
                }else {
                    list = new ArrayList<>();
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        NoticeModel data = snapshot1.getValue(NoticeModel.class);
                        list.add(data);
                    }
                    NoticeAdapter noticeAdapter = new NoticeAdapter(DeleteNotice.this,list);
                    noticeAdapter.setHasStableIds(true);
                    recyclerView.setAdapter(noticeAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DeleteNotice.this, "An error occured!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}