package com.example2.bluetoothtest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothService extends Thread{
	private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private InputStream mmInStream;
    private OutputStream mmOutStream;
 
    public BluetoothService(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;
        mmInStream=null;
        mmOutStream=null;
        
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { }
        mmSocket = tmp;
    }
 
    public void run() {
        // Cancel discovery because it will slow down the connection
    	InputStream tmpIn = null;
        OutputStream tmpOut = null;
    	BluetoothAdapter mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.cancelDiscovery();
 
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
        try {
            tmpIn = mmSocket.getInputStream();
            tmpOut = mmSocket.getOutputStream();
        } catch (IOException e) {	return; }
        	
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        // Do work to manage the connection (in a separate thread)
      // manageConnectedSocket(mmSocket);
    }
 
    /////////////write //////////////
    
    public void write(byte[] buffer) {
        try {
            mmOutStream.write(buffer);

            // Share the sent message back to the UI Activity
           
        } catch (IOException e) {
            //Log.e(TAG, "Exception during write", e);
        }
    }
    
    ///////////////////////////////
    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
    public void manageConnectedSocket(BluetoothSocket mSocket){
    	
    }
}
