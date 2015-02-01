package com.example2.bluetoothtest;

import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

// broadcast receiver to scan and add discoverable devices
public class MyReceiver extends BroadcastReceiver {
	private View mV;
	private BluetoothAdapter mBluetoothAdapter;
	public MyReceiver(View v,BluetoothAdapter ba){
			mV=v;
		 mBluetoothAdapter=ba;
	   }
	@Override
	   public void onReceive(Context context, Intent intent) {
	      Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
	 	 ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(mV.getContext(), R.layout.generate_text);
			ListView mConversationView = (ListView)mV.findViewById(R.id.scaned_devices);
		        mConversationView.setAdapter(mArrayAdapter );
				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
				// If there are paired devices
				if (pairedDevices.size() > 0) {
				    // Loop through paired devices
				    for (BluetoothDevice device : pairedDevices) {
				    	
						// Add the name and address to an array adapter to show in a ListView
				        mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				    }
				    
				}
	   
	}

}