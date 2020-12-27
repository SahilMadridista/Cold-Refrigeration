package com.example.coldrefrigeration.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.coldrefrigeration.R;

public class AdminHomePage extends AppCompatActivity {

   String name, designation, activation, phone, email;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_admin_home_page);

      Intent i = getIntent();
      name = i.getStringExtra("name");
      designation = i.getStringExtra("designation");
      activation = i.getStringExtra("activation");
      phone = i.getStringExtra("phone");
      email = i.getStringExtra("email");

      TextView Name = findViewById(R.id.name_text);
      String full = "Hello! " + name;
      Name.setText(full);

   }

}