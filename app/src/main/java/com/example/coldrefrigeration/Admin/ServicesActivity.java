package com.example.coldrefrigeration.Admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coldrefrigeration.Adapters.ServicesAdapter;
import com.example.coldrefrigeration.BottomSheets.AddServiceBottomSheet;
import com.example.coldrefrigeration.Model.Services;
import com.example.coldrefrigeration.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ServicesActivity extends AppCompatActivity {


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_services);

      TextView NoService = findViewById(R.id.no_service_text);
      FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

      CollectionReference collectionReference = firebaseFirestore.collection("Services");

      Query query = collectionReference.orderBy("service_name", Query.Direction.ASCENDING);
      FirestoreRecyclerOptions<Services> options = new FirestoreRecyclerOptions.Builder<Services>()
              .setQuery(query, Services.class)
              .build();

      query.addSnapshotListener(new EventListener<QuerySnapshot>() {
         @Override
         public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {

            if (error != null) {
               Log.d("TAG", error.getMessage());
               return;
            }

            assert querySnapshot != null;

            List<Services> list = querySnapshot.toObjects(Services.class);

            if(list.size() == 0){
               NoService.setVisibility(View.VISIBLE);
            }

            if(list.size()!=0){
               NoService.setVisibility(View.GONE);
            }

         }
      });

      RecyclerView recyclerView = findViewById(R.id.services_recyclerview);
      ServicesAdapter servicesAdapter = new ServicesAdapter(options);
      recyclerView.setHasFixedSize(true);
      recyclerView.setLayoutManager(new LinearLayoutManager(this));
      recyclerView.setAdapter(servicesAdapter);
      servicesAdapter.startListening();

      ImageView AddServices = findViewById(R.id.add_service_btn);
      AddServices.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            AddServiceBottomSheet addServiceBottomSheet = new AddServiceBottomSheet();
            addServiceBottomSheet.show(getSupportFragmentManager(),"example sheet");

         }
      });


   }


}