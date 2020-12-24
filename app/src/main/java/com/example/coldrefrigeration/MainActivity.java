package com.example.coldrefrigeration;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);


      Button button = findViewById(R.id.send);

      button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            try{

               String apiKey = "apikey=" + "Hbw0MmUmaQM-5zxhzlaKDvIkbsqSJddq2YfrQEbHTX";
               String message = "&message=" + "Hello Sahil. Thank you for choosing our service. Our worker will reach you soon.";
               String sender = "&sender=" + "TXTLCL";
               String numbers = "&numbers=" + "7007537972";

               HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
               String data = apiKey + numbers + message + sender;

               conn.setDoOutput(true);
               conn.setRequestMethod("POST");
               conn.setRequestProperty("Content-Length",Integer.toString(data.length()));
               conn.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));
               final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

               String line;

               while((line = rd.readLine()) != null){
                  Toast.makeText(getApplicationContext(),"The message is " + line,Toast.LENGTH_LONG).show();
               }

               rd.close();


            } catch (Exception e){


               Toast.makeText(getApplicationContext(),"Message not sent and error is " + e.getMessage()
                       ,Toast.LENGTH_LONG).show();

            }

         }
      });

      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
      StrictMode.setThreadPolicy(policy);

   }
}