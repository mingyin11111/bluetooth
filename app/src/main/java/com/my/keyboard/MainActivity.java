package com.my.keyboard;

import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHidDeviceAppQosSettings;
import android.bluetooth.BluetoothHidDeviceAppSdpSettings;
import android.bluetooth.BluetoothProfile;
import android .bluetooth.BluetoothDevice;
import android.bluetooth
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.os.Bundle;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothHidDeviceAppQosSettings;
import android.bluetooth.BluetoothHidDeviceAppSdpSettings;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;





import com.my.keyboard.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BluetoothAdapter mBtAdapter;
    private BluetoothHidDevice mHidDevice;
    private BluetoothDevice mHostDevice;
    private final BluetoothHidDeviceAppQosSettings qosSettings
            = new BluetoothHidDeviceAppQosSettings(BluetoothHidDeviceAppQosSettings.SERVICE_BEST_EFFORT,
            800, 9, 0, 11250, BluetoothHidDeviceAppQosSettings.MAX
    );
    private final BluetoothHidDeviceAppSdpSettings mouseSdpSettings = new BluetoothHidDeviceAppSdpSettings(
            HidConfig.MOUSE_NAME, HidConfig.DESCRIPTION, HidConfig.PROVIDER,
            BluetoothHidDevice.SUBCLASS1_MOUSE, HidConfig.MOUSE_COMBO);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        InitBluetoothHID();
    }

    private void InitBluetoothHID()
    {
    mBtAdapter=BluetoothAdapter.getDefaultAdapter();
    mBtAdapter.getProfileProxy(this.getApplicationContext(), new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if(profile==BluetoothProfile.HID_DEVICE)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if(! (proxy instanceof BluetoothHidDevice))
                    {
                        Log.e("tag" ,   "Proxy received but it's not  BluetoothHIDDevice");
                            return;
                    }
<<<<<<< HEAD
=======

                    mHidDevice=(BluetoothHidDevice) proxy;
>>>>>>> 162f1b182e90ddc81d123931354161e37e618f6f

                    mHidDevice=(BluetoothHidDevice) proxy;
                    registerBluetoothHid();
                }
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {

        }
    },BluetoothProfile.HID_DEVICE);

    }

    private void registerBluetoothHid() {
        if (mHidDevice == null) {
            Log.e(TAG, "hid device is null");
            return;
        }

        mHidDevice.registerApp(mouseSdpSettings, null, qosSettings, Executors.newCachedThreadPool(), new BluetoothHidDevice.Callback() {
            @Override
            public void onAppStatusChanged(BluetoothDevice pluggedDevice, boolean registered) {
                Log.d(TAG, "onAppStatusChanged:" + (pluggedDevice != null ? pluggedDevice.getName() : "null") + " registered:" + registered);
                if (registered) {
                    Log.d(TAG, "paired devices: " + mHidDevice.getConnectionState(pluggedDevice));
                    if (pluggedDevice != null && mHidDevice.getConnectionState(pluggedDevice) != BluetoothProfile.STATE_CONNECTED) {
                        boolean result = mHidDevice.connect(pluggedDevice);
                        Log.d(TAG, "hidDevice connect:" + result);
                    }
                }
                if (mBluetoothHidStateListener != null) {
                    mBluetoothHidStateListener.onRegisterStateChanged(registered, pluggedDevice != null);
                }
            }

            @Override
            public void onConnectionStateChanged(BluetoothDevice device, int state) {
                Log.d(TAG, "onConnectionStateChanged:" + device + "  state:" + state);
                if (state == BluetoothProfile.STATE_CONNECTED) {
                    mHostDevice = device;
                }
                if (state == BluetoothProfile.STATE_DISCONNECTED) {
                    mHostDevice = null;
                }
                if (mBluetoothHidStateListener != null) {
                    mBluetoothHidStateListener.onConnectionStateChanged(state);
                }
            }
        });
    }




}