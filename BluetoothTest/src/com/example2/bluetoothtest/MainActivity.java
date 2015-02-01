package com.example2.bluetoothtest;
import java.util.ArrayList;
import java.util.Set;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, new PlaceholderFragment());
            transaction.commit();
            }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void scanDevices(View v)
	{
		 Toast.makeText(getBaseContext(), "Entered.", Toast.LENGTH_LONG).show();
		   
		//BroadcastReceiver myReceiver = new MyReceiver();
		//IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		//registerReceiver(, filter);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private BluetoothAdapter mBluetoothAdapter = null;
		private static final int REQUEST_ENABLE_BT = 3;
		private  ArrayAdapter<String> mArrayAdapter1;
		private ListView mConversationView1;
		public ArrayList<BluetoothDevice> bluetoothDevices;
		BluetoothService myBConnection;
		    View rootView = null;   
	    
		public PlaceholderFragment() {
		}

		  @Override
		    public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        setHasOptionsMenu(true);
		        // Get local Bluetooth adapter
		         mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		 
		        // If the adapter is null, then Bluetooth is not supported
		        if (mBluetoothAdapter == null) {
		            FragmentActivity activity = getActivity();
		            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
		            activity.finish();
		        }
		    }
		  
		    @Override
		    public void onStart() {
		        super.onStart();
		      
		        // If BT is not on, request that it be enabled.
		        // setupChat() will then be called during onActivityResult
		        if (!mBluetoothAdapter.isEnabled()) {
		           Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		            // Otherwise, setup the chat session
		        	
		        }
		        
		        	
		        	
		       
		    }
	@Override
	public void onActivityResult(int requestCode, int resultCode,
		             Intent data) {
		 
		setup();
	 }
		  
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
		 rootView = inflater.inflate(R.layout.bluetooth_fragment,
					container, false);
			return rootView;
		}
		
		
////////////broadcast receiver for scanning ///////////////
		private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
			    public void onReceive(Context context, Intent intent) {
						
			    	
			        String action = intent.getAction();
			        // When discovery finds a device
			        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			            // Get the BluetoothDevice object from the Intent
			            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			            // Add the name and address to an array adapter to show in a ListView
			            mArrayAdapter1.add(device.getName() + "\n" + device.getAddress());
			        }
			    }
			};
			
  //////////////////////////////////////////////////////////////
		
		private void setup() {
			
			 ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.generate_text);
			final ListView mConversationView = (ListView) rootView.findViewById(R.id.paired_devices);
		        mConversationView.setAdapter(mArrayAdapter );
				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
				bluetoothDevices= new ArrayList<BluetoothDevice>();
				// If there are paired devices
				if (pairedDevices.size() > 0) {
				    // Loop through paired devices
				    for (BluetoothDevice device : pairedDevices) {
				    	
						// Add the name and address to an array adapter to show in a ListView
				        mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				        bluetoothDevices.add(device);
				    }
				    
				}
			//////////// onclick listner for paired devices ////////////////
				mConversationView.setOnItemClickListener(new OnItemClickListener() {
					 @Override
					public void onItemClick(AdapterView<?> parent, View view,
			                int position, long id) {
						 byte[] buffer={1,1,2};
						Object o = mConversationView.getItemAtPosition(position);
			            String str=(String)o;//As you are using Default String Adapter
			            Toast.makeText(getActivity().getApplicationContext(),str,Toast.LENGTH_SHORT).show();
			             myBConnection = new BluetoothService(bluetoothDevices.get(position));
			             myBConnection.start();
			             try {
			            	 Thread.sleep(3000);
			            	 myBConnection.write(buffer);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			///////////////////////////////////////////////////////////////
				
			 mArrayAdapter1 = new ArrayAdapter<String>(getActivity(), R.layout.generate_text);
				 mConversationView1 = (ListView) rootView.findViewById(R.id.scaned_devices);
			     mConversationView1.setAdapter(mArrayAdapter1 );
			  // onclick listner for scanned list items 
			     mConversationView1.setOnItemClickListener(new OnItemClickListener() {
			    	 @Override
						public void onItemClick(AdapterView<?> parent, View view,
				                int position, long id) {
							Object o = mConversationView1.getItemAtPosition(position);
				            String str=(String)o;//As you are using Default String Adapter
				            Toast.makeText(getActivity().getApplicationContext(),str,Toast.LENGTH_SHORT).show();
				       
							
						}
					});
				 mBluetoothAdapter.startDiscovery();
				 IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
				getActivity().registerReceiver(mReceiver, filter);	
		 }
		
	}
}
