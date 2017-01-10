package com.naxtre.anand.wifi_directdemo1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.IpPrefix;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomModel.OnItemAdapterClickListener {
    WifiP2pManager mManager;
    private final IntentFilter intentFilter = new IntentFilter();
    Channel mChannel;
    Boolean isWifiP2pEnabled;
    public static String TAG = "BroadCast Receiver";
    BroadCastReceiver receiver;
    FragmentManager fragmentManager;
    EditText edit_view_sent_or_recieved;
    Button button_to_send_from_client;
    InetAddress groupOwnerAddress;
    Boolean Host;
    AsyncTransferData asyncTransferData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        edit_view_sent_or_recieved = (EditText) findViewById(R.id.edit_view_sent_or_recieved);
        button_to_send_from_client = (Button) findViewById(R.id.button_to_send_from_client);
        button_to_send_from_client.setVisibility(View.GONE);
        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        scanNearDevices();
        CustomModel.getInstance().itemAdapterSetListener(this);

    }

    public void setIsWifiP2pEnabled(Boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    public void scanNearDevices() {
        mManager.discoverPeers(mChannel, new ActionListener() {
            @Override
            public void onSuccess() {
                Log.e("Peer Discovery", " Started Successfully");

            }

            @Override
            public void onFailure(int i) {
                Log.e("Peer Discovery", " Started UnSuccessfully" + String.valueOf(i));
            }
        });
        Fragment fm = new DeviceListFragment();
        fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fm).commit();

    }

    @Override
    protected void onResume() {
        Log.e("OnResume Called", ":");
        super.onResume();
        deletePersistentGroups();
        receiver = new BroadCastReceiver(mManager, mChannel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        Log.e("OnPause Called", ":");
        super.onPause();
        unregisterReceiver(receiver);
        deletePersistentGroups();
//        mManager.removeGroup(mChannel, new ActionListener() {
//            @Override
//            public void onSuccess() {
//                Log.e("Groups Created are Successfully Removed",":");
//            }
//
//            @Override
//            public void onFailure(int i) {
//                Log.e("Groups are not Removed",":");
//            }
//        });
    }

    @Override
    protected void onStop() {
        Log.e("OnStop Called", ":");
        super.onStop();
        deletePersistentGroups();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        switch (item.getItemId()) {
            case R.id.scan_wifi_disconnect:
                wifiManager.setWifiEnabled(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void itemAdapterClickListerner(WifiP2pDevice wifiP2pDevice, int position) {
        final WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
        Log.e("Device Name and Address Passed", ": " + wifiP2pDevice.deviceName + " " + wifiP2pDevice.deviceAddress);
        wifiP2pConfig.deviceAddress = wifiP2pDevice.deviceAddress;
        wifiP2pConfig.wps.setup = WpsInfo.PBC;

        wifiP2pConfig.groupOwnerIntent = 0;
//        mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//                // Device is ready to accept incoming connections from peers.
//                Log.e("Group Creation Successfully",":");
//            }
//
//            @Override
//            public void onFailure(int reason) {
//                Toast.makeText(MainActivity.this, "P2P group creation failed. Retry.",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
        mManager.connect(mChannel, wifiP2pConfig, new ActionListener() {
            @Override
            public void onSuccess() {
                Log.e("Connection Established", ": ");
            }

            @Override
            public void onFailure(int i) {
                Log.e("Connection Something Went Wrong", "+ ");
            }
        });
    }

    /*
    if Host==True ----- Server
    if Host==False----- Client
     */

    public void sendSomeData(final InetAddress groupOwnerAddress, final Boolean Host) {
        asyncTransferData=new AsyncTransferData(groupOwnerAddress,Host,true,null);
        Log.e("Host" + String.valueOf(Host), " GroupOwnerAddress" + groupOwnerAddress.getHostAddress());
        this.groupOwnerAddress = groupOwnerAddress;
        this.Host = Host;
        if (Host) {
            asyncTransferData.StartServer();
        } else if (!Host) {
            button_to_send_from_client.setVisibility(View.VISIBLE);
            edit_view_sent_or_recieved.setFocusableInTouchMode(true);
            edit_view_sent_or_recieved.setFocusable(true);
            edit_view_sent_or_recieved.requestFocus();

            button_to_send_from_client.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edit_view_sent_or_recieved.getText().toString().trim() == null || edit_view_sent_or_recieved.getText().toString().trim().isEmpty()) {
                        edit_view_sent_or_recieved.setError("*This is required.");
                        edit_view_sent_or_recieved.requestFocus();
                    } else {
                        asyncTransferData.StartClient(edit_view_sent_or_recieved.getText().toString().trim());
//                        new AsyncTransferData(groupOwnerAddress,Host,true,null).StartClient(edit_view_sent_or_recieved.getText().toString().trim());
//                        new AsyncTransferData(groupOwnerAddress, Host, true, edit_view_sent_or_recieved.getText().toString().trim()).doInBackground(null);
                    }
                }
            });
        }
//        new AsyncTransferData(groupOwnerAddress,Host,true).doInBackground(null);
    }

//    public void createGroup(){
//        mManager.createGroup(mChannel, new ActionListener() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onFailure(int i) {
//                Log.e("Group Creation UnSuccessfully",":");
//            }
//        });
//    }


    private void deletePersistentGroups() {
        try {
            Method[] methods = WifiP2pManager.class.getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals("deletePersistentGroup")) {
                    // Delete any persistent group
                    for (int netid = 0; netid < 32; netid++) {
                        methods[i].invoke(mManager, mChannel, netid, null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
