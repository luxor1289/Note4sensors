package my.notefour.note4sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



public class Sensors extends AppCompatActivity implements SensorEventListener {

    //variables
    TextView light;
    TextView pressure;
    TextView magnetic;
    TextView altitude;
    SensorManager manager;
    Sensor sensorLight;
    Sensor sensorPressure;
    Sensor sensorMagnetic;
    private float altitudeValue = 0;

    private final String NO_SENSOR = "BRAK";
    private final String DEVELOPER = "Developed by: Łukasz Kowalewski";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        // UI elements
        light = (TextView)findViewById(R.id.textViewLightValue);
        pressure = (TextView)findViewById(R.id.textViewPressureValue);
        magnetic = (TextView)findViewById(R.id.textViewMagneticValue);
        altitude = (TextView)findViewById(R.id.textViewAltitudeValue);

        // Register sensor
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Light sensor
        sensorLight = manager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (sensorLight != null){
            manager.registerListener(this,sensorLight, 0, null);
        }else { light.setText(NO_SENSOR); }

        // Presure sensor
        sensorPressure = manager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (sensorPressure != null){
            manager.registerListener(this,sensorPressure,0,null);
        }else { pressure.setText(NO_SENSOR);
                altitude.setText(NO_SENSOR);}

        // Magnetic sensor
        sensorMagnetic = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (sensorMagnetic != null){
            manager.registerListener(this,sensorMagnetic,0,null);
        }else { magnetic.setText(NO_SENSOR); }


    }//end onCreate


    @Override
    protected void onResume() {
        //register sensor
        super.onResume();
        manager.registerListener(this,sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener(this,sensorPressure, SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener(this,sensorMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //unregister sensor
        manager.unregisterListener(this);
    }



    /*
    *  SENSOR METHOD OVERRIDE
    */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT){
            light.setText(Double.toString(Math.round(event.values[0]*10.0)/10.0)); //light
        }
        else if (event.sensor.getType() == Sensor.TYPE_PRESSURE){
            pressure.setText(Double.toString(Math.round(event.values[0]*10.0)/10.0)); //pressure
            altitudeValue = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE,event.values[0]); //altitude
            altitude.setText(Double.toString(Math.round(altitudeValue*10.0)/10.0));
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            magnetic.setText(calculateInduction(event.values[0],event.values[1],event.values[2])); //magnetic
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Null
    }



    /*
    *  CALCULATE MAGNETIC INDUCTION, ROUND TO 0.0 AND CONVERT TO STRING
    */
    public String calculateInduction(float Bx, float By, float Bz){
        return Double.toString( Math.round((Math.sqrt(Math.pow(Bx,2)+Math.pow(By,2)+Math.pow(Bz,2))/100)*10.0)/10.0 );
    }



    /*
    *  BUTTONS LISTENERS
    */
    // imageButton - Settings
    public void clickSettings(View v){
        Toast.makeText(getApplicationContext(),DEVELOPER, Toast.LENGTH_SHORT).show();
    }

}//end activity
