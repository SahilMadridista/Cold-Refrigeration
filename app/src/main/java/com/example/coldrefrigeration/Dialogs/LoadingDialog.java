package com.example.coldrefrigeration.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.coldrefrigeration.R;

public class LoadingDialog {

   private Activity activity;
   private AlertDialog alertDialog;

   public LoadingDialog(Activity myActivity){
      activity = myActivity;

   }

   public void show(){

      AlertDialog.Builder builder = new AlertDialog.Builder(activity);
      LayoutInflater layoutInflater = activity.getLayoutInflater();
      builder.setView(layoutInflater.inflate(R.layout.loading_dialog,null));
      builder.setCancelable(false);

      alertDialog = builder.create();
      alertDialog.show();


   }

   public void dismiss(){
      alertDialog.dismiss();
   }


}
