package ch.ffhs.esa.proximety.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ch.ffhs.esa.proximety.R;
import ch.ffhs.esa.proximety.activities.MainActivity;
import ch.ffhs.esa.proximety.activities.OpenRequestsActivity;

/**
 * Created by boe on 22.12.2014.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            String type = extras.getString("type");
            String message = extras.getString("message");

            sendNotification(message, type);
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg, String type) {
        Log.i("location-updates", msg);
        Log.i("location-updates", "type: " + type);
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        Bitmap mapPicture = null;
        try {
            String url = "http://maps.googleapis.com/maps/api/staticmap?size=400x150&markers=color:red%7Clabel:F%7C47.4741296,8.674765&markers=color:0x336699%7Clabel:M%7C47.4741296,8.675765";
            mapPicture = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
        } catch (IOException e) {
            //Fehler muss nicht behandelt werden. Notification ist einfach ohne Map Picture
        }


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getText(R.string.settings_proximity_alert))
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .setBigContentTitle(getText(R.string.settings_proximity_alert))
                                .bigPicture(mapPicture))
                        .setContentText(msg)
                .setVibrate(new long[] { 1000, 500, 500, 1000, 500, 500 })
                .setLights(Color.BLUE, 3000, 3000);

        mBuilder.setContentIntent(contentIntent);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }
}
