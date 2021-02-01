package com.example.coldrefrigeration.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coldrefrigeration.Model.Members;
import com.example.coldrefrigeration.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WorkersAdapter extends FirestoreRecyclerAdapter<Members, WorkersAdapter.WorkersViewHolder> {

   Context context;

   public WorkersAdapter(@NonNull FirestoreRecyclerOptions<Members> options) {
      super(options);
   }

   @Override
   protected void onBindViewHolder(@NonNull WorkersViewHolder holder, int position, @NonNull Members model) {

      holder.Name.setText(model.getName());
      holder.Phone.setText(model.getPhone());
      holder.SecurityAmountText.setText(String.valueOf(model.getSecurity()));

      if(model.getActive_status().equals("off")){
         holder.ActiveStatus.setVisibility(View.GONE);
      }else{
         holder.ActiveStatus.setVisibility(View.VISIBLE);
      }

      holder.DoneButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            if(holder.SecurityET.getText().toString().trim().isEmpty()){
               Toast.makeText(context,"Please enter an amount.",Toast.LENGTH_LONG).show();
               return;
            }

            DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
            String id = snapshot.getId();

            int amount = model.getSecurity();
            int new_amount = Integer.parseInt(holder.SecurityET.getText().toString().trim()) + amount;

            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Updating...");

            progressDialog.show();

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Members").document(id)
                    .update("security",Integer.parseInt(String.valueOf(new_amount))).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {

                  progressDialog.dismiss();
                  Toast.makeText(context,"Amount updated.",Toast.LENGTH_LONG).show();
                  holder.SecurityET.setText("");

               }
            }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {

                  progressDialog.dismiss();
                  Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();

               }
            });


         }
      });


   }

   @NonNull
   @Override
   public WorkersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_worker,
              parent, false);
      context = v.getContext();
      return new WorkersViewHolder(v);
   }

   static class WorkersViewHolder extends RecyclerView.ViewHolder{

      TextView Name, Phone, SecurityAmountText;
      EditText SecurityET;
      Button DoneButton;
      ImageView ActiveStatus;

      public WorkersViewHolder(@NonNull View itemView) {
         super(itemView);

         Name = itemView.findViewById(R.id.worker_name_text);
         Phone = itemView.findViewById(R.id.worker_phone_text);
         SecurityAmountText = itemView.findViewById(R.id.security_amount_text);
         SecurityET = itemView.findViewById(R.id.more_security_et);
         DoneButton = itemView.findViewById(R.id.done_bttn);
         ActiveStatus = itemView.findViewById(R.id.active_status_icon);

      }
   }

}
