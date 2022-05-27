package com.packages;

public class Invoice {


    int invoiceId;
    String date;
    String customerName;
    double total;

    public  Invoice(){}
    public Invoice(int invoiceId,String date, String customerName,double total){
        this.invoiceId = invoiceId;
        this.date = date;
        this.customerName = customerName;
        this.total = total;
    }


}
