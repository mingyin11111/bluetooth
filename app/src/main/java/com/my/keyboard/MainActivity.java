package com.my.keyboard;

import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

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
                    mHidDevice=(BluetoothHidDevice) proxy;

                }
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {

        }
    },BluetoothProfile.HID_DEVICE);
    }

}