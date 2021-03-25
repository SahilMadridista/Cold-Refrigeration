package com.example.coldrefrigeration.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coldrefrigeration.Model.Bookings;
import com.example.coldrefrigeration.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WorkHistoryAdapter extends FirestoreRecyclerAdapter<Bookings, WorkHistoryAdapter.WorkHistoryViewHolder> {

   Context context;

   public WorkHistoryAdapter(@NonNull FirestoreRecyclerOptions<Bookings> options) {
      super(options);
   }

   @Override
   protected void onBindViewHolder(@NonNull WorkHistoryViewHolder holder, int position, @NonNull Bookings model) {

      String ser = model.getService_name() + " , " + model.getService_cost();

      holder.Name.setText(model.getCustomer_name());
      holder.Phone.setText(model.getCustomer_phone());
      holder.ServiceName.setText(ser);
      holder.Address.setText(model.getCustomer_address());
      holder.Date.setText(model.getBooking_date());
      holder.WorkerName.setText(model.getWorker_name());

      holder.DeleteBooking.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());

            String id = snapshot.getId();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setMessage("Do you really want to delete this booking ?").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(final DialogInterface dialogInterface, int i) {

                          firestore.collection("Bookings").document(id)
                                  .delete()
                                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void aVoid) {
                                        dialogInterface.cancel();
                                        Toast.makeText(context,"Booking deleted successfully.",Toast.LENGTH_LONG).show();

                                     }
                                  }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                dialogInterface.cancel();
                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                             }
                          });

                       }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                  dialogInterface.cancel();
               }
            });

            AlertDialog alert = builder.create();
            alert.show();

         }
      });


   }

   @NonNull
   @Override
   public WorkHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_worker_work_history,
              parent, false);
      context = v.getContext();
      return new WorkHistoryViewHolder(v);
   }

   static class WorkHistoryViewHolder extends RecyclerView.ViewHolder{

      TextView Name, ServiceName, Address, WorkerName, Phone, Date;
      Button DeleteBooking;

      public WorkHistoryViewHolder(@NonNull View itemView) {

         super(itemView);

         Name = itemView.findViewById(R.id.customer_name_text);
         ServiceName = itemView.findViewById(R.id.service_name_text);
         Address = itemView.findViewById(R.id.address_text);
         WorkerName = itemView.findViewById(R.id.worker_name_text);
         Phone = itemView.findViewById(R.id.customer_phone_text);
         Date = itemView.findViewById(R.id.date_text);
         DeleteBooking = itemView.findViewById(R.id.delete_history);

      }
   }

}
