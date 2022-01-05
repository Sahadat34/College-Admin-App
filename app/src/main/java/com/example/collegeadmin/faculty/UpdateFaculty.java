package com.example.collegeadmin.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.collegeadmin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdateFaculty extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView cse_recycler,civil_recycler,mechanical_recycler,electrical_recycler,electronics_recycler;
    LinearLayout cse_noData,civil_noData,mechanical_noData,electrical_noData,electronics_noData;
    public static ArrayList<TeacherModel> cse_list,civil_list,mechanical_list,electrical_list,electronics_list;
    TeacherDataAdapter adapter;
    DatabaseReference reference,dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_feculty);

        cse_recycler = findViewById(R.id.cse_recycler);
        civil_recycler = findViewById(R.id.civil_recycler);
        mechanical_recycler = findViewById(R.id.mechanical_recycler);
        electrical_recycler = findViewById(R.id.electrical_recycler);
        electronics_recycler = findViewById(R.id.electronics_recycler);

        cse_noData = findViewById(R.id.cse_noData);
        civil_noData = findViewById(R.id.civil_noData);
        mechanical_noData = findViewById(R.id.mechanical_noData);
        electrical_noData = findViewById(R.id.electrical_noData);
        electronics_noData = findViewById(R.id.electronics_noData);

        reference = FirebaseDatabase.getInstance().getReference("Faculty");

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateFaculty.this,AddTeachers.class));
            }
        });



        civilDepartment();
        mechanicalDepartment();
        electricalDepartment();
        electronicDepartment();
        cseDepartment();
    }

    private void electronicDepartment() {
        dbRef = reference.child("ELECTRONICS");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                electronics_list = new ArrayList<>();
                if (!snapshot.exists()){
                    electronics_noData.setVisibility(View.VISIBLE);
                    electronics_recycler.setVisibility(View.GONE);
                }else {
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        TeacherModel data = snapshot1.getValue(TeacherModel.class);
                        electronics_list.add(data);
                    }
                    electronics_noData.setVisibility(View.GONE);
                    electronics_recycler.setVisibility(View.VISIBLE);
                    electronics_recycler.setHasFixedSize(true);
                    electronics_recycler.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherDataAdapter(electronics_list, UpdateFaculty.this);
                    adapter.setHasStableIds(true);
                    electronics_recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void electricalDepartment() {
        dbRef = reference.child("ELECTRICAL");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                electrical_list = new ArrayList<>();
                if (!snapshot.exists()){
                    electrical_noData.setVisibility(View.VISIBLE);
                    electrical_recycler.setVisibility(View.GONE);
                }else {
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        TeacherModel data = snapshot1.getValue(TeacherModel.class);
                        electrical_list.add(data);
                    }
                    electrical_noData.setVisibility(View.GONE);
                    electrical_recycler.setVisibility(View.VISIBLE);
                    electrical_recycler.setHasFixedSize(true);
                    electrical_recycler.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherDataAdapter(electrical_list, UpdateFaculty.this);
                    adapter.setHasStableIds(true);
                    electrical_recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, "Something error occur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mechanicalDepartment() {
        dbRef = reference.child("MECHANICAL");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mechanical_list = new ArrayList<>();
                if (!snapshot.exists()){
                    mechanical_recycler.setVisibility(View.GONE);
                    mechanical_noData.setVisibility(View.VISIBLE);
                }else {
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        TeacherModel data = snapshot1.getValue(TeacherModel.class);
                        mechanical_list.add(data);
                    }
                    mechanical_recycler.setVisibility(View.VISIBLE);
                    mechanical_noData.setVisibility(View.GONE);
                    mechanical_recycler.setHasFixedSize(true);
                    mechanical_recycler.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherDataAdapter(mechanical_list, UpdateFaculty.this);
                    adapter.setHasStableIds(true);
                    mechanical_recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, "Something error occur", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void civilDepartment() {
        dbRef = reference.child("CIVIL");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                civil_list = new ArrayList<>();
                if (!snapshot.exists()){
                    civil_noData.setVisibility(View.VISIBLE);
                    civil_recycler.setVisibility(View.GONE);
                }else {
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        TeacherModel data = snapshot1.getValue(TeacherModel.class);
                        civil_list.add(data);
                    }
                    civil_recycler.setVisibility(View.VISIBLE);
                    civil_noData.setVisibility(View.GONE);
                    civil_recycler.setHasFixedSize(true);
                    civil_recycler.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherDataAdapter(civil_list, UpdateFaculty.this);
                    adapter.setHasStableIds(true);
                    civil_recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, "Something error occur", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void cseDepartment() {
        dbRef = reference.child("CSE");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cse_list = new ArrayList<>();
                if (!snapshot.exists()){
                    cse_recycler.setVisibility(View.GONE);
                    cse_noData.setVisibility(View.VISIBLE);
                }else {
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        TeacherModel data = snapshot1.getValue(TeacherModel.class);
                        cse_list.add(data);
                    }
                    cse_recycler.setVisibility(View.VISIBLE);
                    cse_noData.setVisibility(View.GONE);
                    cse_recycler.setHasFixedSize(true);
                    cse_recycler.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherDataAdapter(cse_list, UpdateFaculty.this);
                    adapter.setHasStableIds(true);
                    cse_recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, "Something error occur", Toast.LENGTH_SHORT).show();
            }
        });
    }
}