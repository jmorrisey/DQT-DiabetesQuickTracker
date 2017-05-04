package com.dqt.jmorrisey.dqt.barcode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dqt.jmorrisey.dqt.R;
import com.dqt.jmorrisey.dqt.additems.ItemInformation;
import com.dqt.jmorrisey.dqt.additems.UserInformation;
import com.dqt.jmorrisey.dqt.records.RecordInfo;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by b3021318 on 09/02/2017.
 */

public class ScanFoodFragment extends Activity {

    TextView barcodeResult;
    final Context context = this;

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Double carbs;
    Double amount;
    Double icr;
    String barcodeValue;



    DatabaseReference referenceFood = FirebaseDatabase.getInstance().getReference("fooditems");
    DatabaseReference referenceUsers = FirebaseDatabase.getInstance().getReference("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_scan_food);
        

        barcodeResult = (TextView) findViewById(R.id.barcode_result);


    }

    public void scanBarcode(View v) {
        Intent intent = new Intent(this, ScanBarcodeActivity.class);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if(resultCode== CommonStatusCodes.SUCCESS){
                if(data!=null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    barcodeValue = barcode.displayValue.toString();
//                    barcodeResult.setText("Barcode Value : " + barcodeValue);

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



                    referenceFood.child(barcodeValue).addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){


                            ItemInformation itemInformation = new ItemInformation();
                            itemInformation = dataSnapshot.getValue(ItemInformation.class);

                            carbs = itemInformation.getCarbs();


                            amount = carbs/((icr)*10);

                            barcodeResult.setText("Insulin Required: " + amount);
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            String currentTime = sdf.format(new Date());



                            //Calculates how much insulin required for that particular item.
                            final String user = currentUser.toString();
                            final String name = itemInformation.getName();


                            final Double serving = itemInformation.getServing();
                            final String time = currentTime;

                            new AlertDialog.Builder(context)
                                    .setTitle("Add to daily intake?")
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


                }
                else{
                    barcodeResult.setText("No Barcode Found! Please add this item");

                }
            }

        } else {


            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
