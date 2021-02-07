package com.example.coldrefrigeration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.coldrefrigeration.Dialogs.LoadingDialog;
import com.example.coldrefrigeration.Model.Members;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Member;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

   FirebaseAuth firebaseAuth;
   FirebaseFirestore firebaseFirestore;
   EditText Name,Phone,Email,Password;
   RelativeLayout Parent;
   Button RegisterButton;
   LoadingDialog loadingDialog;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_sign_up);

      Name = findViewById(R.id.name_et);
      Phone = findViewById(R.id.phone_et);
      Email = findViewById(R.id.email_et);
      Password = findViewById(R.id.password_et);
      Parent = findViewById(R.id.sign_up_layout);
      RegisterButton = findViewById(R.id.sign_up_btn);

      loadingDialog = new LoadingDialog(SignUpActivity.this);

      RegisterButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            checkDetails();

         }
      });

      CheckBox ShowPass = findViewById(R.id.show_pass_check);

      ShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
               Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
               Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
         }
      });


   }

   private void checkDetails() {

      if(Email.getText().toString().isEmpty()){
         String snack = "Email address can't be empty.";
         showSnack(snack);
         return;
      }

      if(!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString().trim()).matches()){
         String snack = "Please enter a valid email address";
         showSnack(snack);
         return;
      }

      if(Name.getText().toString().isEmpty()){
         String snack = "Please provide a name.";
         showSnack(snack);
         return;
      }

      if(Phone.getText().toString().isEmpty()){
         String snack = "Please provide a phone number.";
         showSnack(snack);
         return;
      }

      if(Phone.getText().toString().length() != 10){
         String snack = "Please provide a 10 digit mobile number.";
         showSnack(snack);
         return;
      }

      if(Password.getText().toString().isEmpty()){
         String snack = "Please provide a password.";
         showSnack(snack);
         return;
      }

      if(Password.getText().toString().length() < 6){
         String snack = "Please provide a password of more than or equal to 6 characters.";
         showSnack(snack);
         return;
      }

      createAccount(Email.getText().toString().trim(),Password.getText().toString().trim());

   }

   private void createAccount(String email, String password) {

      loadingDialog.show();

      firebaseAuth = FirebaseAuth.getInstance();
      firebaseFirestore = FirebaseFirestore.getInstance();

      firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
         @Override
         public void onComplete(@NonNull Task<AuthResult> task) {

            if(task.isSuccessful()){

               Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {

                     if(task.isSuccessful()){
                        storeDetails(firebaseAuth.getCurrentUser().getUid());
                     }

                     else {
                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                     }

                  }
               });
            }
            else {
               Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
               loadingDialog.dismiss();

            }

         }
      });


   }

   private void storeDetails(String uid) {

      Members member = new Members();

      member.name = Name.getText().toString().trim();
      member.email = Email.getText().toString().trim();
      member.phone = Phone.getText().toString().trim();
      member.designation = "";
      member.activation = "no";
      member.active_status = "off";
      member.area = "";
      member.uid = uid;

      firebaseFirestore.collection("Members").document(Objects.requireNonNull(firebaseAuth.getCurrentUser())
              .getUid()).set(member).addOnSuccessListener(new OnSuccessListener<Void>() {
         @Override
         public void onSuccess(Void aVoid) {

            Toast.makeText(getApplicationContext(),"A verification link has been sent to your email." +
                    " Please verify your account to login.",Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();


         }
      }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {

            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();


         }
      });

   }

   private void showSnack(String snack){
      Snackbar snackbar = Snackbar.make(Parent,snack,Snackbar.LENGTH_LONG);
      snackbar.show();
   }

}