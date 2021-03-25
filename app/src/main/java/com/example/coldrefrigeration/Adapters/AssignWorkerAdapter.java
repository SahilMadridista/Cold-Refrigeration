package com.example.coldrefrigeration.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.coldrefrigeration.Model.Bookings;
import com.example.coldrefrigeration.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class AssignWorkerAdapter extends FirestoreRecyclerAdapter<Bookings, AssignWorkerAdapter.AssignWorkerViewHolder> {

   Context context;
   String email,phone;

   public AssignWorkerAdapter(@NonNull FirestoreRecyclerOptions<Bookings> options) {
      super(options);
   }

   @Override
   protected void onBindViewHolder(@NonNull AssignWorkerViewHolder holder, int position, @NonNull Bookings model) {

      holder.Name.setText(model.getCustomer_name());
      holder.ServiceName.setText(model.getService_name());
      holder.Address.setText(model.getCustomer_address());

      holder.AssignButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Allotting worker...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                    firebaseFirestore.collection("Members").whereEqualTo("designation","WORKER")
                    .whereEqualTo("name",holder.WorkerSpinner.getSelectedItem().toString().trim()).get().
            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {

                  for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                     email = documentSnapshot.getString("email");
                     phone = documentSnapshot.getString("phone");
                  }

                  Map<String,Object> allotment = new LinkedHashMap<>();
                  allotment.put("worker_allotment_status","yes");
                  allotment.put("worker_name",holder.WorkerSpinner.getSelectedItem().toString().trim());
                  allotment.put("worker_email",email);
                  allotment.put("worker_phone",phone);

                  DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                  String id = snapshot.getId();

                  firebaseFirestore.collection("Bookings").document(id).update(allotment)
                          .addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {

                                Toast.makeText(context,"Worker allotted successfully.",Toast.LENGTH_LONG).show();

                                try {
                                   sendSMS(phone);
                                } catch (UnsupportedEncodingException e) {
                                   e.printStackTrace();
                                }

                                progressDialog.dismiss();


                             }
                          }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {

                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                     }
                  });

               }
            });

         }
      });


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

   private void sendSMS(String phone) throws UnsupportedEncodingException {

      String apiKey = "vnOaGc8URfFmKbpwdYuEBe76ijNVs3HrMqyk2zI1QX4TJWAtZ5E2I6Wb5HaBomxrCXZ9QTv4qKM3lcRt";
      String sendId = "FSTSMS";

      System.out.println("_________________________________________________________");

      String message = "You have been assigned a work. Please look into it and provide the best service.";

      //important step...
      message = URLEncoder.encode(message, "UTF-8");
      String language = "english";

      String route = "p";

      String myUrl = "https://www.fast2sms.com/dev/bulk?authorization=" + apiKey + "&sender_id=" + sendId + "&message=" + message + "&language=" + language + "&route=" + route + "&numbers=" + phone;

      StringRequest requestSms = new StringRequest(myUrl, new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {

            JSONObject object = null;

            try {

               object = new JSONObject(response);
               String ret = object.getString("return");
               String reqId = object.getString("request_id");
               JSONArray dataArray = object.getJSONArray("message");
               String res = dataArray.getString(0);

            } catch (JSONException e) {
               e.printStackTrace();

            }

         }
      }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError volleyError) {

            Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();

         }
      });

      RequestQueue rQueue = Volley.newRequestQueue(context);
      rQueue.add(requestSms);

   }

   @NonNull
   @Override
   public AssignWorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_assign_worker,
              parent, false);
      context = v.getContext();
      return new AssignWorkerViewHolder(v);
   }

   static class AssignWorkerViewHolder extends RecyclerView.ViewHolder{

      TextView Name, ServiceName, Address;
      Spinner WorkerSpinner;
      Button AssignButton, DeleteBooking;

      public AssignWorkerViewHolder(@NonNull View itemView) {
         super(itemView);

         Name = itemView.findViewById(R.id.customer_name_text);
         ServiceName = itemView.findViewById(R.id.service_name_text);
         Address = itemView.findViewById(R.id.address_text);
         WorkerSpinner = itemView.findViewById(R.id.worker_spinner);
         AssignButton = itemView.findViewById(R.id.assign_btn);
         DeleteBooking = itemView.findViewById(R.id.delete_booking);


         FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

         firebaseFirestore.collection("Members")
                 .whereEqualTo("designation", "WORKER")
                 .whereEqualTo("active_status","on")
                 .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

               ArrayList<String> worker_spinner = new ArrayList<>();

               for(DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                  worker_spinner.add(documentSnapshot.getString("name"));
               }

               if(worker_spinner.isEmpty()){

                  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(itemView.getContext(),
                          R.array.no_active_worker, R.layout.spinner_item_without_padding);
                  adapter.setDropDownViewResource(R.layout.spinner_item);
                  WorkerSpinner.setAdapter(adapter);
                  AssignButton.setVisibility(View.GONE);

               }

               else{

                  ArrayAdapter<String> adapter =
                          new ArrayAdapter<String>(itemView.getContext(),
                                  R.layout.spinner_item_without_padding, worker_spinner);
                  adapter.setDropDownViewResource( R.layout.spinner_item);
                  WorkerSpinner.setAdapter(adapter);
                  AssignButton.setVisibility(View.VISIBLE);
                  AssignButton.setEnabled(true);

               }


            }
         });


      }
   }

}
