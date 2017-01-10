package com.naxtre.anand.wifi_directdemo1;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by Anand Vardhan on 1/6/2017.
 */

public class ClientThread implements Runnable {
    InetAddress groupOwnerAddress;
    int Port;
    DatagramSocket socket;
    byte[] sendData=new byte[64];
    byte[] receiveData=new byte[64];
    String receiveString;
    int sendCount=1;
    int receiveCount=0;
    String textToBeSent;
    public ClientThread(InetAddress groupOwnerAddress,int Port,String textToBeSent) {
        this.groupOwnerAddress=groupOwnerAddress;
        this.Port=Port;
        this.textToBeSent=textToBeSent;
    }

    @Override
    public void run() {
        if(groupOwnerAddress!=null && Port!=0){
            while(true){
                if(socket==null){
                    try {
                        socket=new DatagramSocket(Port);
                        socket.setSoTimeout(1);
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }

                    try{
                        sendData=(textToBeSent).getBytes();
                        sendCount++;
                        DatagramPacket datagramPacket=new DatagramPacket(sendData,sendData.length,groupOwnerAddress,Port);
                        socket.send(datagramPacket);
                        Log.e("Client to Server Data Send","::::::"+textToBeSent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try{
                        DatagramPacket receivePacket=new DatagramPacket(receiveData,receiveData.length);
                        socket.receive(receivePacket);
                        receiveString=new String(receivePacket.getData(),0,receivePacket.getLength());
                        Log.e("Client ","Receive String"+receiveString);
                        receiveCount++;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

            }
        }

    }
}
