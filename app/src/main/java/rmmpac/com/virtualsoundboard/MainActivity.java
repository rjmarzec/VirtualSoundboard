package rmmpac.com.virtualsoundboard;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    Button scannerBtn;
    Button ringBtn;

    private SensorManager sensorMan;
    private Sensor accelerometer;

    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    private TextView myText;
    private ImageView myBell;
    private boolean isMoving;

    private MediaPlayer mp;
    private AudioManager audMan;

    private boolean firstTime;

//this is an error
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //myText = (TextView)findViewById(R.id.sensingText);
        //myBell = (ImageView)findViewById(R.id.imageView);
        //mp = MediaPlayer.create(getApplicationContext(), R.raw.ringf);
        audMan = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        firstTime = false;

        sensorMan.registerListener(new SensorEventListener(){
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    mGravity = event.values.clone();
                }

                // Shake detection
                float x = mGravity[0];
                float y = mGravity[1];
                float z = mGravity[2];
                mAccelLast = mAccelCurrent;
                mAccelCurrent = (float)Math.sqrt(x*x + y*y + z*z);
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta;

                // Make this higher or lower according to how much motion you want to detect
                if(mAccel > 3){
                    isMoving = true;
                    myText.setText("Is Moving");
                    myText.setTextColor(Color.rgb(0,200,0));
                    mp.seekTo((int)0);
                    mp.start();
                    firstTime = true;
                }
                else{
                    isMoving = false;
                    myText.setText("Ain't Moving");
                    myText.setTextColor(Color.rgb(200,0,0));
                    //mp.stop();
                }
                return;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                return;
            }
        }, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }


    //Marcin Does Work 101



}