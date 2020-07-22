package com.example.protectme;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Message data payload: {text=Hello}
        String messageName = remoteMessage.getData().get("name");
        String messageLatitude = remoteMessage.getData().get("latitude");
        String messageLongitude = remoteMessage.getData().get("longitude");
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());


        passMessageToActivity(messageName, messageLatitude, messageLongitude);

        ///#############################
        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void passMessageToActivity(String messageName, String messageLatitude, String messageLongitude) {
        Log.d(TAG, "Passfunktion:"+messageName);
        Intent intent = new Intent(this, EmergencyNotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("name", messageName);
        intent.putExtra("latitude", messageLatitude);
        intent.putExtra("longitude", messageLongitude);
        startActivity(intent);
    }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }

//    private void showEditDialog() {
//        FragmentManager fm = getSupportFragmentManager();
//        Notification_Dialog editNameDialogFragment = Notification_Dialog.newInstance("Some Title");
//        editNameDialogFragment.show(fm, "fragment_edit_name");
//    }
}

