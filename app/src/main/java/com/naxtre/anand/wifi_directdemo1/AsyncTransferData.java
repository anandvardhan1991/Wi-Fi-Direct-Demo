package com.naxtre.anand.wifi_directdemo1;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.net.InetAddress;

/**
 * Created by Anand Vardhan on 1/6/2017.
 */

public class AsyncTransferData {

    InetAddress groupOwnerAddress;
    Boolean Host, isConnected;
    ServerThread serverThread;
    ClientThread clientThread;
    String textToBeSent;
    int Port = 8888;

    public AsyncTransferData(InetAddress groupOwnerAddress, Boolean Host, Boolean isConnected, String textToBeSent) {
        this.groupOwnerAddress = groupOwnerAddress;
        this.Host = Host;
        this.isConnected = isConnected;
        this.textToBeSent = textToBeSent;
    }

    public void StartServer() {
        Log.e("", "Calling Server Thread");
        serverThread = new ServerThread(Port);
        new Thread(serverThread).start();
    }

    public void StartClient(String textToBeSent) {
        Log.e("", "Calling Client Thread");
        clientThread = new ClientThread(groupOwnerAddress, Port, textToBeSent);
        new Thread(clientThread).start();
    }


//    /*
//    if Host==true ---- Server
//    if Host==false --- Client
//     */
//
//    InetAddress groupOwnerAddress;
//    Boolean Host,isConnected;
//    ServerThread serverThread;
//    ClientThread clientThread;
//    String textToBeSent;
//    int Port=8888;
//
//    public AsyncTransferData(InetAddress groupOwnerAddress,Boolean Host,Boolean isConnected,String textToBeSent) {
//        this.groupOwnerAddress=groupOwnerAddress;
//        this.Host=Host;
//        this.isConnected=isConnected;
//        this.textToBeSent=textToBeSent;
//    }
//
//    @Override
//    protected Object doInBackground(Object[] objects) {
//        if(isConnected){
//            if(Host){
//                Log.e("","Calling Server Thread");
//                serverThread=new ServerThread(Port);
//                new Thread(serverThread).start();
//
//            }
//            else{
//                Log.e("","Calling Client Thread");
//                clientThread=new ClientThread(groupOwnerAddress,Port,textToBeSent);
//                new Thread(clientThread).start();
//            }
//        }
//        return null;
//    }
}
