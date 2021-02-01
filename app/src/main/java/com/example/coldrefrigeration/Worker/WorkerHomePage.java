package com.example.coldrefrigeration.Worker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
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

import java.util.Objects;

public class WorkerHomePage extends AppCompatActivity {

   String name, designation, activation, phone, email, security, active_status;
   CardView PendingWork;
   TextView AlertText;
   TextView Wallet;
   SwitchCompat ActiveStatusSwitch;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_worker_home_page);

      PendingWork = findViewById(R.id.pending_work_card);
      CardView WorkHistoryCard = findViewById(R.id.work_history_card);
      ActiveStatusSwitch = findViewById(R.id.active_status_check);

      Intent i = getIntent();
      name = i.getStringExtra("name");
      designation = i.getStringExtra("designation");
      activation = i.getStringExtra("activation");
      phone = i.getStringExtra("phone");
      email = i.getStringExtra("email");
      security = i.getStringExtra("security");
      active_status = i.getStringExtra("active_status");

      TextView Name = findViewById(R.id.name_text);
      String full = "Hello! " + name;
      Name.setText(full);

      ActiveStatusSwitch.setChecked(!active_status.equals("off"));

      Wallet = findViewById(R.id.wallet_amount);
      Wallet.setText(security);
      AlertText = findViewById(R.id.alert_text);

      if(Integer.parseInt(security) < 5000){
         AlertText.setVisibility(View.VISIBLE);
         PendingWork.setVisibility(View.GONE);
      }
      else{
         AlertText.setVisibility(View.GONE);
         PendingWork.setVisibility(View.VISIBLE);
      }

      ActiveStatusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

            String on = "on";
            String off = "off";

            if(isChecked){
               changeStatus(on);
            }
            else{
               changeStatus(off);
            }

         }
      });

      ImageView SignOutButton = findViewById(R.id.sign_out_btn);
      SignOutButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            ProgressDialog progressDialog = new ProgressDialog(WorkerHomePage.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Logging out...");
            progressDialog.show();

            firebaseFirestore.collection("Members")
                    .document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                    .update("active_status","off")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {

                          SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                          SharedPreferences.Editor editor = preferences.edit();
                          editor.putInt("login", SharedPrefConsts.NO_LOGIN);
                          editor.apply();

                          firebaseAuth.signOut();
                          progressDialog.dismiss();
                          finish();
                          startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                       }
                    }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {

                  Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                  progressDialog.dismiss();

               }
            });

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

      ImageView RefreshButton = findViewById(R.id.refresh_btn);
      RefreshButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            String loading = "Loading...";
            Wallet.setText(loading);

            String id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            firebaseFirestore.collection("Members").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
               @Override
               public void onSuccess(DocumentSnapshot documentSnapshot) {

                  int sec = Integer.parseInt(String.valueOf(documentSnapshot.get("security")));
                  Wallet.setText(String.valueOf(sec));
                  Toast.makeText(getApplicationContext(),"Amount updated.",Toast.LENGTH_LONG).show();

                  if(sec < 3000){
                     AlertText.setVisibility(View.VISIBLE);
                     PendingWork.setVisibility(View.GONE);
                  }
                  else{
                     AlertText.setVisibility(View.GONE);
                     PendingWork.setVisibility(View.VISIBLE);
                  }


               }
            }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {

                  Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

               }
            });

         }
      });

   }

   private void changeStatus(String status) {

      FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
      FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

      ProgressDialog progressDialog = new ProgressDialog(WorkerHomePage.this);
      progressDialog.setCancelable(false);
      progressDialog.setMessage("Changing active status...");
      progressDialog.show();

      firebaseFirestore.collection("Members")
              .document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
              .update("active_status",status)
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void aVoid) {

                    Toast.makeText(getApplicationContext(),"Changed your active status to " + status.toUpperCase(),Toast.LENGTH_LONG)
                            .show();
                    progressDialog.dismiss();

                 }
              }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {

            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            progressDialog.dismiss();

         }
      });


   }

   @Override
   public void onBackPressed() {
      WorkerHomePage.super.onBackPressed();
      finish();
   }
}