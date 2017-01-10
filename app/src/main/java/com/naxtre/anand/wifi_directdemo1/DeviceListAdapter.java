package com.naxtre.anand.wifi_directdemo1;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Anand Vardhan on 1/5/2017.
 */

public class DeviceListAdapter extends BaseAdapter implements CustomModel.OnCustomStateListener{

    Context context;
    List<WifiP2pDevice> listOfDevices;
    LayoutInflater layoutInflater;

    public DeviceListAdapter(Context context, List<WifiP2pDevice> listOfDevices) {
        this.context = context;
        this.listOfDevices = listOfDevices;
        layoutInflater = LayoutInflater.from(this.context);
        CustomModel.getInstance().setListener(this);
    }

    @Override
    public int getCount() {
        return listOfDevices == null ? 0 : listOfDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private String getDeviceStatus(int deviceStatus) {
        Log.d(MainActivity.TAG, "Peer status :" + deviceStatus);
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";

        }
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.peers_list_item, null);
        TextView device_name, status_of_device;
        LinearLayout container_for_list;
        container_for_list=(LinearLayout)view.findViewById(R.id.container_for_list);
        device_name = (TextView) view.findViewById(R.id.device_name);
        status_of_device = (TextView) view.findViewById(R.id.status_of_device);
        device_name.setText(listOfDevices.get(i).deviceName);
        String status = getDeviceStatus(listOfDevices.get(i).status);
        status_of_device.setText(status);
        container_for_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listOfDevices.get(i).status== WifiP2pDevice.INVITED || listOfDevices.get(i).status==WifiP2pDevice.CONNECTED){
                    Toast.makeText(context, "This device is already connected.\n Can't make a connection now", Toast.LENGTH_SHORT).show();
                }
                else{
                Log.e("Connecting to Device",": "+listOfDevices.get(i).deviceName+" "+listOfDevices.get(i).deviceAddress);
                CustomModel.getInstance().adapterClickListener(listOfDevices.get(i),i);
                }
            }
        });
        return view;
    }

    @Override
    public void PeerListRefreshed(List<WifiP2pDevice> newPeerList) {
        listOfDevices=newPeerList;
        notifyDataSetInvalidated();
        notifyDataSetChanged();
    }
}
