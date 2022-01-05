package com.example.collegeadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.collegeadmin.faculty.UpdateFaculty;
import com.example.collegeadmin.notice.DeleteNotice;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    CardView add_notice,add_gallery,add_pdf,delete_notice,add_faculty,exit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getString("isLogin","false").equals("false")){
           openLogin();
        }

        initView();

    }


    private void initView() {
        add_notice = findViewById(R.id.add_notice_Card);
        add_notice.setOnClickListener(this);

        add_gallery = findViewById(R.id.add_gallery_Card);
        add_gallery.setOnClickListener(this);

        add_pdf = findViewById(R.id.add_ebooks_Card);
        add_pdf.setOnClickListener(this);

        add_faculty = findViewById(R.id.add_feculty_Card);
        add_faculty.setOnClickListener(this);

        delete_notice = findViewById(R.id.delete_notice_Card);
        delete_notice.setOnClickListener(this);

        exit = findViewById(R.id.exit_Card);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

           case R.id.add_notice_Card:
                 startActivity(new Intent(MainActivity.this, UploadNotice.class));
                 break;

           case R.id.add_gallery_Card:
               startActivity(new Intent(MainActivity.this, UploadGallery.class));
               break;
           case R.id.add_ebooks_Card:
               startActivity(new Intent(MainActivity.this, UploadPdf.class));
               break;
           case R.id.add_feculty_Card:
               startActivity(new Intent(MainActivity.this, UpdateFaculty.class));
               break;
           case R.id.delete_notice_Card:
               startActivity(new Intent(MainActivity.this, DeleteNotice.class));
               break;
           case R.id.exit_Card:
               editor.putString("isLogin","false");
               editor.commit();
               openLogin();



            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }

    }
   private void  openLogin(){
       startActivity(new Intent(this,LoginActivity.class));
       finish();
    }
}
