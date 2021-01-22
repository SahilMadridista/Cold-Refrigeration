package com.example.coldrefrigeration.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coldrefrigeration.Model.Bookings;
import com.example.coldrefrigeration.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class WorkerWorkHistoryAdapter extends FirestoreRecyclerAdapter<Bookings,WorkerWorkHistoryAdapter.WorkWorkHistoryViewHolder> {

   Context context;

   public WorkerWorkHistoryAdapter(@NonNull FirestoreRecyclerOptions<Bookings> options) {
      super(options);
   }

   @Override
   protected void onBindViewHolder(@NonNull WorkWorkHistoryViewHolder holder, int position, @NonNull Bookings model) {

      String ser = model.getService_name() + " , " + model.getService_cost();

      holder.Name.setText(model.getCustomer_name());
      holder.Phone.setText(model.getCustomer_phone());
      holder.ServiceName.setText(ser);
      holder.Address.setText(model.getCustomer_address());
      holder.Date.setText(model.getBooking_date());

   }

   @NonNull
   @Override
   public WorkWorkHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_worker_work_history,
              parent, false);
      context = v.getContext();
      return new WorkWorkHistoryViewHolder(v);
   }

   static class WorkWorkHistoryViewHolder extends RecyclerView.ViewHolder{

      TextView Name, ServiceName, Address, Phone, Date;

      public WorkWorkHistoryViewHolder(@NonNull View itemView) {
         super(itemView);

         Name = itemView.findViewById(R.id.customer_name_text);
         ServiceName = itemView.findViewById(R.id.service_name_text);
         Address = itemView.findViewById(R.id.address_text);
         Phone = itemView.findViewById(R.id.customer_phone_text);
         Date = itemView.findViewById(R.id.date_text);

      }
   }

}
