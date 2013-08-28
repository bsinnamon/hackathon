package com.reconinstruments.reconsdkapplication;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.reconinstruments.ReconSDK.*;


public class TemperatureView extends Activity implements IReconDataReceiver
{
	// SDK Manager copy for this activity
    ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);
    
    // View Fields
    private TextView mTemp;
    private TextView mTempMin;
    private TextView mTempMax;
    private TextView mTempMinAllTime;
    private TextView mTempMaxAllTime;
    
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
  		ReconTemperature temp = (ReconTemperature)result.arrItems.get(0);
  		
  		String strText = String.format(" %d", temp.GetTemperature() );
  		mTemp.setText(strText );
			
  		strText = String.format(" %d", temp.GetMaxTemperature() );
  		mTempMax.setText(strText );
  		
  		strText = String.format(" %d", temp.GetMinTemperature() );
  		mTempMin.setText(strText );
  		
  		strText = String.format(" %d", temp.GetAllTimeMaxTemperature() );
  		mTempMaxAllTime.setText(strText );
  		
  		strText = String.format(" %d", temp.GetAllTimeMinTemperature() );
  		mTempMinAllTime.setText(strText );
  		
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState); 

	    setContentView(R.layout.tempview);
	    
	    // get our edit fields
	    mTemp = (TextView) findViewById(R.id.id_temp);
        mTemp.setText("0");
        
	    mTempMin = (TextView) findViewById(R.id.id_temp_min);
        mTempMin.setText("0");
        
	    mTempMax = (TextView) findViewById(R.id.id_temp_max);
        mTempMax.setText("0");
        
	    mTempMaxAllTime = (TextView) findViewById(R.id.id_temp_max_alltime);
        mTempMaxAllTime.setText("0");
        
	    mTempMinAllTime = (TextView) findViewById(R.id.id_temp_min_alltime);
        mTempMinAllTime.setText("0");
        
        float textSize = 25;
        
 		mTemp.setTextSize(textSize);
 		mTempMax.setTextSize(textSize);
 		mTempMin.setTextSize(textSize);
 		mTempMaxAllTime.setTextSize(textSize);
 		mTempMinAllTime.setTextSize(textSize);
 		
 		mTemp.setBackgroundColor(0xff0000);
 		
 		
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
    	    // triger Temperature Event fetch
    	    mDataManager.receiveData(this, ReconEvent.TYPE_TEMPERATURE);
        }
	}
	
	// full update callback -- not used
	public void onFullUpdateCompleted(int status,
			ArrayList<ReconDataResult> results)
	{
		
	}

}
