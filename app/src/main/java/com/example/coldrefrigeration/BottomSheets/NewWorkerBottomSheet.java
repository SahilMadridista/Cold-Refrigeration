package com.example.coldrefrigeration.BottomSheets;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coldrefrigeration.Adapters.NewWorkerAdapter;
import com.example.coldrefrigeration.Adapters.ServicesAdapter;
import com.example.coldrefrigeration.Model.Members;
import com.example.coldrefrigeration.Model.Services;
import com.example.coldrefrigeration.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class NewWorkerBottomSheet extends BottomSheetDialogFragment {

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      final View v = inflater.inflate(R.layout.new_worker_bottom_sheet, container, false);
      final Context context = v.getContext();
      TextView NoWorker = v.findViewById(R.id.no_worker_text);
      FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

      CollectionReference collectionReference = firebaseFirestore.collection("Members");
      Query query = collectionReference.whereEqualTo("activation","no");

      FirestoreRecyclerOptions<Members> options = new FirestoreRecyclerOptions.Builder<Members>()
              .setQuery(query, Members.class)
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
               NoWorker.setVisibility(View.VISIBLE);
            }

            if(list.size()!=0){
               NoWorker.setVisibility(View.GONE);
            }

         }
      });

      RecyclerView recyclerView = v.findViewById(R.id.new_worker_recyclerview);
      NewWorkerAdapter newWorkerAdapter = new NewWorkerAdapter(options);
      recyclerView.setHasFixedSize(true);
      recyclerView.setLayoutManager(new LinearLayoutManager(context));
      recyclerView.setAdapter(newWorkerAdapter);
      newWorkerAdapter.startListening();


      return v;
   }


}
