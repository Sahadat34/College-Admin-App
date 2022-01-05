package com.example.collegeadmin.notice;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeadmin.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.myHolder> {
    Context context;
    ArrayList<NoticeModel> list;

    public NoticeAdapter(Context context, ArrayList<NoticeModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_feed_layout,parent,false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        NoticeModel item = list.get(position);
        holder.notice_title.setText(item.title);
        try {
            Picasso.get().load(item.image).into(holder.notice_image_view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.time.setText(item.getTime());
        holder.date.setText(item.getDate());

        holder.delete_notice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage("Are you sure want to delete this notice ?");
                alertDialog.setCancelable(true);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!item.image.isEmpty()){
                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(item.image);
                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Notice image removed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Notice");
                        myRef.child(item.uniqueKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Notice removed from Database", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "An Error occured!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = null;
                try{
                    dialog = alertDialog.create();
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (dialog!=null){
                    dialog.show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class myHolder extends RecyclerView.ViewHolder{
        ImageView notice_image_view;
        Button delete_notice_btn;
        TextView notice_title,time,date;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            notice_image_view = itemView.findViewById(R.id.delete_notice_image_view);
            delete_notice_btn = itemView.findViewById(R.id.delete_notice_btn);
            notice_title = itemView.findViewById(R.id.delete_notice_title);
            time = itemView.findViewById(R.id.time_value);
            date = itemView.findViewById(R.id.date_value);
        }
        
    }
}
