package com.example.coldrefrigeration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {



   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_login);

      EditText EmailET = findViewById(R.id.email_et);
      EditText PasswordET = findViewById(R.id.password_et);

      Button LoginButton = findViewById(R.id.login_btn);

      LoginButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            Toast.makeText(getApplicationContext(),EmailET.getText().toString().trim() +
                    PasswordET.getText().toString().trim(),Toast.LENGTH_LONG).show();


         }
      });

      TextView SignUpText = findViewById(R.id.sign_up_text);
      SignUpText.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
         }
      });

   }
}