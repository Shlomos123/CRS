package com.crs.clothes_r_us;

/**
 * Created by Shlomi Biton, ID 303103501 on 07/12/2017.
 */

public class Order {

    public int id;
    public String firstName;
    public String lastName;
    public String phone;
    public String street;
    public String city;
    public String zipCode;
    public String creditCardNumber;
    public String itemsOrdered;
    public int priceToPay;

    public Order(int id,String firstName,String lastName,String phone,String street,String city,String zipCode,String creditCardNumber,String itemsOrdered,int priceToPay)
    {

        this.id=id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.phone=phone;
        this.street=street;
        this.city=city;
        this.zipCode=zipCode;
        this.creditCardNumber=creditCardNumber;
        this.itemsOrdered=itemsOrdered;
        this.priceToPay=priceToPay;
    }
}
