// Written by Shlomi Biton, ID 303103501
package com.crs.clothes_r_us;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView textAmount,textTotalPrice,textTitle,textPrice,textBadge;

    private ImageView imgItem;

    static int Amount,Price,Discount,OldPrice,TotalPrice,TotalPriceToPay;
    static String Title,Picture,Section,Items="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_item);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_item);
        } else {
            setContentView(R.layout.activity_item_land);
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_nv);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null) {
            Title = (String) b.get("Title");
            Picture = (String) b.get("Picture");
            Price = (int) b.get("Price");
            Discount = (int) b.get("Discount");
            Section = (String) b.get("Section");
        }
        //Price = 20;  //from db
        //Title="Longline Check Buffalo Shirt Grandad Collar";
        //Picture="";


        textTitle = (TextView) findViewById(R.id.textTitle);
        textPrice = (TextView) findViewById(R.id.textPrice);
        imgItem = (ImageView) findViewById(R.id.imgItem);
        textAmount = (TextView) findViewById(R.id.textAmount);
        textTotalPrice = (TextView) findViewById(R.id.textTotalPrice);
        textBadge = (TextView) findViewById(R.id.textBadge);


        textAmount.setText("Amount: "+Amount);
        textTotalPrice.setText("Total Price: "+TotalPrice+"$");




        if (Discount>0 && Discount<100)
        {
            textBadge.setText(Discount+"% OFF");
            OldPrice = (int)Math.round((Price*100)/(100-Discount));
            SpannableString text = new SpannableString("\nPrice: "+Price+"$,"+OldPrice+"$");
            text.setSpan(new StrikethroughSpan(), String.valueOf(Price).length()+10, String.valueOf(Price).length()+11+String.valueOf(OldPrice).length(), 0);
            //text.setSpan(new StrikethroughSpan(), 10, 12, 0);
            textPrice.setMovementMethod(LinkMovementMethod.getInstance());
            textPrice.setText(text, TextView.BufferType.SPANNABLE);
        }
        else
        {
            if (Section.equalsIgnoreCase("new")){
                textBadge.setText("NEW");
            }
            else
            {
                textBadge.setText("SALE");
            }
            textPrice.setText("\nPrice: "+Price+"$");
        }
        textTitle.setText("\nItem: "+Title+"\n");


        int id = getResources().getIdentifier(Picture, "drawable", getPackageName());
        imgItem.setImageResource(id);

        //imgItem.setImageResource(R.mipmap.);


        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.btnAdd:
                        Amount++;
                        textAmount.setText("Amount: "+Amount);
                        calculatePrice();

                        break;
                    case R.id.btnRemove:
                        if (Amount>0)
                            Amount--;
                        textAmount.setText("Amount: "+Amount);
                        calculatePrice();
                        break;
                    case R.id.btnReset:
                        Button button = (Button) findViewById(R.id.btnReset);
                        final Context context = ItemActivity.this;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override public void onClick(View arg0) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setTitle("Clothes-R-Us");
                                alertDialogBuilder.setMessage("Are you sure you want to reset your Shopping Cart?").setCancelable(false) .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        Amount=0;
                                        TotalPrice=0;
                                        textAmount.setText("Amount: "+Amount);
                                        calculatePrice();
                                        ItemActivity.Items="";
                                        ItemActivity.TotalPriceToPay=0;
                                        Toast.makeText(ItemActivity.this, "Your Shopping Cart has been Successfully Reset!", Toast.LENGTH_LONG).show();
                                    }
                                }) .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show(); } });
                        break;
                    case R.id.btnAddToCart:
                        if(Amount>0)//Update Items,TotalPriceToPay and Move between Pages
                        {
                            TotalPriceToPay+=TotalPrice;
                            Items+= Amount+" "+Title+", ";
                            Toast.makeText(ItemActivity.this, "Successfully added to cart!, redirected to previous page", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                        else
                            Toast.makeText(ItemActivity.this, "No Items Selected", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.btnPay:
                        if(Amount>0)//Update Items,TotalPriceToPay and Move between Pages
                        {
                            TotalPriceToPay+=TotalPrice;
                            Items+= Amount+" "+Title+", ";
                        }
                        Amount=0;
                        TotalPrice=0;
                        Intent intent = new Intent(ItemActivity.this, PayActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };
        this.findViewById(R.id.btnAdd).setOnClickListener(listener);
        this.findViewById(R.id.btnRemove).setOnClickListener(listener);
        this.findViewById(R.id.btnReset).setOnClickListener(listener);
        this.findViewById(R.id.btnPay).setOnClickListener(listener);
        this.findViewById(R.id.btnAddToCart).setOnClickListener(listener);


    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(ItemActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_womens:
                    intent = new Intent(ItemActivity.this, WOMENSActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_mens:
                    intent = new Intent(ItemActivity.this, MENSActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_pay:
                    if(Amount>0)//Update Items,TotalPriceToPay and Move between Pages
                    {
                        TotalPriceToPay+=TotalPrice;
                        Items+= Amount+" "+Title+", ";
                    }
                    Amount=0;
                    TotalPrice=0;
                    intent = new Intent(ItemActivity.this, PayActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    private void calculatePrice() {
        TotalPrice= Price * Amount;
        textTotalPrice.setText("Total Price: "+TotalPrice+"$");
    }

    @Override
    public void onBackPressed() {
        Amount=0;
        TotalPrice=0;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Amount=0;
        TotalPrice=0;

        if (id == R.id.nav_home) {
            Intent intent = new Intent(ItemActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_womens) {
            Intent intent = new Intent(ItemActivity.this, WOMENSActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_mens) {
            Intent intent = new Intent(ItemActivity.this, MENSActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_pay)
        {
            if(Amount>0)//Update Items,TotalPriceToPay and Move between Pages
            {
                TotalPriceToPay+=TotalPrice;
                Items+= Amount+" "+Title+", ";
            }
            Amount=0;
            TotalPrice=0;
            Intent intent = new Intent(ItemActivity.this, PayActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
