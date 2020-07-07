package com.minorproject.android.esrf.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.minorproject.android.esrf.R;
import com.minorproject.android.esrf.alertResponseActivity;
import com.minorproject.android.esrf.Helping_Classes.statics;

import java.util.Random;

public class recieveAlert extends FirebaseMessagingService{

    private final String TAG = "recieveAlert";
    private final String ADMIN_CHANNEL_ID ="admin_channel";
    private String uid;
    private DatabaseReference dbref;
    public recieveAlert() {

    }

    @Override
    public void onNewToken(@NonNull String s) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("tokens", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token_value",s);
        editor.commit();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Log.d(TAG, s);
            statics.token = s;
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            dbref = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            dbref.child("token").setValue(s);

        }
        else {
            Log.d(TAG, s);
            statics.token = s;
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, alertResponseActivity.class);

        Bundle b =new Bundle();
        b.putString("name",remoteMessage.getData().get("user"));
        b.putDouble("lat",Double.parseDouble(remoteMessage.getData().get("lat")));
        b.putDouble("long",Double.parseDouble(remoteMessage.getData().get("long")));
        b.putString("location",remoteMessage.getData().get("location"));
        intent.putExtras(b);



        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_sos);

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_sos)
                .setLargeIcon(largeIcon)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        notificationManager.notify(notificationID, notificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}
