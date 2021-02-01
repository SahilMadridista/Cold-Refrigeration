package com.example.coldrefrigeration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.coldrefrigeration.Admin.AdminHomePage;
import com.example.coldrefrigeration.Consts.SharedPrefConsts;
import com.example.coldrefrigeration.Worker.WorkerHomePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
      final int loginStatus = preferences.getInt("login", SharedPrefConsts.NO_LOGIN);

      new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {

            if(loginStatus == SharedPrefConsts.ADMIN_LOGIN){

               FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
               FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
               assert firebaseAuth.getCurrentUser() != null;
               String id = firebaseAuth.getCurrentUser().getUid();

               final ArrayList<String> list = new ArrayList<>();
               firebaseFirestore.collection("Members").whereEqualTo("designation",
                       "WORKER").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<QuerySnapshot> task) {

                     for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                        list.add(documentSnapshot.getString("name"));
                     }

                  }
               });

               firebaseFirestore.collection("Members").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                  @Override
                  public void onSuccess(DocumentSnapshot documentSnapshot) {

                     assert documentSnapshot != null;
                     String name = documentSnapshot.getString("name");
                     String designation = documentSnapshot.getString("designation");
                     String activation = documentSnapshot.getString("activation");
                     String phone = documentSnapshot.getString("phone");
                     String email = documentSnapshot.getString("email");

                     Intent i = new Intent(getApplicationContext(), AdminHomePage.class);
                     i.putExtra("name",name);
                     i.putExtra("designation",designation);
                     i.putExtra("activation",activation);
                     i.putExtra("email",email);
                     i.putExtra("phone",phone);
                     i.putExtra("worker_list",list);
                     startActivity(i);
                     finish();

                  }
               });

            }

            else if(loginStatus == SharedPrefConsts.WORKER_LOGIN){

               FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
               FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
               assert firebaseAuth.getCurrentUser() != null;
               String id = firebaseAuth.getCurrentUser().getUid();
               firebaseFirestore.collection("Members").document(id)
                       .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                  @Override
                  public void onSuccess(DocumentSnapshot documentSnapshot) {

                     assert documentSnapshot != null;
                     String name = documentSnapshot.getString("name");
                     String designation = documentSnapshot.getString("designation");
                     String activation = documentSnapshot.getString("activation");
                     String phone = documentSnapshot.getString("phone");
                     String email = documentSnapshot.getString("email");
                     String security = String.valueOf(documentSnapshot.get("security"));
                     String active_status = String.valueOf(documentSnapshot.get("active_status"));

                     Intent i = new Intent(getApplicationContext(), WorkerHomePage.class);
                     i.putExtra("name",name);
                     i.putExtra("designation",designation);
                     i.putExtra("activation",activation);
                     i.putExtra("email",email);
                     i.putExtra("phone",phone);
                     i.putExtra("security",security);
                     i.putExtra("active_status",active_status);
                     startActivity(i);
                     finish();

                  }
               });

            }


            else {

               new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {

                     startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                     finish();

                  }
               },0);

            }

         }
      },1000);

   }
}