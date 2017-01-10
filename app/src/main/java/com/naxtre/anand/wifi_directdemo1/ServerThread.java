package com.naxtre.anand.wifi_directdemo1;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;

/**
 * Created by Anand Vardhan on 1/6/2017.
 */

public class ServerThread implements Runnable {
    int Port;
    DatagramSocket socket;
    byte[] sendData=new byte[64];
    byte[] receiveData=new byte[64];
    String receiveString;
    int sendCount=1;
    int receiveCount=0;
    InetAddress mClientAddress;
    public ServerThread(int Port) {
        this.Port=Port;
    }

    @Override
    public void run() {

        while(true){
            try{
                if(socket==null){
                    socket=new DatagramSocket(Port);
                    socket.setSoTimeout(1);
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
            //To Receive
            DatagramPacket datagramPacket=new DatagramPacket(receiveData,receiveData.length);
            Log.e("Server: Waiting for Packets","");
            try{
                socket.receive(datagramPacket);
                datagramPacket.getData();
                receiveString=new String(datagramPacket.getData(),0,datagramPacket.getLength());
                receiveCount++;
                Log.e("Server Received String ",":"+receiveString);
                if(mClientAddress==null){
                    mClientAddress=datagramPacket.getAddress();
                }




            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }
}
