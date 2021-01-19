package com.example.coldrefrigeration.BottomSheets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coldrefrigeration.Model.Services;
import com.example.coldrefrigeration.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddServiceBottomSheet extends BottomSheetDialogFragment {

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      final View v = inflater.inflate(R.layout.add_services_bottom_sheet, container, false);
      final Context context = v.getContext();

      EditText ServiceName = v.findViewById(R.id.service_name_et);
      EditText TotalCost = v.findViewById(R.id.total_price_et);
      EditText WorkerCost = v.findViewById(R.id.worker_cost_et);
      Button AddService = v.findViewById(R.id.done_btn);

      AddService.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            if(ServiceName.getText().toString().trim().isEmpty()){
               Toast.makeText(context,"Please enter service name.",Toast.LENGTH_LONG).show();
               return;
            }

            if(TotalCost.getText().toString().trim().isEmpty()){
               Toast.makeText(context,"Please enter service price.",Toast.LENGTH_LONG).show();
               return;
            }

            if(WorkerCost.getText().toString().trim().isEmpty()){
               Toast.makeText(context,"Please enter worker cost.",Toast.LENGTH_LONG).show();
               return;
            }

            if(Integer.parseInt(TotalCost.getText().toString().trim()) < Integer.parseInt(WorkerCost.getText().toString().trim())){
               Toast.makeText(context,"Worker's cost can not be more than total cost.",Toast.LENGTH_LONG).show();
               return;
            }

            AddService.setText(R.string.adding);
            AddService.setClickable(false);

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            Services services = new Services();

            services.service_name = ServiceName.getText().toString().trim();
            services.total_cost = Integer.parseInt(TotalCost.getText().toString().trim());
            services.worker_cost = Integer.parseInt(WorkerCost.getText().toString().trim());

            DocumentReference documentReference = firebaseFirestore.collection("Services")
                    .document();

            documentReference.set(services).addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {

                  Toast.makeText(context,"Service added : " + ServiceName.getText().toString().trim(),Toast.LENGTH_LONG).show();
                  AddService.setText(R.string.done);
                  AddService.setClickable(true);
                  ServiceName.setText("");
                  TotalCost.setText("");
                  WorkerCost.setText("");

               }
            }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {

                  Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                  AddService.setText(R.string.done);
                  AddService.setClickable(true);

               }
            });



         }
      });



      return v;
   }

}
