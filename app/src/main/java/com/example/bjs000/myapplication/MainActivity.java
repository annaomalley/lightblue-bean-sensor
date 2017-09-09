package com.example.bjs000.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.ScratchBank;
import com.punchthrough.bean.sdk.message.Acceleration;


import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<Bean> beans = new ArrayList<>();

        BeanDiscoveryListener listener = new BeanDiscoveryListener() {
            @Override
            public void onBeanDiscovered(Bean bean, int rssi) {
                beans.add(bean);
            }

            @Override
            public void onDiscoveryComplete() {
                // This is called when the scan times out, defined by the .setScanTimeout(int seconds) method

                for (Bean bean : beans) {
                    System.out.println(bean.getDevice().getName());   // "Bean"              (example)
                    //System.out.println(bean.getDevice().mAddress);    // "B4:99:4C:1E:BC:75" (example)
                }

                readDeviceInfo(beans);

            }
        };

        BeanManager.getInstance().setScanTimeout(15);  // Timeout in seconds, optional, default is 30 seconds
        Log.d("default adapter",(BluetoothAdapter.getDefaultAdapter()).getName());
        BeanManager.getInstance().startDiscovery(listener);


    }

    private void readDeviceInfo(List<Bean> beans) {
        // Assume we have a reference to the 'beans' ArrayList from above.
        final Bean bean = beans.get(0);

        BeanListener beanListener = new BeanListener() {

            @Override
            public void onConnected() {
                System.out.println("connected to Bean!");
                bean.readDeviceInfo(new Callback<DeviceInfo>() {
                    @Override
                    public void onResult(DeviceInfo deviceInfo) {
                        System.out.println(deviceInfo.hardwareVersion());
                        System.out.println(deviceInfo.firmwareVersion());
                        System.out.println(deviceInfo.softwareVersion());
                    }
                });
            }

            // In practice you must implement the other Listener methods

            @Override
            public void onReadRemoteRssi(int i) {

            }

            @Override
            public void onError(BeanError e) {

            }

            @Override
            public void onScratchValueChanged(ScratchBank s,byte[] b) {

            }

            @Override
            public void onSerialMessageReceived(byte[] b) {

            }

            @Override
            public void onDisconnected() {

            }

            @Override
            public void onConnectionFailed() {

            }



        };

// Assuming you are in an Activity, use 'this' for the context
        bean.connect(this, beanListener);



        bean.readAcceleration(new Callback<Acceleration>() {
            @Override
            public void onResult(Acceleration result) {
                Log.d("abcd", "result: "+result);
                Log.d("abcd", "Acceleration on X-axis: "+result.x());
                Log.d("abcd", "Acceleration on y-axis: "+result.y());
                Log.d("abcd", "Acceleration on z-axis: "+result.z());
            }
        });


    }
}
