package com.reconinstruments.reconsdkapplication;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.reconinstruments.ReconSDK.*;

public class DistanceView extends Activity implements IReconDataReceiver
{
	// SDK Manager copy for this activity
    ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);
    
    // View Fields
    private TextView mDistance;
    private TextView mDistanceHorizontal;
    private TextView mDistanceVertical;
    private TextView mDistanceAllTime;

	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState); 

	    setContentView(R.layout.distview);
	    
	    // get our edit fields
	    mDistance = (TextView) findViewById(R.id.id_dist);
        mDistance.setText("0");
        
	    mDistanceHorizontal = (TextView) findViewById(R.id.id_dist_horiz);
	    mDistanceHorizontal.setText("0");
        
	    mDistanceVertical = (TextView) findViewById(R.id.id_dist_ver);
        mDistanceVertical.setText("0");
        
	    mDistanceAllTime = (TextView) findViewById(R.id.id_dist_alltime);
	    mDistanceAllTime.setText("0");
        
        float textSize = 25;
        
        mDistance.setTextSize(textSize);
        mDistanceHorizontal.setTextSize(textSize);
        mDistanceVertical.setTextSize(textSize);
        mDistanceAllTime.setTextSize(textSize);
 		
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
    	    // triger Distance Event fetch
    	    mDataManager.receiveData(this, ReconEvent.TYPE_DISTANCE);
        }
        

	}
	
	public void onReceiveCompleted(int status, ReconDataResult result)
	{
		// check status first
  		if (status != ReconSDKManager.STATUS_OK)
  		{
  			Toast toast = Toast.makeText(this, "Communication Failure with Transcend Service", Toast.LENGTH_LONG);
  			toast.show();
  			
  			return;
  		}
  		
  		// now simply dump data into text fields
  		ReconDistance dist = (ReconDistance)result.arrItems.get(0);
  		
  		String strText = String.format(" %.2f", dist.GetDistance() );
  		mDistance.setText(strText );
			
  		strText = String.format(" %.2f", dist.GetHorizontalDistance() );
  		mDistanceHorizontal.setText(strText );
  		
  		strText = String.format(" %.2f", dist.GetVerticalDistance() );
  		mDistanceVertical.setText(strText );
  		
  		strText = String.format(" %.2f", dist.GetAllTimeDistance() );
  		mDistanceAllTime.setText(strText );
  	
  		
	}

	// full update callback -- not used
	public void onFullUpdateCompleted(int status,
			ArrayList<ReconDataResult> results)
	{
		
	}

}
