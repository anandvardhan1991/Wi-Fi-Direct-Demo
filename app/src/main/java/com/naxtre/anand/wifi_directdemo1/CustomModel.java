package com.naxtre.anand.wifi_directdemo1;

import android.net.wifi.p2p.WifiP2pDevice;

import java.util.List;

/**
 * Created by Anand Vardhan on 9/30/2016.
 */
public class CustomModel {

    public interface OnCustomStateListener {
//        void urlSelected(String urlSelected);
        void PeerListRefreshed(List<WifiP2pDevice> newPeerList);
    }
    public interface OnItemAdapterClickListener{
        void itemAdapterClickListerner(WifiP2pDevice wifiP2pDevice,int position);
    }

    private static CustomModel mInstance;
    private OnCustomStateListener mListener;
    private OnItemAdapterClickListener mAdapterItemClickListener;
    private boolean mState;


    private CustomModel() {}

    public static CustomModel getInstance() {
        if(mInstance == null) {
            mInstance = new CustomModel();
        }
        return mInstance;
    }

    public void setListener(OnCustomStateListener listener) {
        mListener = listener;
    }

    public void itemAdapterSetListener(OnItemAdapterClickListener listener){
        mAdapterItemClickListener=listener;
    }

//    public void changeState(boolean state) {
//        if(mListener != null) {
//            mState = state;
//            notifyStateChange();
//        }
//    }

//    public void urlSelected(String urlSelected){
//
//    }

    public void peersChanged(List<WifiP2pDevice> peerList){
        List<WifiP2pDevice> newPeerList=peerList;
        mListener.PeerListRefreshed(newPeerList);
    }

    public void adapterClickListener(WifiP2pDevice wifiP2pDevice,int position){
        mAdapterItemClickListener.itemAdapterClickListerner(wifiP2pDevice,position);
    }


//    public boolean getState() {
//        return mState;
//    }

//    private void notifyStateChange() {
//        mListener.stateChanged();
//    }


}