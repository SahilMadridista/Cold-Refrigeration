package com.example.coldrefrigeration.Worker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coldrefrigeration.Admin.AdminProfileActivity;
import com.example.coldrefrigeration.Consts.SharedPrefConsts;
import com.example.coldrefrigeration.LoginActivity;
import com.example.coldrefrigeration.R;
import com.google.firebase.auth.FirebaseAuth;

public class WorkerHomePage extends AppCompatActivity {

   String name, designation, activation, phone, email, security;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_worker_home_page);

      Intent i = getIntent();
      name = i.getStringExtra("name");
      designation = i.getStringExtra("designation");
      activation = i.getStringExtra("activation");
      phone = i.getStringExtra("phone");
      email = i.getStringExtra("email");
      security = i.getStringExtra("security");

      TextView Name = findViewById(R.id.name_text);
      String full = "Hello! " + name;
      Name.setText(full);

      TextView Wallet = findViewById(R.id.wallet_amount);
      Wallet.setText(security);

      TextView MyWork = findViewById(R.id.b);
      TextView AlertText = findViewById(R.id.alert_text);

      if(Integer.parseInt(security) < 2000){
         AlertText.setVisibility(View.VISIBLE);
         MyWork.setVisibility(View.GONE);
      }
      else{
         setUpRecyclerView(name);
      }

      ImageView SignOutButton = findViewById(R.id.sign_out_btn);
      SignOutButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("login", SharedPrefConsts.NO_LOGIN);
            editor.apply();

            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

         }
      });

      CardView ProfileCard = findViewById(R.id.card);
      ProfileCard.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            Intent intent = new Intent(getApplicationContext(), WorkerProfileActivity.class);

            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("email",email);

            startActivity(intent);


         }
      });


   }

   private void setUpRecyclerView(String name) {

      Toast.makeText(getApplicationContext(),"Your account is activated currently.",Toast.LENGTH_LONG).show();

   }
}