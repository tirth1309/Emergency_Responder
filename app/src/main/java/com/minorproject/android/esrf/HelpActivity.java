package com.minorproject.android.esrf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.minorproject.android.esrf.Helping_Classes.statics;
import com.minorproject.android.esrf.Models.User;
import com.minorproject.android.esrf.Models.alert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HelpActivity extends AppCompatActivity {


    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAaG8hVck:APA91bEovU1VFr4U0CfGywVMLIyc6-eTNLizPGClb56yF7E_p2y0f2D4_8Nnv_TBXlOCSYINc-cycXMjcTOa9J8AjyF_GgZlhQWY-DtLYrcKxfjTGeFv0of7VDh2NXw-flWRO4_zBobb";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    private User currUser;
    private String NOTIFICATION_TITLE;
    private String NOTIFICATION_MESSAGE;
    private ArrayList<String> tokenList;
    private Location userLoc;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        //userLoc = new Location("gps");
        currUser =(User)getIntent().getSerializableExtra("currUser");
        Button btnSend = findViewById(R.id.button);
        tokenList = new ArrayList<>();
        handlepermissions();
        //userLoc.setLatitude(currUser.latitude);
        //userLoc.setLongitude(currUser.longitude);
        Log.d("Intent Value",currUser.name);
        //populateTokenList();


    }

    public void renderDailogBox(){
        dialog = new ProgressDialog(HelpActivity.this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Alerting Emergency Services and People Near You...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void kind(){
        dialog.show();
        NOTIFICATION_TITLE = "Help! "+currUser.name+" is in danger";
        NOTIFICATION_MESSAGE = "Location : "+statics.currentLoc;
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        JSONArray  jsonArray = new JSONArray(tokenList);
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);
            notifcationBody.put("user", currUser.name);
            notifcationBody.put("lat", Double.toString(statics.currLat));
            notifcationBody.put("long", Double.toString(statics.currLong));
            notifcationBody.put("location", statics.currentLoc);
            notification.put("registration_ids", jsonArray);
            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        Log.d("TOKEN SIZE",Integer.toString(tokenList.size()));
        alertAuth();
        sendNotification(notification);

    }

    public void sendSMS()
    {
        SmsManager smsMgrVar = SmsManager.getDefault();
        String numbers[] = {"+91"+currUser.er.number1,"+91"+currUser.er.number2};
        String message = "Help !! I am in Danger";
        for(String number : numbers){
            try
            {
                smsMgrVar.sendTextMessage(number, null,message, null, null);
            }
            catch (Exception ErrVar)
            {
                ErrVar.printStackTrace();
            }
        }
    }

    private void handlepermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE},444);
        else{
            renderDailogBox();
            sendSMS();
            populateTokenList();


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case 444: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    renderDailogBox();
                    populateTokenList();
                    sendSMS();

                } else {
                    Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
                    renderDailogBox();
                    populateTokenList();
                }
                break;
            }
        }
    }



    public void populateTokenList(){

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User temp;
                for(DataSnapshot dataValue : dataSnapshot.getChildren()){
                   temp = dataValue.getValue(User.class);
                   /*if(temp.name.equals(currUser.er.name1) || temp.name.equals(currUser.er.name2) ){
                        //tokenByName(temp);
                   }*/
                   if(ifLocation(temp) /*&& !temp.name.equals(currUser.name)*/){
                       tokenByLocation(temp);

                   }



                }
                kind();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void alertAuth(){
        DatabaseReference authRef = FirebaseDatabase.getInstance().getReference().child("alerts");
        String key= authRef.push().getKey();
        String dateTime = DateFormat.getDateTimeInstance().format(new Date());
        authRef.push().setValue(new alert(currUser.name,statics.currentLoc,statics.currLat,statics.currLong,ServerValue.TIMESTAMP,dateTime));

    }


    private void sendNotification(JSONObject notification) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
        new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        dialog.dismiss();
                        startActivity(new Intent(HelpActivity.this,FirstAidList.class));
                        finish();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HelpActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                        dialog.dismiss();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private boolean ifLocation(User user){
        float[] distances = new float[1];
        boolean flag = true;
        Location.distanceBetween(statics.currLat,
                statics.currLong,
                user.latitude,
                user.longitude, distances);
        flag = distances[0]<2000.0;

        return flag;
    }

    public void tokenByLocation(User temp){
        Log.d("Users notifies",temp.name);
        tokenList.add(temp.token);

    }

    public void tokenByName(User temp){
            tokenList.add(temp.token);
        }




}

