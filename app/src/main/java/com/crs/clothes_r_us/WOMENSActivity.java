// Written by Shlomi Biton, ID 303103501
package com.crs.clothes_r_us;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WOMENSActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ArrayList<Item> WomensNewItems;
    public ArrayList<Item> WomensHotItems;

    public CustomeAdapter customeAdapterNew;
    public CustomeAdapter customeAdapterHot;
    public ArrayList<ImageModel> imageModelNewArrayList;
    public ArrayList<ImageModel> imageModelHotArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_womens);
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

        //final ArrayAdapter adapterNew = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        //final ArrayAdapter adapterHot = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        final ListView listNew = (ListView) findViewById(R.id.lvNew);
        final ListView listHot = (ListView) findViewById(R.id.lvHot);
        final Intent intent = new Intent(this, ItemActivity.class);
        WomensNewItems = new ArrayList<Item>();
        WomensHotItems = new ArrayList<Item>();

        imageModelNewArrayList = new ArrayList<ImageModel>();
        imageModelHotArrayList = new ArrayList<ImageModel>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference itemToAddRef = database.getReference("WomensItems");


            itemToAddRef.child("WomensNewItems").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (int i = 1; i < 8; i++)
                    {
                        String name = (String) dataSnapshot.child("Item" + i + "").child("name").getValue();
                        String picture = (String) dataSnapshot.child("Item" + i + "").child("picture").getValue();
                        int price = Integer.valueOf(dataSnapshot.child("Item" + i + "").child("price").getValue().toString());
                        int id = Integer.valueOf(dataSnapshot.child("Item" + i + "").child("id").getValue().toString());
                        int discount = Integer.valueOf(dataSnapshot.child("Item" + i + "").child("discount").getValue().toString());
                        String section = (String) dataSnapshot.child("Item" + i + "").child("section").getValue();
                        String gender = (String) dataSnapshot.child("Item" + i + "").child("gender").getValue();

                        ImageModel imageModel = new ImageModel();
                        imageModel.setName(name);
                        imageModel.setPrice(String.valueOf(price));
                        imageModel.setImage_drawable(getResources().getIdentifier(picture, "drawable", getPackageName()));
                        imageModelNewArrayList.add(imageModel);

                        //adapterNew.add(name);
                        WomensNewItems.add(new Item(id, name, price, discount, picture, section, gender));
                    }
                    //Log.d("hjhjh",imageModelArrayList.size()+"");
                    customeAdapterNew = new CustomeAdapter(WOMENSActivity.this,imageModelNewArrayList);
                    listNew.setAdapter(customeAdapterNew);
                        //Toast.makeText(getApplicationContext(), "Value is: " +WomensNewItems.get(0).name, Toast.LENGTH_SHORT).show();
                    }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Toast.makeText(getApplicationContext(), "Failed to read value." + error.toException(), Toast.LENGTH_SHORT).show();
                }
            });



        //listNew.setAdapter(adapterNew);
        listNew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                for (int i = 0; i < WomensNewItems.size(); i++)
                    if(position==i) {
                        intent.putExtra("Title", WomensNewItems.get(i).name);
                        intent.putExtra("Price", WomensNewItems.get(i).price);
                        intent.putExtra("Picture", WomensNewItems.get(i).picture);
                        intent.putExtra("Discount", WomensNewItems.get(i).discount);
                        intent.putExtra("Section", WomensNewItems.get(i).section);
                        startActivity(intent);
                    }

            }
        });

            itemToAddRef.child("WomensHotItems").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (int i = 1; i < 8; i++) {
                        String name = (String) dataSnapshot.child("Item" + i + "").child("name").getValue();
                        String picture = (String) dataSnapshot.child("Item" + i + "").child("picture").getValue();
                        int price = Integer.valueOf(dataSnapshot.child("Item" + i + "").child("price").getValue().toString());
                        int id = Integer.valueOf(dataSnapshot.child("Item" + i + "").child("id").getValue().toString());
                        int discount = Integer.valueOf(dataSnapshot.child("Item" + i + "").child("discount").getValue().toString());
                        String section = (String) dataSnapshot.child("Item" + i + "").child("section").getValue();
                        String gender = (String) dataSnapshot.child("Item" + i + "").child("gender").getValue();

                        ImageModel imageModel = new ImageModel();
                        imageModel.setName(name);
                        imageModel.setPrice(String.valueOf(price));
                        imageModel.setImage_drawable(getResources().getIdentifier(picture, "drawable", getPackageName()));
                        imageModelHotArrayList.add(imageModel);
                        //adapterHot.add(name);
                        WomensHotItems.add(new Item(id, name, price, discount, picture, section, gender));
                    }

                    customeAdapterHot = new CustomeAdapter(WOMENSActivity.this,imageModelHotArrayList);
                    listHot.setAdapter(customeAdapterHot);
                    //Toast.makeText(getApplicationContext(), "Value is: " +WomensHotItems.get(0).name, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Toast.makeText(getApplicationContext(), "Failed to read value." + error.toException(), Toast.LENGTH_SHORT).show();
                }
            });

        //listHot.setAdapter(adapterHot);
        listHot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                for (int i = 0; i < WomensHotItems.size(); i++)
                if(position==i) {
                    intent.putExtra("Title", WomensHotItems.get(i).name);
                    intent.putExtra("Price", WomensHotItems.get(i).price);
                    intent.putExtra("Picture", WomensHotItems.get(i).picture);
                    intent.putExtra("Discount", WomensHotItems.get(i).discount);
                    intent.putExtra("Section", WomensHotItems.get(i).section);
                    startActivity(intent);
                }
            }
        });
    }
    //private ArrayList<ImageModel> populateList() {

        //ArrayList<ImageModel> list = new ArrayList<>();

        //for (int i = 0; i < 7; i++) {
            //ImageModel imageModel = new ImageModel();
           // imageModel.setName(titles[i]);
           // imageModel.setPrice(prices[i]);
           // imageModel.setImage_drawable(myImageList[i]);
           // list.add(imageModel);
        //}

        //return list;
    //}

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
                    intent = new Intent(WOMENSActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_womens:

                    return true;
                case R.id.navigation_mens:
                    intent = new Intent(WOMENSActivity.this, MENSActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_pay:
                    intent = new Intent(WOMENSActivity.this, PayActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.women, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(WOMENSActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_womens) {

        } else if (id == R.id.nav_mens) {
            Intent intent = new Intent(WOMENSActivity.this, MENSActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_pay)
        {
            Intent intent = new Intent(WOMENSActivity.this, PayActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
