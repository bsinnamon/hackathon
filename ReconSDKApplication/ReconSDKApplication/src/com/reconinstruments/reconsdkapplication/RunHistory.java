package com.reconinstruments.reconsdkapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.reconinstruments.ReconSDK.*;

public class RunHistory extends Activity implements IReconDataReceiver
{
   	// standard ident tag
    @SuppressWarnings("unused")
	private static final String TAG = RunHistory.class.getSimpleName(); 
    
    // SDK Manager copy for this activity
    ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);
    
    // data receiver callback
	public void onReceiveCompleted(int status, ReconDataResult result)
	{
	    // JAR API will call us back right here when jump history has been retrieved
  		
 		// check status first
  		if (status != ReconSDKManager.STATUS_OK)
  		{
  			Toast toast = Toast.makeText(this, "Communication Failure with Transcend Service", Toast.LENGTH_LONG);
  			toast.show();
  			
  			return;
  		}
  		
  		// get list view object
  	    ListView lv= (ListView)findViewById(R.id.runlist);
  	    
  	   // create the grid item mapping
  	   String[] from = new String[] 
  	   {
  			 getString(R.string.RunSequence),
  			 getString(R.string.RunStart),
  			 getString(R.string.RunAvgSpeed),
  			 getString(R.string.RunMaxSpeed),
  			 getString(R.string.RunDistance),
  			 getString(R.string.RunVertical),
  	   };
  	   
  	   int[] to = new int[] { R.id.run_sequence, R.id.run_start, R.id.run_averagespeed, R.id.run_maxspeed, R.id.run_distance, R.id.run_vertical };
  		
  	   // prepare list of all runs
  	   List<HashMap<String, String>> runMaps = new ArrayList<HashMap<String, String>>();
  	   
  	   for (int i = 0; i < result.arrItems.size(); i++)
  	   {
  	  	    HashMap<String, String> map = new HashMap<String, String>();
  	  	    ReconRun run = (ReconRun)result.arrItems.get(i);
  	  	    
  	  	    // sequence
  	  	    String strData = String.format("%d", run.GetSequence() );
  	  	    map.put(getString(R.string.RunSequence), strData);
  	  	    
  	  	    // start
 	  	    strData = String.format("%d", run.GetStart() );
  	  	    map.put(getString(R.string.RunStart), strData);
  	  	    
  	  	    // average speed
	  	    strData = String.format("%.02f", run.GetAverageSpeed() );
  	  	    map.put(getString(R.string.RunAvgSpeed), strData);
  	  	    
  	  	    // maximum speed
	  	    strData = String.format("%.02f", run.GetMaximumSpeed() );
  	  	    map.put(getString(R.string.RunMaxSpeed), strData);
  	  	    
  	  	    // distance
	  	    strData = String.format("%.02f", run.GetDistance() );
  	  	    map.put(getString(R.string.RunDistance), strData);
  	  	    
  	  	    // vertical
	  	    strData = String.format("%.02f", run.GetVertical() );
  	  	    map.put(getString(R.string.RunVertical), strData);
  		            
  		    runMaps.add(map);
  	   }
  		 
  		        
  	   // fill in the runitem layout
  	   SimpleAdapter adapter = new SimpleAdapter(this, runMaps, R.layout.runitem, from, to);
  	   lv.setAdapter(adapter);

	}
	
	// full update callback -- not used
	public void onFullUpdateCompleted(int status,
			ArrayList<ReconDataResult> results)
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState); 
	    
	    setContentView(R.layout.runview);
	    
        // check if we are started internally, in which case we'll have data array in extras
	    // Otherwise retrieve data from the server
        Intent i = getIntent();
        Bundle bdata = i.getBundleExtra(ReconSDKApplicationActivity.RECON_DATA_BUNDLE);
        if (bdata != null)
        {
			try 
			{
				ReconDataResult result = ReconDataResult.fromBundle(bdata);
				this.onReceiveCompleted(ReconSDKManager.STATUS_OK, result);
			} 
			catch (Exception ex)
			{
				Toast toast = Toast.makeText(this, "Unexpected Internal Error", Toast.LENGTH_LONG);
				toast.show();
			}
        	
        }
        else
        {
    	    // Retrieve Run History using Recon SDK Functionality
    	    mDataManager.receiveData(this, ReconEvent.TYPE_RUN);
        }
	}

}
