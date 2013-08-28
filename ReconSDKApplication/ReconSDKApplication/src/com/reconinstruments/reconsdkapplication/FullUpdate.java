package com.reconinstruments.reconsdkapplication;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.reconinstruments.ReconSDK.*;

public class FullUpdate extends ListActivity implements IReconDataReceiver
{
  	// standard ident tag
    @SuppressWarnings("unused")
	private static final String TAG = FullUpdate.class.getSimpleName(); 
 
    
    // SDK Manager copy for this activity
    ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);
    
    private String [] mItems = new String [0];      // display strings
    private ArrayList<ReconDataResult> mResults = new ArrayList<ReconDataResult>();
    
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	   super.onCreate(savedInstanceState);
	 
		  
	    // retrieve Full Update using Recon SDK functionality
	    mDataManager.receiveFullUpdate(this); 

	}
	
	// full update callback
	public void onFullUpdateCompleted(int status,
			ArrayList<ReconDataResult> results)
	{
		// save local copy
		mResults.clear();
		mResults.addAll(results);
		
		// check status first
  		if (status != ReconSDKManager.STATUS_OK)
  		{
  			Toast toast = Toast.makeText(this, "Communication Failure with Transcend Service", Toast.LENGTH_LONG);
  			toast.show();
  			
  			return;
  		}
  		
  		// populate string adapter array

  		mItems = new String[results.size()-1];
  		int index = 0;
  		for (int i = 0; i < results.size(); i++)
  		{
  			ReconDataResult result = results.get(i);
  			// Recon Location is an enhancement feature and unavailable now
  			if(!result.toString().equals("ReconLocation"))
  			{
  				mItems[index] = result.toString();
  				index++;
  			}
  		}
  		
  		setListAdapter(new ArrayAdapter<String>(this, R.layout.fullitem, mItems));
		ListView lv = getListView();
	
		// now insert displays and on-click launch activity passing received DataResult
		lv.setTextFilterEnabled(true);

		  lv.setOnItemClickListener(new OnItemClickListener() 
		  {

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id)
			{
				ReconDataResult result = mResults.get(position);
				ReconEvent evt = result.arrItems.get(0);
				
				Intent intent = null;
				
                // resolve type
				switch (evt.getType() )
				{
				 
					case ReconEvent.TYPE_ALTITUDE:
						
						intent = new Intent(FullUpdate.this, AltitudeView.class);
						break;
						
					case ReconEvent.TYPE_SPEED:
						
						intent = new Intent(FullUpdate.this, SpeedTimeView.class);
						break;
						
				    case ReconEvent.TYPE_DISTANCE:
							
					    intent = new Intent(FullUpdate.this, DistanceView.class);
					    break;
							
			        case ReconEvent.TYPE_JUMP:
						
						intent = new Intent(FullUpdate.this, JumpHistory.class);
						break;
						
			        case ReconEvent.TYPE_RUN:
						
						intent = new Intent(FullUpdate.this, RunHistory.class);
						break;
						
			        case ReconEvent.TYPE_TEMPERATURE:
						
						intent = new Intent(FullUpdate.this, TemperatureView.class);
						break;
					
			        case ReconEvent.TYPE_VERTICAL:
						
						intent = new Intent(FullUpdate.this, VerticalView.class);
						break;
						
						
				    default:
						return;
				} 
				
				// now pass received result in intent
			    intent.putExtra(ReconSDKApplicationActivity.RECON_DATA_BUNDLE, result.toBundle() );
		
				try
				{
				   startActivity(intent);
				}
				catch (Exception ex)
				{
					Toast toast = Toast.makeText(FullUpdate.this, ex.getMessage(), Toast.LENGTH_LONG );
					toast.show();
				} 

			}  

		
		  }); 
  		
	
	}


	// single update -  not used
	public void onReceiveCompleted(int status, ReconDataResult result)
	{
	}

}
