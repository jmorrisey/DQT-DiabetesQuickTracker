package com.dqt.jmorrisey.dqt.menu;


import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.dqt.jmorrisey.dqt.R;
import com.dqt.jmorrisey.dqt.addbg.AddBloodSugarReading;
import com.dqt.jmorrisey.dqt.additems.ItemInformation;
import com.dqt.jmorrisey.dqt.additems.UserInformation;
import com.dqt.jmorrisey.dqt.barcode.ScanBarcodeActivity;
import com.dqt.jmorrisey.dqt.reminder.RemindersFragment;
import com.dqt.jmorrisey.dqt.records.RecordInfo;
import com.dqt.jmorrisey.dqt.searchitems.SearchFood;
import com.dqt.jmorrisey.dqt.additems.AddItemFragment;
import com.dqt.jmorrisey.dqt.leaderboard.LeaderboardFragment;
import com.dqt.jmorrisey.dqt.registration.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.CommonStatusCodes;
import android.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    // Support Map Fragment Import and initialisation.
    SupportMapFragment sMapFragment;


    TextView barcodeResult;
    final Context context = this;

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Double carbs;
    Double amount;
    Double icr;
    String barcodeValue;


    DatabaseReference referenceFood = FirebaseDatabase.getInstance().getReference("fooditems");
    DatabaseReference referenceUsers = FirebaseDatabase.getInstance().getReference("users");

    private FirebaseAuth firebaseAuth;

    // Initialising the buttons textviews, animations for rotation.
    FloatingActionButton fab_plus, fab_barcode, fab_search, fab_bg, fab_plus_item;
    TextView textViewBarcode, textViewSearch, textViewBG, textViewAdd;

    Animation FabRClockwise, FabRanticlockwise;

    boolean isOpen = false;
    FragmentManager fragmentManager = getFragmentManager();

    public GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;

    // Checking for maps permission for usrs device if not permitted asks the user to allow
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    {

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                    }

                }

            }

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sMapFragment = SupportMapFragment.newInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.logo);
        setSupportActionBar(toolbar);
        CheckGooglePlayServices();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new TrackingFragment()).commit();

        // Incomplete feature maps.
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            //Asks the user for permission to use location services
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) locationListener);
        }

        // Setting the buttons for the actionmenu and their textview information location in layout file
        fab_plus = (FloatingActionButton) findViewById(R.id.fab_plus);

        fab_barcode = (FloatingActionButton) findViewById(R.id.fab_food_barcode);
        textViewBarcode = (TextView) findViewById(R.id.textViewBarcode);
        textViewBarcode.setVisibility(View.GONE);

        fab_search = (FloatingActionButton) findViewById(R.id.fab_food_search);
        textViewSearch = (TextView) findViewById(R.id.textViewSearch);
        textViewSearch.setVisibility(View.GONE);

        fab_bg = (FloatingActionButton) findViewById(R.id.fab_bg_plus);
        textViewBG = (TextView) findViewById(R.id.textViewBloodSugar);
        textViewBG.setVisibility(View.GONE);

        fab_plus_item = (FloatingActionButton) findViewById(R.id.fab_plus_item);
        textViewAdd = (TextView) findViewById(R.id.textViewAddItem);
        textViewAdd.setVisibility(View.GONE);

        // Setting the animations for clockwise and anticlockwise
        FabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);

        FabRanticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        // If plus button is set to open onclick close all buttons
        fab_plus.setOnClickListener(new View.OnClickListener() {

            public void onClick(View b) {
                if (isOpen) {

                    textViewBarcode.animate().alpha(0).setDuration(500);
                    textViewBarcode.setVisibility(View.GONE);
                    fab_barcode.hide();
                    fab_barcode.setClickable(false);

                    textViewSearch.animate().alpha(0).setDuration(500);
                    textViewSearch.setVisibility(View.GONE);
                    fab_search.hide();
                    fab_search.setClickable(false);

                    textViewBG.animate().alpha(0).setDuration(500);
                    textViewBG.setVisibility(View.GONE);
                    fab_bg.hide();
                    fab_bg.setClickable(false);

                    textViewAdd.animate().alpha(0).setDuration(500);
                    textViewAdd.setVisibility(View.GONE);
                    fab_plus_item.hide();
                    fab_plus_item.setClickable(false);

                    fab_plus.startAnimation(FabRanticlockwise);
                    isOpen = false;
                } else {

                    textViewBarcode.animate().alpha(1).setDuration(500);
                    textViewBarcode.setVisibility(View.VISIBLE);
                    fab_barcode.show();
                    fab_barcode.setClickable(true);

                    textViewSearch.animate().alpha(1).setDuration(500);
                    textViewSearch.setVisibility(View.VISIBLE);
                    fab_search.show();
                    fab_search.setClickable(true);


                    textViewBG.animate().alpha(1).setDuration(500);
                    textViewBG.setVisibility(View.VISIBLE);
                    fab_bg.show();
                    fab_bg.setClickable(true);

                    textViewAdd.animate().alpha(1).setDuration(500);
                    textViewAdd.setVisibility(View.VISIBLE);
                    fab_plus_item.show();
                    fab_plus_item.setClickable(true);

                    fab_plus.startAnimation(FabRClockwise);
                    isOpen = true;
                }
            }

        });

        fab_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starts a new intent to ScanBarcodeActivity for barcode scanning
                Intent intent = new Intent(getApplicationContext(), ScanBarcodeActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        fab_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFood searchFood = new SearchFood();
                searchFood.show(fragmentManager, "Search Fragment");
            }
        });

        fab_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBloodSugarReading dialogFragment = new AddBloodSugarReading();
                dialogFragment.show(fragmentManager, "Blood Sugar Fragment");
            }
        });

        fab_plus_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItemFragment dialogFragment = new AddItemFragment();
                dialogFragment.show(fragmentManager, "Item Fragment");
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sMapFragment.getMapAsync(this);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();
        int id = item.getItemId();
        if (sMapFragment.isAdded())
            sFm.beginTransaction().hide(sMapFragment).commit();

        if (id == R.id.nav_tracker) {


            //go to main menu
            fragmentManager.beginTransaction().replace(R.id.content_frame, new TrackingFragment()).commit();
        } else if (id == R.id.nav_insulin_layout) {

            if (!sMapFragment.isAdded())

                sFm.beginTransaction().add(R.id.content_frame, sMapFragment).commit();
            else
                sFm.beginTransaction().show(sMapFragment).commit();
        } else if (id == R.id.nav_bmi_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new BmiFragment()).commit();
        } else if (id == R.id.nav_reminders_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new RemindersFragment()).commit();
        } else if (id == R.id.nav_leaderboard_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new LeaderboardFragment()).commit();
        } else if (id == R.id.nav_settings_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(View view) {

        finish();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainMenu.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    final Barcode barcode = data.getParcelableExtra("barcode");
                    barcodeValue = barcode.displayValue.toString();


                    referenceUsers.child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserInformation userInformation = new UserInformation();
                            userInformation = dataSnapshot.getValue(UserInformation.class);

                            icr = userInformation.getICR();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    referenceFood.child(barcodeValue).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ItemInformation itemInformation = new ItemInformation();
                            itemInformation = dataSnapshot.getValue(ItemInformation.class);
                            carbs = itemInformation.getCarbs();


                            amount = carbs / ((icr) * 10);

//                            barcodeResult.setText("Insulin Required: " + amount);
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            String currentTime = sdf.format(new Date());


                            //Calculates how much insulin required for that particular item.
                            final String user = currentUser.toString();
                            final String name = itemInformation.getName();


                            final Double serving = itemInformation.getServing();
                            final String time = currentTime;

                            new AlertDialog.Builder(context)
                                    .setTitle("Add " + name + " to daily intake?")
                                    .setMessage("Insulin shots required: " + amount)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //add all the code for new table
                                            RecordInfo recordInfo = new RecordInfo(user, name, carbs, amount, serving, time);
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference myRef = database.getReference("records");
                                            myRef.push().setValue(recordInfo);

                                            dialog.dismiss();


                                        }
                                    })

                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                } else {
                    barcodeResult.setText("No Barcode Found! Please add this item");


                }
            }

        } else {


            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
        if(googleAPI.isUserResolvableError(result)) {
        googleAPI.getErrorDialog(this, result,0).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                mMap.clear();

                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation,14));


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//                mMap.setMyLocationEnabled(true);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.clear();

                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));


            }


        }


    }

}
