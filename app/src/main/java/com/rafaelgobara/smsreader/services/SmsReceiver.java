package com.rafaelgobara.smsreader.services;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.rafaelgobara.smsreader.FullscreenActivity;
import com.rafaelgobara.smsreader.R;
import com.rafaelgobara.smsreader.constants.Constants;
/**
 * Created by rafaelgobara on 7/3/15.
 */
public class SmsReceiver extends BroadcastReceiver
{
	private String TAG = SmsReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// Get the data (SMS data) bound to intent
		Bundle bundle = intent.getExtras();

		SmsMessage[] msgs = null;

		String str = "";

		if (bundle != null)
		{
			// Retrieve the SMS Messages received
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];

			// For every SMS message received
			for (int i = 0; i < msgs.length; i++)
			{
				// TODO Verifiy if the SMS is that what you want.

				// Convert Object array
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				// Sender's phone number
				str += "SMS from " + msgs[i].getOriginatingAddress() + " : ";
				// Fetch the text message
				str += msgs[i].getMessageBody().toString();
				// Newline <img src="http://codetheory.in/wp-includes/images/smilies/simple-smile.png" alt=":-)" class="wp-smiley" style="height: 1em; max-height: 1em;">
				str += "\n";
			}

			int notificationId = (int) (System.currentTimeMillis() / 1000L);

			Intent resultIntent = new Intent(context, FullscreenActivity.class);
			resultIntent.putExtra(Constants.NOTIFICATION_ID, notificationId);

			// Because clicking the notification opens a new ("special") activity, there's
			// no need to create an artificial back stack.
			PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			builder.setSmallIcon(R.drawable.ic_stat_name);
			builder.setContentTitle("Opa");
			builder.setContentText(str);
			builder.setContentIntent(resultPendingIntent);

			// Automatically launch the activity.
			builder.setFullScreenIntent(resultPendingIntent, true);

			// High priority.
			builder.setPriority(NotificationCompat.PRIORITY_MAX);

			// Public visibility means that the notification will appear even with screen locked.
			builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

			// Setting a category for notification.
			builder.setCategory(NotificationCompat.CATEGORY_ALARM);

			// Keep the notification alive until you manually dismiss it.
			builder.setOngoing(true);

			// Gets an instance of the NotificationManager service
			final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

			// Builds the notification and issues it.
			notificationManager.notify(notificationId, builder.build());
		}
	}
}
