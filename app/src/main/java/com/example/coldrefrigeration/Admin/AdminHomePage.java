package com.example.coldrefrigeration.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

      CardView ServicesCard = findViewById(R.id.services_card);
      ServicesCard.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(),ServicesActivity.class));
         }
      });

   }

}