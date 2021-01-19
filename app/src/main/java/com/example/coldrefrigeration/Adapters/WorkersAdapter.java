package com.example.coldrefrigeration.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coldrefrigeration.Model.Members;
import com.example.coldrefrigeration.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

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

      public WorkersViewHolder(@NonNull View itemView) {
         super(itemView);

         Name = itemView.findViewById(R.id.worker_name_text);
         Phone = itemView.findViewById(R.id.worker_phone_text);
         SecurityAmountText = itemView.findViewById(R.id.security_amount_text);

      }
   }

}
