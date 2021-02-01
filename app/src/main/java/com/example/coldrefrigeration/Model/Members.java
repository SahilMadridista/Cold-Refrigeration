package com.example.coldrefrigeration.Model;

public class Members {

   public String name;
   public String phone;
   public String email;
   public String designation;
   public String activation;
   public String uid;
   public String active_status;
   public int security;

   public Members() {

   }

   public Members(String name, String phone, String email, String designation,
                  String activation, String uid, String active_status, int security) {
      this.name = name;
      this.phone = phone;
      this.email = email;
      this.designation = designation;
      this.activation = activation;
      this.uid = uid;
      this.active_status = active_status;
      this.security = security;
   }

   public String getName() {
      return name;
   }

   public String getPhone() {
      return phone;
   }

   public String getEmail() {
      return email;
   }

   public String getDesignation() {
      return designation;
   }

   public String getActivation() {
      return activation;
   }

   public String getUid() {
      return uid;
   }

   public String getActive_status() {
      return active_status;
   }

   public int getSecurity() {
      return security;
   }
}

