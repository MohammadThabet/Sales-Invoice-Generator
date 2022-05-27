package com.packages;

import java.util.ArrayList;
import java.util.List;

public class Item {


    int invoiceId;
    int itemId;
    String itemName;
    double itemPrice;
    double itemCount;
    double total;

    public Item(){}
    public Item(int invoiceId,int itemId, String itemName, double itemPrice,double itemCount,double total){
        this.invoiceId = invoiceId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemCount = itemCount;
        this.total = total;
    }

    public int getInvoiceId(){
        return invoiceId;
    }

}
