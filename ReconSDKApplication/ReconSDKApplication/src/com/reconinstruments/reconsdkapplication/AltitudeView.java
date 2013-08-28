package com.reconinstruments.reconsdkapplication;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import com.reconinstruments.ReconSDK.*;

public class AltitudeView extends Activity implements IReconDataReceiver
{
	// SDK Manager copy for this activity
    ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);
    
    // View Fields
    private TextView mPressure;
    private TextView mAltCurrent;
    private TextView mAltMax;
    private TextView mAltMin;
    private ImageView mImage;

	public void onReceiveCompleted(int status, ReconDataResult result)
	{
		
		// check status first
  		if (status != ReconSDKManager.STATUS_OK)
  		{
  			Toast toast = Toast.makeText(this, "Communication Failure with Transcend Service", Toast.LENGTH_LONG); //SHOULDN'T THAT SAY MOD-LIVE INSTEAD OF TRANSCEND? 
  			toast.show();
  			
  			return;
  		}
  		
  		// now simply dump data into text fields
  		ReconAltitude alt = (ReconAltitude)result.arrItems.get(0);
  		
  		String strText = String.format(" %.2f hPa", alt.GetPressureAltitude().GetPressure() );
  		mPressure.setText(strText );
			
  		strText = String.format(" %.2f", alt.GetAltitude() );
  		mAltCurrent.setText(strText);
  		
  		strText = String.format(" %.2f", alt.GetMaxAltitude() );
  		mAltMax.setText(strText);
  		
  		strText = String.format(" %.2f", alt.GetMinAltitude() );
  		mAltMin.setText(strText);
  		
	}

	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState); 

	    setContentView(R.layout.altitudeview);
	    
	    float textSize = 20;
	    
	    // get our edit fields
	    mPressure = (TextView) findViewById(R.id.id_altpressure);
	    mPressure.setTextSize(textSize);
        mPressure.setText("0 hPa");
        
        
	    mAltCurrent = (TextView) findViewById(R.id.id_altvalue);
	    mAltCurrent.setTextSize(textSize);
        mAltCurrent.setText("0 m"); 
        
        mAltMax = (TextView) findViewById(R.id.id_altmax);
        mAltMax.setTextSize(textSize);
        mAltMax.setText("0 m");
        
        mAltMin = (TextView) findViewById(R.id.id_altmin);
        mAltMin.setTextSize(textSize);
        mAltMin.setText("0 m"); 
        
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
            // trigger altitude event fetch
	        mDataManager.receiveData(this, ReconEvent.TYPE_ALTITUDE);
        }
	}
	
	// full update callback -- not used
	public void onFullUpdateCompleted(int status,
			ArrayList<ReconDataResult> results) 
	{
		

	}

}
