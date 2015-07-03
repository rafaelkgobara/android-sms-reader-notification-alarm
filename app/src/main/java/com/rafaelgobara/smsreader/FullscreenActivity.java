package com.rafaelgobara.smsreader;

import com.rafaelgobara.smsreader.constants.Constants;
import com.rafaelgobara.smsreader.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity
{
	private Window mWindow;
	private int mNotificationId;
	private AudioManager mAudioManager;
	private MediaPlayer mPlayer;

	private EditText mTxtPass;
	private Button mBtnStop;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
				.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_fullscreen);

		mWindow = this.getWindow();
		mWindow.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		mWindow.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		mWindow.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

//		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//		mAudioManager.setStreamVolume(AudioManager.STREAM_RING, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING), AudioManager.MODE_RINGTONE);

		try
		{
			final AssetFileDescriptor descriptor = getAssets().openFd("audio/siren.mp3");

			mPlayer = new MediaPlayer();
			mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
			mPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(),
					descriptor.getLength());
			mPlayer.prepare();
//			mPlayer.setVolume(0.3f, 0.3f);
			mPlayer.setLooping(true);
			mPlayer.start();

			descriptor.close();
		}
		catch (IOException e)
		{
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

		Bundle bundle = getIntent().getExtras();

		if (bundle != null)
			mNotificationId = bundle.getInt(Constants.NOTIFICATION_ID);

		mTxtPass = (EditText) findViewById(R.id.txt_pass);

		mBtnStop = (Button) findViewById(R.id.btn_stop);
		mBtnStop.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String pass = mTxtPass.getText().toString();

				if (pass.equalsIgnoreCase("1234"))
				{
//					if (mNotificationId != 0)
//					{
						mPlayer.stop();

						NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
						manager.cancel(mNotificationId);
//					}

					finish();
				}
				else
					Toast.makeText(getApplicationContext(), "Senha errada!", Toast.LENGTH_LONG).show();

			}
		});
	}
}
