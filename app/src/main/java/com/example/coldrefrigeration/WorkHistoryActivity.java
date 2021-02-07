package com.example.coldrefrigeration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coldrefrigeration.Adapters.PendingAdapter;
import com.example.coldrefrigeration.Adapters.WorkHistoryAdapter;
import com.example.coldrefrigeration.Model.Bookings;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class WorkHistoryActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_work_history);

      LottieAnimationView Loading;
      TextView Empty;

      Loading = findViewById(R.id.loading_animation);
      Empty = findViewById(R.id.empty_text);

      FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

      CollectionReference collectionReference = firebaseFirestore.collection("Bookings");
      Query query = collectionReference.whereEqualTo("work_status","done");

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
            }

         }
      });

      RecyclerView recyclerView = findViewById(R.id.pending_work_r_view);
      WorkHistoryAdapter workHistoryAdapter = new WorkHistoryAdapter(options);
      recyclerView.setHasFixedSize(true);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      recyclerView.setAdapter(workHistoryAdapter);
      workHistoryAdapter.startListening();

   }

}