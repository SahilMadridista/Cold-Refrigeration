package com.example.coldrefrigeration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coldrefrigeration.Admin.AdminHomePage;
import com.example.coldrefrigeration.Consts.SharedPrefConsts;
import com.example.coldrefrigeration.Dialogs.LoadingDialog;
import com.example.coldrefrigeration.Worker.WorkerHomePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

   EditText EmailET, PasswordET;
   Button LoginButton;
   FirebaseAuth firebaseAuth;
   RelativeLayout Parent;
   LoadingDialog loadingDialog;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_login);

      firebaseAuth = FirebaseAuth.getInstance();

      EmailET = findViewById(R.id.email_et);
      PasswordET = findViewById(R.id.password_et);

      LoginButton = findViewById(R.id.login_btn);

      loadingDialog = new LoadingDialog(LoginActivity.this);

      Parent = findViewById(R.id.parent);

      LoginButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            checkEditTexts(EmailET.getText().toString().trim(),PasswordET.getText().toString().trim());

         }
      });

      TextView SignUpText = findViewById(R.id.sign_up_text);
      SignUpText.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
         }
      });

      CheckBox ShowPass = findViewById(R.id.show_pass_check);

      ShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
               PasswordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
               PasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
         }
      });

   }

   private void checkEditTexts(String email, String password) {

      if(email.isEmpty()){
         String snack = "Please provide an email address.";
         showSnack(snack);
         return;
      }

      if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
         String snack = "Please provide a valid email address.";
         showSnack(snack);
         return;
      }

      if(password.isEmpty()){
         String snack = "Please provide a password.";
         showSnack(snack);
         return;
      }

      loginUser(email,password);

   }

   private void loginUser(String email, String password) {

      loadingDialog.show();

      firebaseAuth.signInWithEmailAndPassword(email,password)
              .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                       assert firebaseAuth.getCurrentUser() != null;
                       String id = firebaseAuth.getCurrentUser().getUid();

                       if(Objects.requireNonNull(firebaseAuth.getCurrentUser()).isEmailVerified()){
                          checkDesignation(id);
                       }
                       else{
                          Toast.makeText(LoginActivity.this,"Please verify your email first. You can login after verification.",
                                  Toast.LENGTH_LONG).show();
                          loadingDialog.dismiss();

                       }
                    }else {
                       Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(),
                               Toast.LENGTH_LONG).show();
                       loadingDialog.dismiss();

                    }
                 }
              });

   }

   private void checkDesignation(String id) {

      FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
      firebaseFirestore.collection("Members").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
         @Override
         public void onSuccess(DocumentSnapshot documentSnapshot) {

            assert documentSnapshot != null;

            String name = documentSnapshot.getString("name");
            String designation = documentSnapshot.getString("designation");
            String activation = documentSnapshot.getString("activation");
            String phone = documentSnapshot.getString("phone");
            String email = documentSnapshot.getString("email");
            String security = String.valueOf(documentSnapshot.get("security"));
            String active_status = String.valueOf(documentSnapshot.get("active_status"));
            assert designation != null;
            assert activation != null;
            openActivity(designation,name,activation,phone,email,security,active_status);

         }

      }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
         }
      });

   }

   private void openActivity(String designation, String name, String activation, String phone,
                             String email, String security, String active_status) {

      if(activation.equals("no")){
         Toast.makeText(getApplicationContext(),"Please ask the admin to activate your account first."
                 ,Toast.LENGTH_LONG).show();
         loadingDialog.dismiss();
         firebaseAuth.signOut();
         return;
      }

      if(designation.equals("ADMIN")){

         SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
         SharedPreferences.Editor editor = preferences.edit();
         editor.putInt("login", SharedPrefConsts.ADMIN_LOGIN);
         editor.apply();

         Intent i = new Intent(getApplicationContext(), AdminHomePage.class);
         i.putExtra("name",name);
         i.putExtra("designation",designation);
         i.putExtra("activation",activation);
         i.putExtra("email",email);
         i.putExtra("phone",phone);
         startActivity(i);
         finish();

      }

      if(designation.equals("WORKER")){

         SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
         SharedPreferences.Editor editor = preferences.edit();
         editor.putInt("login", SharedPrefConsts.WORKER_LOGIN);
         editor.apply();

         Intent i = new Intent(getApplicationContext(), WorkerHomePage.class);
         i.putExtra("name",name);
         i.putExtra("designation",designation);
         i.putExtra("activation",activation);
         i.putExtra("email",email);
         i.putExtra("phone",phone);
         i.putExtra("security",security);
         i.putExtra("active_status",active_status);

         startActivity(i);
         finish();

      }


   }

   private void showSnack(String snack) {

      Snackbar snackbar = Snackbar.make(Parent,snack,Snackbar.LENGTH_LONG);
      snackbar.show();

   }

}