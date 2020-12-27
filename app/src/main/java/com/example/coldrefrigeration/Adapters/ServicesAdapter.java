package com.example.coldrefrigeration.Adapters;

import android.content.Context;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coldrefrigeration.Model.Services;
import com.example.coldrefrigeration.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ServicesAdapter extends FirestoreRecyclerAdapter<Services, ServicesAdapter.ServicesViewHolder> {

   Context context;

   public ServicesAdapter(@NonNull FirestoreRecyclerOptions<Services> options) {
      super(options);
   }

   @Override
   protected void onBindViewHolder(@NonNull ServicesViewHolder holder, int position, @NonNull Services model) {

      holder.ServiceName.setText(model.getService_name());
      holder.TotalCost.setText(String.valueOf(model.getTotal_cost()));
      holder.WorkerCost.setText(String.valueOf(model.getWorker_cost()));

      holder.Delete.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
            String id = snapshot.getId();

            firebaseFirestore.collection("Services")
                    .document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {
                  Toast.makeText(context,"Service deleted successfully.",Toast.LENGTH_LONG).show();
               }
            }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {

                  Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();

               }
            });


         }
      });

   }

   @NonNull
   @Override
   public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_service_card,
              parent, false);
      context = v.getContext();
      return new ServicesViewHolder(v);
   }

   static class ServicesViewHolder extends RecyclerView.ViewHolder{

      TextView ServiceName, TotalCost, WorkerCost;
      ImageView Delete;

      public ServicesViewHolder(@NonNull View itemView) {
         super(itemView);

         ServiceName = itemView.findViewById(R.id.service_name_text);
         TotalCost = itemView.findViewById(R.id.total_amount_text);
         WorkerCost = itemView.findViewById(R.id.worker_amount_text);
         Delete = itemView.findViewById(R.id.delete_service);

      }
   }

}
