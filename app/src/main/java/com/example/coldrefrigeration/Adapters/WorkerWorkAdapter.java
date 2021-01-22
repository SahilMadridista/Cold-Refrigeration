package com.example.coldrefrigeration.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class WorkerWorkAdapter extends FirestoreRecyclerAdapter<Bookings, WorkerWorkAdapter.WorkerWorkViewHolder> {

   Context context;

   public WorkerWorkAdapter(@NonNull FirestoreRecyclerOptions<Bookings> options) {
      super(options);
   }

   @Override
   protected void onBindViewHolder(@NonNull WorkerWorkViewHolder holder, int position, @NonNull Bookings model) {

      String ser = model.getService_name() + " , " + model.getService_cost();

      holder.Name.setText(model.getCustomer_name());
      holder.Phone.setText(model.getCustomer_phone());
      holder.ServiceName.setText(ser);
      holder.Address.setText(model.getCustomer_address());

      holder.DoneButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            Calendar c = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            String date = DateFormat.getDateInstance(DateFormat.DEFAULT).format(c.getTime());

            final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
            String id = snapshot.getId();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setMessage("Work completed ?").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(final DialogInterface dialogInterface, int i) {


                          Map<String,Object> remove = new LinkedHashMap<>();
                          remove.put("work_status","done");
                          remove.put("booking_complete_date",date);

                          firestore.collection("Bookings").document(id).update(remove)
                                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void aVoid) {

                                        dialogInterface.cancel();
                                        Toast.makeText(context,
                                                "Work completed.",Toast.LENGTH_LONG).show();

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

      holder.NotAvailableButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Removing your name from this booking...");

            DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
            String id = snapshot.getId();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setMessage("Confirm your unavailability ?").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(final DialogInterface dialogInterface, int i) {

                          progressDialog.show();

                          Map<String,Object> remove = new LinkedHashMap<>();
                          remove.put("worker_name","");
                          remove.put("worker_email","");
                          remove.put("worker_phone","");
                          remove.put("worker_allotment_status","no");

                          firestore.collection("Bookings").document(id).update(remove)
                                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void aVoid) {

                                        dialogInterface.cancel();
                                        progressDialog.dismiss();
                                        Toast.makeText(context,
                                                "Your name removed successfully from this booking.",Toast.LENGTH_LONG).show();

                                     }
                                  }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {

                                dialogInterface.cancel();
                                progressDialog.dismiss();
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
   public WorkerWorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_worker_pending_work,
              parent, false);
      context = v.getContext();
      return new WorkerWorkViewHolder(v);

   }

   static class WorkerWorkViewHolder extends RecyclerView.ViewHolder{

      TextView Name, ServiceName, Address, WorkerName, Phone;
      Button NotAvailableButton, DoneButton;

      public WorkerWorkViewHolder(@NonNull View itemView) {
         super(itemView);

         Name = itemView.findViewById(R.id.customer_name_text);
         ServiceName = itemView.findViewById(R.id.service_name_text);
         Address = itemView.findViewById(R.id.address_text);
         Phone = itemView.findViewById(R.id.customer_phone_text);
         NotAvailableButton = itemView.findViewById(R.id.not_available_button);
         DoneButton = itemView.findViewById(R.id.done_button);


      }
   }



}
