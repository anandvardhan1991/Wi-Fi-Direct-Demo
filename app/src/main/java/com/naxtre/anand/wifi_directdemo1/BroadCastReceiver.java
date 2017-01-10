package com.naxtre.anand.wifi_directdemo1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anand Vardhan on 1/5/2017.
 */

public class BroadCastReceiver extends BroadcastReceiver implements PeerListListener {
    WifiP2pManager manager;
    Channel channel;
    MainActivity activity;
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    List<WifiP2pDevice> refreshedPeers=new ArrayList<WifiP2pDevice>();


    public BroadCastReceiver(WifiP2pManager manager, Channel channel, MainActivity activity) {
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                activity.setIsWifiP2pEnabled(true);
            } else {
                activity.setIsWifiP2pEnabled(false);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            //The peer list has changed. Please do something about it.
            Log.e("Peer List has Changed", ": " + "Yipeee");
            //If changes happens call the List to notify that the peer list have changed
            // and to update the list as the devices get discovered;
            if (manager != null) {
                manager.requestPeers(channel, this);
            }
            Log.d(MainActivity.TAG, "P2P peers changed");


        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Connection state changed!  We should probably do something about
            // that.

            if(manager!=null){
                manager.requestPeers(channel,this);
            }
            manager.requestConnectionInfo(channel,connectionInfoListener);
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
//            Log.e("Device Details has Changed ", ": " + String.valueOf(intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE)));
        }
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
        refreshedPeers.addAll(wifiP2pDeviceList.getDeviceList());
        if (!refreshedPeers.equals(peers)) {
            peers.clear();
            peers.addAll(refreshedPeers);
            Log.e(MainActivity.TAG, "Size " + String.valueOf(peers.size()));
            for (int i = 0; i < peers.size(); i++) {
                Log.e(MainActivity.TAG, "Details of New Device Detected" + peers.get(i).deviceName + peers.get(i).deviceAddress);
            }
            CustomModel.getInstance().peersChanged(peers);
        }
        if(refreshedPeers.equals(peers)){
            for (WifiP2pDevice peerDevices:refreshedPeers) {
                if(peerDevices.status==WifiP2pDevice.CONNECTED){
                    CustomModel.getInstance().peersChanged(refreshedPeers);
                }
            }
        }
        if (peers.size() == 0) {
            Log.d(MainActivity.TAG, "No devices found");
            return;
        }
    }

    WifiP2pManager.ConnectionInfoListener connectionInfoListener=new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            InetAddress groupOwnerAddress=wifiP2pInfo.groupOwnerAddress;
            if(wifiP2pInfo.groupFormed){
                if(wifiP2pInfo.isGroupOwner) {
                    Toast.makeText(activity, "Server", Toast.LENGTH_LONG).show();
                    Log.e("Server", "Device");
                    activity.sendSomeData(groupOwnerAddress, true);
                }
                else{
                    Log.e("Client Device","Running");
                    activity.sendSomeData(groupOwnerAddress,false);
                }
            }

        }
    };

}
