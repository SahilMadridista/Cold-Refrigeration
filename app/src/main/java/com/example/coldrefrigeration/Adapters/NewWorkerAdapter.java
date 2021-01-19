package com.example.coldrefrigeration.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coldrefrigeration.Model.Members;
import com.example.coldrefrigeration.Model.Services;
import com.example.coldrefrigeration.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedHashMap;
import java.util.Map;

public class NewWorkerAdapter extends FirestoreRecyclerAdapter<Members, NewWorkerAdapter.NewWorkerViewHolder> {

   Context context;

   public NewWorkerAdapter(@NonNull FirestoreRecyclerOptions<Members> options) {
      super(options);
   }


   @Override
   protected void onBindViewHolder(@NonNull NewWorkerViewHolder holder, int position, @NonNull Members model) {

      holder.Name.setText(model.getName());
      holder.Phone.setText(model.getPhone());

      holder.Approve.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            if(holder.SecurityAmount.getText().toString().isEmpty()){
               Toast.makeText(context,"Please enter security amount.",Toast.LENGTH_LONG).show();
               return;
            }

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
            String id = snapshot.getId();

            Map<String,Object> a = new LinkedHashMap<>();
            a.put("activation","yes");
            a.put("designation","WORKER");
            a.put("security",Integer.parseInt(holder.SecurityAmount.getText().toString()));

            firebaseFirestore.collection("Members").document(id)
                    .update(a).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {
                  Toast.makeText(context,"Account activated of " + model.getName(),Toast.LENGTH_LONG).show();
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
   public NewWorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_new_worker_layout,
              parent, false);
      context = v.getContext();
      return new NewWorkerViewHolder(v);

   }

   static class NewWorkerViewHolder extends RecyclerView.ViewHolder{

      TextView Name, Phone;
      EditText SecurityAmount;
      ImageView Approve;

      public NewWorkerViewHolder(@NonNull View itemView) {
         super(itemView);

         Name = itemView.findViewById(R.id.worker_name);
         Phone = itemView.findViewById(R.id.worker_phone);
         SecurityAmount = itemView.findViewById(R.id.security_amount_et);
         Approve = itemView.findViewById(R.id.approve_btn);

      }
   }

}
