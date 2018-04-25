// Written by Shlomi Biton, ID 303103501
package com.crs.clothes_r_us;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Patterns;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.common.collect.Range;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static com.basgeekball.awesomevalidation.ValidationStyle.COLORATION;
import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;




public class PayActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText etFirstName,etLastName, etEmail, etPhone, etStreet,etCity, etCreditCardNumber, etZipCode;

    private TextView textSummary;

    private Button btnSaveOrder;

    static String Price, Items, TotalPriceToPay;

    private int orderNumber=0;


    private AwesomeValidation formValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
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

        Price="20";
        Items= "longline Jeans";

        textSummary = (TextView) findViewById(R.id.textSummary);

        textSummary.setText("You have Selected the following Items: "+ItemActivity.Items+"\nThe Total amount to pay: "+ItemActivity.TotalPriceToPay+"$");

        initView();
        setFormValidationStyle();
        addFormValidation();
        operationOnButton();

    }

    private void initView() {
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etCity = (EditText) findViewById(R.id.etCity);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etStreet = (EditText) findViewById(R.id.etStreet);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etCreditCardNumber = (EditText) findViewById(R.id.etCreditCardNumber);
        etZipCode = (EditText) findViewById(R.id.etZipCode);

        btnSaveOrder = (Button) findViewById(R.id.btnSaveOrder);
        getOrderNumber();
    }


    public void setFormValidationStyle(){

        // formValidation = new AwesomeValidation(BASIC);
        //formValidation = new AwesomeValidation(ValidationStyle.BASIC);
        // or
        formValidation = new AwesomeValidation(COLORATION);
        formValidation.setColor(Color.YELLOW);  // optional, default color is RED if not set

        // or
        //formValidation = new AwesomeValidation(UNDERLABEL);
        //formValidation.setContext(this);  // mandatory for UNDERLABEL style
    }


    public void addFormValidation(){

        formValidation.addValidation(this, R.id.etFirstName, RegexTemplate.NOT_EMPTY, R.string.fname_error);
        formValidation.addValidation(this, R.id.etLastName, RegexTemplate.NOT_EMPTY, R.string.lname_error);
        formValidation.addValidation(this, R.id.etEmail, Patterns.EMAIL_ADDRESS, R.string.email_error);
        formValidation.addValidation(this, R.id.etCity, RegexTemplate.NOT_EMPTY, R.string.city_error);
        formValidation.addValidation(this, R.id.etStreet, RegexTemplate.NOT_EMPTY, R.string.street_error);
        formValidation.addValidation(this, R.id.etPhone, "[0-9]{9,10}" , R.string.phone_error);
        formValidation.addValidation(this, R.id.etZipCode, "\\d+", R.string.zip_error);
        formValidation.addValidation(this, R.id.etCreditCardNumber, "[0-9]{16}", R.string.ccn_error);

    }

    public void setOrderNumber(int num)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Orders").child("Counter");
        myRef.child("Orders").child("Counter");
        myRef.setValue(num);
    }

    public void getOrderNumber()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Orders");
        myRef.child("Counter").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            orderNumber = Integer.parseInt(dataSnapshot.getValue().toString());
            orderNumber++;

        }
        @Override
         public void onCancelled(DatabaseError error) {
          // Failed to read value
          Toast.makeText(getApplicationContext(), "Failed to read value." + error.toException(), Toast.LENGTH_SHORT).show();
           }
        });
    }

    public void operationOnButton(){

        findViewById(R.id.btnSaveOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formValidation.validate()){
                    if (ItemActivity.TotalPriceToPay>0) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference orderToAddRef = database.getReference("Orders").child("Order" + orderNumber + "");
                        Order myOrder = new Order(orderNumber, etFirstName.getText().toString(), etLastName.getText().toString(),
                                etPhone.getText().toString(), etStreet.getText().toString(), etCity.getText().toString(), etZipCode.getText().toString(),
                                etCreditCardNumber.getText().toString(), ItemActivity.Items, ItemActivity.TotalPriceToPay);
                        orderToAddRef.child("Orders").child("Order" + orderNumber + "");
                        orderToAddRef.setValue(myOrder);
                        setOrderNumber(orderNumber);
                        Toast.makeText(PayActivity.this, "Your Order is Confirmed!\nThanks for buying Clothes-R-Us!", Toast.LENGTH_LONG).show();
                        ItemActivity.Items="";
                        ItemActivity.TotalPriceToPay=0;
                        Intent intent = new Intent(PayActivity.this, MainActivity.class);
                        startActivity(intent);

                    }
                    else
                        Toast.makeText(PayActivity.this, "No Items Selected", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Optional: remove validation failure information
       // findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
       //     @Override
        //    public void onClick(View v) {
       //         formValidation.clear();
       //     }
       // });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    intent = new Intent(PayActivity.this, MainActivity.class);
                    startActivity(intent);

                    return true;
                case R.id.navigation_womens:
                    intent = new Intent(PayActivity.this, WOMENSActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_mens:
                    intent = new Intent(PayActivity.this, MENSActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_pay:

                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pay, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(PayActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_womens) {
            Intent intent = new Intent(PayActivity.this, WOMENSActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_mens) {
            Intent intent = new Intent(PayActivity.this, MENSActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_pay)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
