package com.example.coldrefrigeration.Model;

public class Services {

   public String service_name;
   public int total_cost;
   public int worker_cost;

   public Services(){

   }

   public Services(String service_name, int total_cost, int worker_cost) {
      this.service_name = service_name;
      this.total_cost = total_cost;
      this.worker_cost = worker_cost;
   }

   public String getService_name() {
      return service_name;
   }

   public int getTotal_cost() {
      return total_cost;
   }

   public int getWorker_cost() {
      return worker_cost;
   }

}
