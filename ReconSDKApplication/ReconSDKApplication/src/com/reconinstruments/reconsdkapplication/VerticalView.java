package com.reconinstruments.reconsdkapplication;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.reconinstruments.ReconSDK.*;

public class VerticalView extends Activity implements IReconDataReceiver 
{
	// SDK Manager copy for this activity
    ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);
    
    // View Fields
    private TextView mVertical;
    private TextView mPrevVertical;
    private TextView mAllTimeVertical;
  
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState); 

	    setContentView(R.layout.vertview);
	    
	    // get our edit fields
	    mVertical = (TextView) findViewById(R.id.id_vert);
        mVertical.setText("0");
        
	    mPrevVertical = (TextView) findViewById(R.id.id_vert_prev);
	    mPrevVertical.setText("0");
        
	    mAllTimeVertical = (TextView) findViewById(R.id.id_vert_alltime);
	    mAllTimeVertical.setText("0");
        
        float textSize = 25;
        
        mVertical.setTextSize(textSize);
        mPrevVertical.setTextSize(textSize);
        mAllTimeVertical.setTextSize(textSize);
 		
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
    	    // triger Vertical Event fetch
    	    mDataManager.receiveData(this, ReconEvent.TYPE_VERTICAL);
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
  		ReconVertical vert = (ReconVertical)result.arrItems.get(0);
  		
  		String strText = String.format(" %.2f", vert.GetVertical() );
  		mVertical.setText(strText );
			
  		strText = String.format(" %.2f", vert.GetPreviousVertical() );
  		mPrevVertical.setText(strText );
  		
  		strText = String.format(" %.2f", vert.GetAllTimeVertical() );
  		mAllTimeVertical.setText(strText );
	}
	
	// full update callback -- not used
	public void onFullUpdateCompleted(int status,
			ArrayList<ReconDataResult> results)
	{
		
	}

}
