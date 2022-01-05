package com.example.collegeadmin.faculty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeadmin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TeacherDataAdapter extends RecyclerView.Adapter<TeacherDataAdapter.Myholder> {
   ArrayList<TeacherModel> teacherModels;
   Context context;

    public TeacherDataAdapter(ArrayList<TeacherModel> teacherModels, Context context) {
        this.teacherModels = teacherModels;
        this.context = context;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Myholder(LayoutInflater.from(context).inflate(R.layout.feculty_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        TeacherModel item = teacherModels.get(position);
        holder.name.setText(item.getName());
        holder.email.setText(item.getEmail());
        holder.position.setText(item.getPosition());
        try {
            Picasso.get().load(item.getImage()).into(holder.profileImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.teacherDataUpdate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,EditFaculty.class)
                        .putExtra("name",item.name)
                        .putExtra("email",item.getEmail())
                        .putExtra("position",item.getPosition())
                        .putExtra("image",item.getImage())
                        .putExtra("key",item.getKey())
                        .putExtra("department",item.getDepartment());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return teacherModels.size();
    }


    public class Myholder extends RecyclerView.ViewHolder {
        TextView name,email,position;
        Button teacherDataUpdate_btn;
        ImageView profileImage;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.teacher_name);
            email = itemView.findViewById(R.id.teacher_email);
            position = itemView.findViewById(R.id.teacher_position);
            teacherDataUpdate_btn = itemView.findViewById(R.id.teacher_update_btn);
            profileImage = itemView.findViewById(R.id.teacher_profile_photo);
        }
    }
}
