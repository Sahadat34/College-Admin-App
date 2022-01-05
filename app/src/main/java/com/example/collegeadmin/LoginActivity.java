package com.example.collegeadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.SharedMemory;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText username,password;
    Button login;
    String user,pass;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = this.getSharedPreferences("login",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getString("isLogin","false").equals("true")){
            openDash();
        }
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_btn);
        validation();



    }

    private void validation() {
        login.setOnClickListener(v -> {
            user = username.getText().toString();
            pass = password.getText().toString();
            if (username.getText().toString().isEmpty()){
                username.setError("username can't be empty");
                username.requestFocus();
            }else if (username.getText().toString().isEmpty()){
                password.setError("provide password");
                password.requestFocus();
            }else if (user.equals("admin") && pass.equals("1234") ){
                openDash();
            }else {
                Toast.makeText(this, "Check username and password again", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void openDash(){
        editor.putString("isLogin","true");
        editor.commit();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}