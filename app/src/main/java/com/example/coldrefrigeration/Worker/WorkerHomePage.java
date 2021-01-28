package com.example.coldrefrigeration.Worker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coldrefrigeration.Consts.SharedPrefConsts;
import com.example.coldrefrigeration.LoginActivity;
import com.example.coldrefrigeration.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WorkerHomePage extends AppCompatActivity {

   String name, designation, activation, phone, email, security;
   CardView PendingWork;
   TextView AlertText;
   TextView Wallet;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_worker_home_page);

      PendingWork = findViewById(R.id.pending_work_card);
      CardView WorkHistoryCard = findViewById(R.id.work_history_card);

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

      Wallet = findViewById(R.id.wallet_amount);
      Wallet.setText(security);

      AlertText = findViewById(R.id.alert_text);

      if(Integer.parseInt(security) < 2000){
         AlertText.setVisibility(View.VISIBLE);
         PendingWork.setVisibility(View.GONE);
      }
      else{
         Toast.makeText(getApplicationContext(),"Your account is activated currently.",Toast.LENGTH_LONG).show();
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


      PendingWork.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            Intent i = new Intent(getApplicationContext(),WorkerPendingWorkActivity.class);
            i.putExtra("email",email);
            startActivity(i);

         }
      });

      WorkHistoryCard.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            Intent i = new Intent(getApplicationContext(),WorkerWorkHistory.class);
            i.putExtra("email",email);
            startActivity(i);

         }
      });

   }


   /*@Override
   protected void onStart() {
      super.onStart();

      ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
      progressDialog.setCancelable(false);
      progressDialog.setMessage("Loading details...");

      FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

      firebaseFirestore.collection("Members").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
              .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
         @Override
         public void onSuccess(DocumentSnapshot documentSnapshot) {

            String security = String.valueOf(documentSnapshot.get("security"));
            Wallet.setText(security);

            if(Integer.parseInt(security) < 2000){
               AlertText.setVisibility(View.VISIBLE);
               PendingWork.setVisibility(View.GONE);
            }

         }
      }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {

            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

         }
      });


   }*/

   @Override
   public void onBackPressed() {
      WorkerHomePage.super.onBackPressed();
      finish();
   }
}