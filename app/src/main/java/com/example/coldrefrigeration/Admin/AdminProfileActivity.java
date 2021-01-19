package com.example.coldrefrigeration.Admin;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.coldrefrigeration.R;

public class AdminProfileActivity extends AppCompatActivity {

   TextView Name, Email, Phone;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_admin_profile);

      Intent i = getIntent();

      String name = i.getStringExtra("name");
      String email = i.getStringExtra("email");
      String phone = i.getStringExtra("phone");

      Name = findViewById(R.id.name_text);
      Email = findViewById(R.id.email_text);
      Phone = findViewById(R.id.phone_text);

      Name.setText(name);
      Email.setText(email);
      Phone.setText(phone);

      LinearLayout ChangePasswordCard = findViewById(R.id.change_pass_layout);
      ChangePasswordCard.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {



         }
      });

   }
}