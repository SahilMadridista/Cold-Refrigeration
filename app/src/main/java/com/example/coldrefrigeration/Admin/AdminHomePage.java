package com.example.coldrefrigeration.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coldrefrigeration.AssignWorkerActivity;
import com.example.coldrefrigeration.BottomSheets.NewWorkerBottomSheet;
import com.example.coldrefrigeration.BottomSheets.WorkersBottomSheet;
import com.example.coldrefrigeration.Consts.SharedPrefConsts;
import com.example.coldrefrigeration.LoginActivity;
import com.example.coldrefrigeration.PendingWorkActivity;
import com.example.coldrefrigeration.R;
import com.example.coldrefrigeration.WorkHistoryActivity;
import com.google.firebase.auth.FirebaseAuth;

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

      CardView ProfileCard = findViewById(R.id.card);
      ProfileCard.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            Intent intent = new Intent(getApplicationContext(),AdminProfileActivity.class);

            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("email",email);

            startActivity(intent);


         }
      });


      CardView NewWorker = findViewById(R.id.new_worker_card);
      NewWorker.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            NewWorkerBottomSheet newWorkerBottomSheet = new NewWorkerBottomSheet();
            newWorkerBottomSheet.show(getSupportFragmentManager(),"new worker");
         }
      });

      CardView Workers = findViewById(R.id.workers_card);
      Workers.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            WorkersBottomSheet workersBottomSheet = new WorkersBottomSheet();
            workersBottomSheet.show(getSupportFragmentManager(),"workers");
         }
      });

      CardView AssignWorkerCard = findViewById(R.id.assign_worker_card);
      AssignWorkerCard.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), AssignWorkerActivity.class));
         }
      });

      CardView PendingWork = findViewById(R.id.pending_card);
      PendingWork.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), PendingWorkActivity.class));
         }
      });

      CardView WorkHistoryCard = findViewById(R.id.work_history_card);
      WorkHistoryCard.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), WorkHistoryActivity.class));
         }
      });

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

   }

}