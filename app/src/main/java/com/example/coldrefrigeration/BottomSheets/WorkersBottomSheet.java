package com.example.coldrefrigeration.BottomSheets;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coldrefrigeration.Adapters.NewWorkerAdapter;
import com.example.coldrefrigeration.Adapters.WorkersAdapter;
import com.example.coldrefrigeration.Model.Members;
import com.example.coldrefrigeration.Model.Services;
import com.example.coldrefrigeration.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class WorkersBottomSheet extends BottomSheetDialogFragment {

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      final View v = inflater.inflate(R.layout.workers_bottom_sheet, container, false);
      final Context context = v.getContext();
      TextView NoWorker = v.findViewById(R.id.no_worker_text);
      Spinner AreaSpinner = v.findViewById(R.id.area_spinner);
      FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

      ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),
              R.array.loading, R.layout.spinner_item_without_padding);
      adapter.setDropDownViewResource(R.layout.spinner_item);
      AreaSpinner.setAdapter(adapter);


      firebaseFirestore.collection("Members").whereEqualTo("designation","WORKER").get()
              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    ArrayList<String> areas = new ArrayList<>();

                    for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                       areas.add(documentSnapshot.getString("area"));
                    }

                    Set<String> set = new HashSet<>(areas);
                    areas.clear();
                    areas.addAll(set);

                    ArrayAdapter<String> adapter2 =
                            new ArrayAdapter<String>(v.getContext(),
                                    R.layout.spinner_item_without_padding, areas);
                    adapter2.setDropDownViewResource( R.layout.spinner_item);
                    AreaSpinner.setAdapter(adapter2);

                 }
              });

      AreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            String area = adapterView.getItemAtPosition(i).toString().trim();

            CollectionReference collectionReference = firebaseFirestore.collection("Members");
            Query query = collectionReference.whereEqualTo("designation","WORKER")
                    .whereEqualTo("area",area);

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

            RecyclerView recyclerView = v.findViewById(R.id.workers_recyclerview);
            WorkersAdapter workersAdapter = new WorkersAdapter(options);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(workersAdapter);
            workersAdapter.startListening();

         }

         @Override
         public void onNothingSelected(AdapterView<?> adapterView) {

         }
      });

      return v;

   }

}
