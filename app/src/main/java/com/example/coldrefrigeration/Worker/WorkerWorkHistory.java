package com.example.coldrefrigeration.Worker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coldrefrigeration.Adapters.WorkHistoryAdapter;
import com.example.coldrefrigeration.Adapters.WorkerWorkAdapter;
import com.example.coldrefrigeration.Adapters.WorkerWorkHistoryAdapter;
import com.example.coldrefrigeration.Model.Bookings;
import com.example.coldrefrigeration.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class WorkerWorkHistory extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_worker_work_history);

      Intent i = getIntent();
      String email = i.getStringExtra("email");

      LottieAnimationView Loading;
      TextView Empty;

      Loading = findViewById(R.id.loading_animation_2);
      Empty = findViewById(R.id.empty_text);

      FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
      CollectionReference collectionReference = firebaseFirestore.collection("Bookings");

      Query query = collectionReference.whereEqualTo("worker_email",email)
              .whereEqualTo("work_status","done");

      FirestoreRecyclerOptions<Bookings> options = new FirestoreRecyclerOptions.Builder<Bookings>()
              .setQuery(query, Bookings.class)
              .build();

      query.addSnapshotListener(new EventListener<QuerySnapshot>() {
         @Override
         public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {

            if (error != null) {
               Log.d("TAG", error.getMessage());
               return;
            }

            assert querySnapshot != null;

            List<Bookings> list = querySnapshot.toObjects(Bookings.class);

            if(list.size() == 0){
               Loading.setVisibility(View.GONE);
               Empty.setVisibility(View.VISIBLE);
            }

            if(list.size()!=0){
               Loading.setVisibility(View.GONE);
               Empty.setVisibility(View.GONE);
            }

         }
      });

      RecyclerView recyclerView = findViewById(R.id.work_history_r_view);
      WorkerWorkHistoryAdapter workerWorkHistoryAdapter = new WorkerWorkHistoryAdapter(options);
      recyclerView.setHasFixedSize(true);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      recyclerView.setAdapter(workerWorkHistoryAdapter);
      workerWorkHistoryAdapter.startListening();

   }
}