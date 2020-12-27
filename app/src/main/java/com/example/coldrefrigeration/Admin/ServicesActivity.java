package com.example.coldrefrigeration.Admin;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.coldrefrigeration.BottomSheets.AddServiceBottomSheet;
import com.example.coldrefrigeration.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ServicesActivity extends AppCompatActivity {


   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_services);


      FloatingActionButton AddServices = findViewById(R.id.add_service_btn);
      AddServices.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            AddServiceBottomSheet addServiceBottomSheet = new AddServiceBottomSheet();
            addServiceBottomSheet.show(getSupportFragmentManager(),"example sheet");

         }
      });


   }
}