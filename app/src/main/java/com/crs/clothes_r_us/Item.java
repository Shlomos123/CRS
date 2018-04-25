package com.crs.clothes_r_us;

/**
 * Created by Shlomi Biton ,ID 303103501 on 07/12/2017.
 */

public class Item {
    int id;
    String name;
    int price;
    int discount;
    String picture;
    String section;
    String gender;

    public Item(int id,String name,int price,int discount,String picture,String section,String gender )
    {
        this.id=id;
        this.name=name;
        this.price=price;
        this.discount=discount;
        this.picture=picture;
        this.section=section;
        this.gender=gender;
    }
}
