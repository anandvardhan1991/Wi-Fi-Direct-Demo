package com.naxtre.anand.wifi_directdemo1;


import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anand Vardhan on 1/5/2017.
 */

public class DeviceListFragment extends Fragment {
    Context context;
    DeviceListAdapter deviceListAdapter;
    List<WifiP2pDevice> listOfDevices = new ArrayList<WifiP2pDevice>();
    ListView listView;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.device_list_frag, container, false);
        if (isAdded())
            context = getActivity();
        listView = (ListView) V.findViewById(R.id.list_view);
        setTheList();
        return V;
    }


    public void setTheList() {
        deviceListAdapter = new DeviceListAdapter(context, listOfDevices);
        listView.setAdapter(deviceListAdapter);

    }
}
