package com.example.coldrefrigeration.BottomSheets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import com.example.coldrefrigeration.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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





      return v;
   }

}
